package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

@Service
public class RideService {
    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }
    
    @Transactional(readOnly = true)
    public Collection<DriverRideHistoryDTO> getDriverRideHistory(
            Long driverId,
            LocalDate from,
            LocalDate to) {

        LocalDateTime fromDateTime = (from != null)
                ? from.atStartOfDay()
                : LocalDate.of(2000, 1, 1).atStartOfDay();  // SAFE MIN

        LocalDateTime toDateTime = (to != null)
                ? to.atTime(23, 59, 59)
                : LocalDate.of(2100, 1, 1).atStartOfDay();  // SAFE MAX

        List<Ride> rides = rideRepository.findByDriverIdAndStatusAndStartingTimeBetween(
                driverId,
                RideStatus.FINISHED,
                fromDateTime,
                toDateTime
        );

        return rides.stream().map(this::mapDriverHistoryToDTO).toList();
    }

    private DriverRideHistoryDTO mapDriverHistoryToDTO(Ride ride) {
        DriverRideHistoryDTO dto = new DriverRideHistoryDTO();
        dto.setRideId(ride.getId());
        dto.setStartingTime(ride.getStartingTime());
        dto.setEndingTime(ride.getEndingTime());
        dto.setPrice(ride.getPrice());
        dto.setPanicActivated(ride.getPanicActivated());
        dto.setCanceled(ride.getStatus() == RideStatus.CANCELED);
        dto.setCanceledBy(ride.getCancelledBy());
        dto.setPassengers(ride.getLinkedPassengers()
                .stream()
                .map(p -> new PassengerInRideDTO(
                        p.getEmail(),
                        p.getFirstName(),
                        p.getLastName(),
                        String.valueOf(p.getPhoneNumber())
                ))
                .toList()
        );
        Route route = ride.getRoute();
        GetRouteDTO routeDTO = new GetRouteDTO(
        	    route.getId(),
        	    route.getLocations().stream()
        	         .map((rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location loc) -> 
        	             new LocationDTO(loc.getLatitude(), loc.getLongitude(),"Neka adresa"))
        	         .toList(),
        	    route.getDistance(),
        	    route.getEstimatedTime()
		);
        dto.setRoute(routeDTO);
        return dto;
    }
    
    @Transactional(readOnly = true)
    public DriverRideHistoryDTO getActiveRideForPassenger(Long passengerId) {

        Ride ride = rideRepository
                .findByCreator_IdAndStatus(passengerId, RideStatus.STARTED);

        if (ride == null) {
            throw new RuntimeException("Passenger nema aktivnu vožnju");
        }

        return mapDriverHistoryToDTO(ride);
    }

    @Transactional
    public FinishedRideDTO finishRide(Long driverId, FinishRideDTO dto) {
        // 1️ Dohvati voznju po rideId i driverId
        Ride ride = rideRepository.findByIdAndDriver_Id(dto.getRideId(), driverId);
        if (ride == null) {
            throw new RuntimeException("Voznja ne postoji ili nije dodeljena ovom vozacu");
        }

        // 2️ Zavrsavanje voznje
        ride.setStatus(RideStatus.FINISHED);
        ride.setEndingTime(LocalDateTime.now());

        // 3️ Placanje
        if (Boolean.TRUE.equals(dto.getPaid())) {
            ride.setPaid(true); 
        }

        Driver driver = ride.getDriver();

        // 4️ Proveri sledecu zakazanu voznju
        Ride nextRide = rideRepository
                .findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(driverId, RideStatus.ACCEPTED);

        FinishedRideDTO response = new FinishedRideDTO();
        response.setRideId(ride.getId());
        response.setRideStatus(ride.getStatus());

        if (nextRide != null) {
            driver.setStatus(DriverStatus.BUSY);
            response.setDriverStatus(driver.getStatus());
            response.setHasNextScheduledRide(true);
        } else {
            driver.setStatus(DriverStatus.ACTIVE);
            response.setDriverStatus(driver.getStatus());
            response.setHasNextScheduledRide(false);
        }

        return response;
    }

}
