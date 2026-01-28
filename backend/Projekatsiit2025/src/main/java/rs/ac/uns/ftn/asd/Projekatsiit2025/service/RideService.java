package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetPassengerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideCancelRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

@Service
public class RideService {
    private final RideRepository rideRepository;
    private final DriverAssignmentService driverAssignmentService;
    private final PriceConfigService priceConfigService;
    private final PassengerRepository passengerRepository;

    public RideService(RideRepository rideRepository, DriverAssignmentService driverAssignmentService, PriceConfigService priceConfigService, PassengerRepository passengerRepository) {
        this.rideRepository = rideRepository;
        this.driverAssignmentService = driverAssignmentService;
        this.priceConfigService = priceConfigService;
        this.passengerRepository = passengerRepository;
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
    
    @Transactional
    public CreatedRideDTO createRide(CreateRideDTO dto) {
    	Ride ride = new Ride();
    	ride.setScheduledTime(dto.getScheduledTime());
    	Route route = new Route();
    	List<Location> locations = new ArrayList<Location>();
    	for(LocationDTO loc : dto.getRoute().getLocations()) {
    		locations.add(new Location(loc.getLatitude(), loc.getLongitude(), loc.getAddress()));
    	}
    	route.setLocations(locations);
    	route.setDistance(calculateDistance(route));
    	route.setEstimatedTime(calculateEstimatedTime(route));
    	ride.setRoute(route);
    	List<Passenger> linkedPassengers = new ArrayList<Passenger>();
    	if(dto.getLinkedPassengersEmails().size() != 0) {
    		for(String email : dto.getLinkedPassengersEmails()) {
        		Passenger passenger = passengerRepository.findByEmail(email).get();
        		linkedPassengers.add(passenger);
        	}
    	}
    	ride.setLinkedPassengers(linkedPassengers);
    	Passenger creator = passengerRepository.findById(dto.getCreatorId()).get();
    	ride.setCreator(creator);
    	ride.setDriver(driverAssignmentService.selectDriver(dto.getBabyTransport(), dto.getPetFriendly(), VehicleType.valueOf(dto.getVehicleType()), route, dto.getScheduledTime()));
    	ride.setStatus(RideStatus.REQUESTED);
    	ride.setPriceConfig(priceConfigService.getCurrentConfig());
    	ride.setPrice(priceConfigService.calculatePrice(VehicleType.valueOf(dto.getVehicleType()), route.getDistance()));
    	rideRepository.save(ride);
    	return mapToCreatedRideDTO(ride);
    	
    }
    
    @Transactional
    public GetRideDTO startRide(Long id) {
    	Ride ride = rideRepository.findById(id).get();
    	ride.setStatus(RideStatus.STARTED);
    	ride.getDriver().setStatus(DriverStatus.BUSY);
    	ride.setStartingTime(LocalDateTime.now());
    	ride.setEndingTime(ride.getStartingTime().plusMinutes(getEstimatedTime(ride.getRoute())));
    	ride.setPanicActivated(false);
    	ride.setCancellationReason("");
    	rideRepository.save(ride);
    	return mapToGetRideDTO(ride);
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
        	         .map(loc -> 
        	             new LocationDTO(loc.getLatitude(), loc.getLongitude(), loc.getAddress()))
        	         .toList(),
        	    route.getDistance(),
        	    route.getEstimatedTime()
		);
        dto.setRoute(routeDTO);
        return dto;
    }
    
    
    private GetRideDTO mapToGetRideDTO(Ride ride) {
    	GetRideDTO dto = new GetRideDTO();
    	dto.setId(ride.getId());
    	dto.setStatus(ride.getStatus());
    	dto.setPrice(ride.getPrice());
    	dto.setScheduledTime(ride.getScheduledTime());
    	dto.setStartingTime(ride.getStartingTime());
    	dto.setEndingTime(ride.getEndingTime());
    	dto.setPanicActivated(ride.getPanicActivated());
    	dto.setCancellationReason(ride.getCancellationReason());
    	dto.setDriver(mapToGetDriverDTO(ride.getDriver()));
    	dto.setCreator(mapToGetPassengerDTO(ride.getCreator()));
    	List<GetPassengerDTO> linkedPassengers = new ArrayList<GetPassengerDTO>();
    	for(Passenger passenger : ride.getLinkedPassengers()) {
    		linkedPassengers.add(mapToGetPassengerDTO(passenger));
    	}
    	dto.setLinkedPassengers(linkedPassengers);
    	return dto;
    }
    
    private CreatedRideDTO mapToCreatedRideDTO(Ride ride) {
        CreatedRideDTO dto = new CreatedRideDTO();
        dto.setId(ride.getId());
        dto.setStatus(ride.getStatus());
        dto.setPrice(ride.getPrice());
        dto.setScheduledTime(ride.getScheduledTime());
        dto.setDriver(ride.getDriver() != null ? mapToGetDriverDTO(ride.getDriver()) : null);
        dto.setRoute(ride.getRoute() != null ? mapToCreatedRouteDTO(ride.getRoute()) : null);
        dto.setPriceConfig(ride.getPriceConfig());
        
        if (ride.getLinkedPassengers() != null) {
            List<GetPassengerDTO> passengers = ride.getLinkedPassengers()
                .stream()
                .map(this::mapToGetPassengerDTO)
                .toList();
            dto.setLinkedPassengers(passengers);
        } else {
            dto.setLinkedPassengers(List.of());
        }

        dto.setCreator(ride.getCreator() != null ? mapToGetPassengerDTO(ride.getCreator()) : null);

        return dto;
    }
    
