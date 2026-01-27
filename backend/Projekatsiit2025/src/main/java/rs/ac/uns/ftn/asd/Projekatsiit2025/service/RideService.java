package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
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

        List<Ride> rides = rideRepository
                .findByDriverIdAndStartingTimeBetween(driverId, fromDateTime, toDateTime);

        return rides.stream().map(this::mapToDTO).toList();
    }

    private DriverRideHistoryDTO mapToDTO(Ride ride) {
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
    
    
}
