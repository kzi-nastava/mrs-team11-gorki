package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.report.ReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.report.ReportPointDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.report.SummaryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.UserNotFoundException;

@Service
public class ReportService {

	@Autowired
	private final RideRepository rideRepository;
	@Autowired
	private final UserRepository userRepository;

    public ReportService(RideRepository rideRepository, UserRepository userRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
    }

    public ReportDTO getDriverReport(Long driverId, LocalDate from, LocalDate to) {
        var range = normalizeRange(from, to);

        List<Ride> rides = rideRepository.findAllByDriverIdAndStatusAndStartingTimeBetween(
                driverId, RideStatus.FINISHED, range.from, range.to
        );

        return buildReport(rides, range.from.toLocalDate(), range.to.toLocalDate());
    }

    public ReportDTO getPassengerReport(Long passengerId, LocalDate from, LocalDate to) {
        var range = normalizeRange(from, to);

        List<Ride> rides = rideRepository.findAllByLinkedPassengers_IdAndStatusAndStartingTimeBetween(
                passengerId, RideStatus.FINISHED, range.from, range.to
        );

        List<Ride> created = rideRepository.findAllByCreatorIdAndStatusAndStartingTimeBetween(
                passengerId, RideStatus.FINISHED, range.from, range.to
        );

        Map<Long, Ride> unique = new HashMap<>();
        for (Ride r : rides) unique.put(r.getId(), r);
        for (Ride r : created) unique.put(r.getId(), r);

        return buildReport(new ArrayList<>(unique.values()), range.from.toLocalDate(), range.to.toLocalDate());
    }

    public ReportDTO getAdminAggregateReport(LocalDate from, LocalDate to) {
        var range = normalizeRange(from, to);

        List<Ride> rides = rideRepository.findAllByStatusAndStartingTimeBetween(
                RideStatus.FINISHED, range.from, range.to
        );

        return buildReport(rides, range.from.toLocalDate(), range.to.toLocalDate());
    }

    public ReportDTO getAdminUserReport(String email, LocalDate from, LocalDate to) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found."));
        if(user.getRole().equals(UserRole.PASSENGER)) {
        	return getPassengerReport(user.getId(), from, to);
        }
        if(user.getRole().equals(UserRole.DRIVER)) {
        	return getDriverReport(user.getId(), from, to);
        }
        throw new IllegalArgumentException("Report not supported for this role.");
    }

    private static class Range {
        LocalDateTime from;
        LocalDateTime to;
        Range(LocalDateTime from, LocalDateTime to) { this.from = from; this.to = to; }
    }

    private Range normalizeRange(LocalDate from, LocalDate to) {
        LocalDate start = (from != null) ? from : LocalDate.now().minusDays(30);
        LocalDate end = (to != null) ? to : LocalDate.now();

        LocalDateTime fromDT = start.atStartOfDay();
        LocalDateTime toDT = end.plusDays(1).atStartOfDay().minusSeconds(1);

        return new Range(fromDT, toDT);
    }

    private ReportDTO buildReport(List<Ride> rides, LocalDate from, LocalDate to) {
        Map<LocalDate, ReportPointDTO> perDay = new HashMap<>();

        for (Ride r : rides) {
            if (r.getStartingTime() == null) continue;

            LocalDate day = r.getStartingTime().toLocalDate();

            ReportPointDTO p = perDay.computeIfAbsent(day, d -> {
                ReportPointDTO x = new ReportPointDTO();
                x.setDate(d);
                x.setRideCount(0);
                x.setKilometers(0);
                x.setMoney(0);
                return x;
            });

            p.setRideCount(p.getRideCount() + 1);
            p.setKilometers(p.getKilometers() + r.getRoute().getDistance());
            double money = r.getPrice();
            p.setMoney(p.getMoney() + money);
        }

        List<ReportPointDTO> points = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            points.add(perDay.getOrDefault(d, emptyPoint(d)));
        }

        points.sort(Comparator.comparing(ReportPointDTO::getDate));

        int cumRides = 0;
        double cumKm = 0;
        double cumMoney = 0;

        for (ReportPointDTO p : points) {
            cumRides += p.getRideCount();
            cumKm += p.getKilometers();
            cumMoney += p.getMoney();

            p.setCumulativeRideCount(cumRides);
            p.setCumulativeKilometers(cumKm);
            p.setCumulativeMoney(cumMoney);
        }

        int days = points.size();
        SummaryDTO summary = new SummaryDTO();
        summary.setTotalRides(cumRides);
        summary.setTotalKilometers(cumKm);
        summary.setTotalMoney(cumMoney);

        summary.setAvgRidesPerDay(days == 0 ? 0 : cumRides / (double) days);
        summary.setAvgKilometersPerDay(days == 0 ? 0 : cumKm / (double) days);
        summary.setAvgMoneyPerDay(days == 0 ? 0 : cumMoney / (double) days);

        ReportDTO dto = new ReportDTO();
        dto.setPoints(points);
        dto.setSummary(summary);
        return dto;
    }

    private ReportPointDTO emptyPoint(LocalDate d) {
        ReportPointDTO p = new ReportPointDTO();
        p.setDate(d);
        p.setRideCount(0);
        p.setKilometers(0);
        p.setMoney(0);
        p.setCumulativeRideCount(0);
        p.setCumulativeKilometers(0);
        p.setCumulativeMoney(0);
        return p;
    }
}