    private GetDriverDTO mapToGetDriverDTO(Driver driver) {
		GetDriverDTO dto = new GetDriverDTO();
		dto.setUser(new GetUserDTO());
		dto.getUser().setId(driver.getId());
		dto.getUser().setEmail(driver.getEmail());
		dto.getUser().setFirstName(driver.getFirstName());
		dto.getUser().setLastName(driver.getLastName());
		dto.getUser().setPhoneNumber(driver.getPhoneNumber());
        dto.getUser().setAddress(driver.getAddress());
        dto.getUser().setRole(driver.getRole());
        dto.getUser().setActive(driver.getActive());
        dto.getUser().setProfileImage(driver.getProfileImage());
        dto.setActivityLast24h(driver.getActivityLast24h());
        return dto;
	}
    
    private LocationDTO mapToLocationDTO(Location location) {
		LocationDTO dto = new LocationDTO();
		dto.setAddress(location.getAddress());
		dto.setLatitude(location.getLatitude());
		dto.setLongitude(location.getLongitude());
		return dto;
	}
    
    private CreatedRouteDTO mapToCreatedRouteDTO(Route route) {
    	CreatedRouteDTO dto = new CreatedRouteDTO();
    	dto.setId(route.getId());
    	List<LocationDTO> locations = new ArrayList<LocationDTO>();
		for(Location location : route.getLocations()) {
			locations.add(mapToLocationDTO(location));
		}
		dto.setLocations(locations);
		dto.setDistance(route.getDistance());
		dto.setEstimatedTime(route.getEstimatedTime());
		return dto;
    }
    
    private GetPassengerDTO mapToGetPassengerDTO(Passenger passenger) {
    	GetPassengerDTO dto = new GetPassengerDTO();
		dto.setUser(new GetUserDTO());
		dto.getUser().setId(passenger.getId());
		dto.getUser().setEmail(passenger.getEmail());
		dto.getUser().setFirstName(passenger.getFirstName());
		dto.getUser().setLastName(passenger.getLastName());
		dto.getUser().setPhoneNumber(passenger.getPhoneNumber());
        dto.getUser().setAddress(passenger.getAddress());
        dto.getUser().setRole(passenger.getRole());
        dto.getUser().setActive(passenger.getActive());
        dto.getUser().setProfileImage(passenger.getProfileImage());
        dto.setFavouriteRoutes(new ArrayList<GetRouteDTO>());
        for(Route route : passenger.getFavouriteRoutes()) {
        	dto.getFavouriteRoutes().add(mapToGetRouteDTO(route));
        }
        return dto;
    }
    
    private GetRouteDTO mapToGetRouteDTO(Route route) {
		GetRouteDTO dto = new GetRouteDTO();
		dto.setId(route.getId());
		List<LocationDTO> locations = new ArrayList<LocationDTO>();
		for(Location location : route.getLocations()) {
			locations.add(mapToLocationDTO(location));
		}
		dto.setLocations(locations);
		dto.setDistance(route.getDistance());
		dto.setEstimatedTime(route.getEstimatedTime());
		return dto;
	}
    
    private double calculateDistance(Route route) {
        if (route.getLocations() == null || route.getLocations().size() < 2) {
            return 0;
        }

        Location start = route.getLocations().get(0);
        Location end = route.getLocations().get(route.getLocations().size() - 1);

        return haversineDistance(start.getLatitude(), start.getLongitude(),
                                 end.getLatitude(), end.getLongitude());
    }
    
    public LocalDateTime calculateEstimatedTime(Route route) {
        double distanceKm = calculateDistance(route);
        double averageSpeedKmh = 40;

        double hours = distanceKm / averageSpeedKmh;
        long totalMinutes = Math.round(hours * 60);

        long hh = totalMinutes / 60;
        long mm = totalMinutes % 60;

        return LocalDateTime.of(0, 1, 1, (int) hh, (int) mm);
    }
    
    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; 

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }
    
    public long getEstimatedTime(Route route) {
        double distanceKm = calculateDistance(route);
        double averageSpeedKmh = 40;
        double hours = distanceKm / averageSpeedKmh;
        return Math.round(hours * 60);
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

    @Transactional
    public RideCancelRequestDTO cancelRide(
            Long rideId,
            String cancellationReason,
            String cancelledBy) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        ride.setStatus(RideStatus.CANCELED);
        ride.setCancellationReason(cancellationReason);
        ride.setCancelledBy(cancelledBy);

        rideRepository.save(ride);

        RideCancelRequestDTO dto = new RideCancelRequestDTO();
        dto.setRideId(ride.getId());          // ✅ OVDE
        dto.setRideStatus(RideStatus.CANCELED);
        dto.setCancellationReason(cancellationReason);
        dto.setCancelledBy(cancelledBy);

        return dto;
    }



}
