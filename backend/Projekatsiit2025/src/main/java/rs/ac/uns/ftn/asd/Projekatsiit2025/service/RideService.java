package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.GetPassengerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.CreateRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.CreatedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.GetRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideCancelRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideCancelResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideStopResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.ScheduledRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.UserRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.CreatedRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.NoEligibleDriverException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.LinkedPassengerNotFoundException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RatingRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto.PanicEventDTO;

@Service
public class RideService {
    private final RideRepository rideRepository;
    private final DriverAssignmentService driverAssignmentService;
    private final PriceConfigService priceConfigService;
    private final PassengerRepository passengerRepository;
	@Autowired
	private final RatingRepository ratingRepository;
	@Autowired
	private final NotificationService notificationService;
    @Autowired
    private PanicWsService panicWsService;
    @Autowired EmailService emailService;

    public RideService(RideRepository rideRepository, DriverAssignmentService driverAssignmentService, 
    		PriceConfigService priceConfigService, PassengerRepository passengerRepository,
    		RatingRepository ratingRepository,NotificationService notificationService,
    		EmailService emailService) {
        this.rideRepository = rideRepository;
        this.driverAssignmentService = driverAssignmentService;
        this.priceConfigService = priceConfigService;
        this.passengerRepository = passengerRepository;
        this.ratingRepository=ratingRepository;
        this.notificationService=notificationService;
        this.emailService=emailService;
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
        		Passenger passenger = passengerRepository.findByEmail(email).orElseThrow(() -> new LinkedPassengerNotFoundException("Passengers with email " + email + " is not found."));
        		linkedPassengers.add(passenger);
        	}
    	}
    	ride.setLinkedPassengers(linkedPassengers);
    	Passenger creator = passengerRepository.findById(dto.getCreatorId()).get();
    	ride.setCreator(creator);
    	ride.setDriver(driverAssignmentService.selectDriver(dto.getBabyTransport(), dto.getPetFriendly(), VehicleType.valueOf(dto.getVehicleType()), route, dto.getScheduledTime()));
    	if(ride.getDriver() == null) {
        	//Notifikacija kreatoru voznje
        	String content3 = "Ride ordering failed, no eligible drivers.";
        	notificationService.createAndSend(ride.getCreator().getEmail(),ride.getId(), "RIDE_FAILED", content3);
    		throw new NoEligibleDriverException("There are no eligible drivers currently.");
    	}
    	ride.setStatus(RideStatus.ACCEPTED);
    	ride.setPriceConfig(priceConfigService.getCurrentConfig());
    	ride.setPrice(priceConfigService.calculatePrice(VehicleType.valueOf(dto.getVehicleType()), route.getDistance()));
    	rideRepository.save(ride);
    	
    	//Notifikacije ulinkovanim putnicima
    	String link = "http://localhost:4200/ride-in-progress";
    	String content = "You were added to a ride and the driver has been found. Track the ride: " + link;

    	for (Passenger p : ride.getLinkedPassengers()) {
    	    notificationService.createAndSend(p.getEmail(),ride.getId(), "RIDE_ACCEPTED", content);
    	    emailService.sendRideAcceptedMail("mrs.team11.gorki@gmail.com", link); 
    	    System.out.println("MAIL STATUS to " + p.getEmail());
    	}
    	
    	//Notifikacija kreatoru voznje
    	String content2 = "You ride is successfully created.";
    	notificationService.createAndSend(ride.getCreator().getEmail(),ride.getId(), "RIDE_CREATED", content2); 
    	
    	
    	//Notifikacija driveru
    	String content3 = "You have new ride.";
    	notificationService.createAndSend(ride.getDriver().getEmail(),ride.getId(), "NEW_RIDE", content3); 
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
    	ride.setPaid(false);
    	rideRepository.save(ride);
    	return mapToGetRideDTO(ride);
    }
    
    @Transactional(readOnly = true)
    public GetRideDTO getNextScheduledRide(Long id) {
    	Ride ride = this.rideRepository.findFirstByDriverIdAndStatusOrderByScheduledTimeAsc(id, RideStatus.ACCEPTED).orElse(null);
    	return ride == null ? null : mapToGetRideDTO(ride);
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
    	dto.setRoute(mapToGetRouteDTO(ride.getRoute()));
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

        return LocalDateTime.of(1, 1, 1, (int) hh, (int) mm);
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

        Ride ride = rideRepository.findByIdAndDriver_Id(dto.getRideId(), driverId);
        if (ride == null) {
            throw new RuntimeException("Voznja ne postoji ili nije dodeljena ovom vozacu");
        }

        ride.setStatus(RideStatus.FINISHED);
        ride.setEndingTime(LocalDateTime.now());

        if (Boolean.TRUE.equals(dto.getPaid())) {
            ride.setPaid(true);
        }
        rideRepository.save(ride);

        System.out.println("SENDING RATING_AVAILABLE to " + ride.getCreator().getEmail()
                + " rideId=" + ride.getId());

        //Moze da se oceni
        notificationService.sendRatingAvailable(ride.getCreator().getEmail(), ride.getId());
        
        //===== Uspesno zavrsena voznja=====
        String link = "http://localhost:4200/rides-list-user";
        String content = "Ride finished successfully. You can view details here: " + link;

        emailService.sendRideFinishedMail("mrs.team11.gorki@gmail.com", link);
        notificationService.createAndSend(ride.getCreator().getEmail(),ride.getId(), "RIDE_FINISHED", content);

        for (Passenger p : ride.getLinkedPassengers()) {
            if (p.getId().equals(ride.getCreator().getId())) continue;

            emailService.sendRideFinishedMail("mrs.team11.gorki@gmail.com", link);
            notificationService.createAndSend(p.getEmail(),ride.getId() ,"RIDE_FINISHED", content);
        }
        
       //===== Uspesno zavrsena voznja=====

        Driver driver = ride.getDriver();

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
    public RideCancelResponseDTO cancelRide(Long rideId, RideCancelRequestDTO request) {

        if (request.getCancellationReason() == null || request.getCancellationReason().isBlank()) {
            throw new RuntimeException("Cancellation reason is required");
        }
        if (request.getCancelledBy() == null || request.getCancelledBy().isBlank()) {
            throw new RuntimeException("cancelledBy is required (DRIVER/PASSENGER)");
        }
        if (request.getActorId() == null) {
            throw new RuntimeException("actorId is required");
        }

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.ACCEPTED && ride.getStatus() != RideStatus.REQUESTED){
            throw new RuntimeException("Ride cannot be cancelled now");
        }

        String by = request.getCancelledBy().trim().toUpperCase();

        if ("DRIVER".equals(by)) {

            if (ride.getStatus() != RideStatus.ACCEPTED) {
                throw new RuntimeException("Driver can cancel only ACCEPTED rides");
            }
            if (ride.getDriver() == null || !ride.getDriver().getId().equals(request.getActorId())) {
                throw new RuntimeException("This driver is not assigned to this ride");
            }

            ride.setStatus(RideStatus.CANCELED);
            ride.setCancellationReason(request.getCancellationReason().trim());
            ride.setCancelledBy("DRIVER");

            ride.getDriver().setStatus(DriverStatus.ACTIVE);

            rideRepository.save(ride);
        }

        else if ("PASSENGER".equals(by)) {

            if (ride.getCreator() == null || !ride.getCreator().getId().equals(request.getActorId())) {
            //    throw new RuntimeException("Only the ride creator can cancel the ride");
            }
            if (ride.getScheduledTime() == null) {
                throw new RuntimeException("Ride has no scheduled time");
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime deadline = ride.getScheduledTime().minusMinutes(10);

            if (now.isAfter(deadline)) {
            //    throw new RuntimeException("Too late to cancel (10 minutes before start rule)");
            }

            ride.setStatus(RideStatus.CANCELED);
            ride.setCancellationReason(request.getCancellationReason().trim());
            ride.setCancelledBy("PASSENGER");

            if (ride.getDriver() != null) {
                ride.getDriver().setStatus(DriverStatus.ACTIVE);
            }

            rideRepository.save(ride);
        }

        else {
            throw new RuntimeException("cancelledBy must be DRIVER or PASSENGER");
        }

        RideCancelResponseDTO response = new RideCancelResponseDTO();
        response.setRideId(ride.getId());
        response.setRideStatus(ride.getStatus());
        response.setCancellationReason(ride.getCancellationReason());
        response.setCancelledBy(ride.getCancelledBy());
        return response;
    }

    @Transactional
    public void activatePanic(Long rideId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() == RideStatus.FINISHED || ride.getStatus() == RideStatus.CANCELED) {
        //    throw new RuntimeException("Ride not active");
        }

        if (ride.getPanicActivated()) {
        //    throw new RuntimeException("Ride already has panic activated");
        }

        ride.setPanicActivated(true);
        rideRepository.save(ride);

        // ko je okinuo panic? (opciono)
        String role = null;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            role = auth.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .filter(a -> a.equals("ROLE_DRIVER") || a.equals("ROLE_PASSENGER"))
                    .findFirst()
                    .orElse(null);
            if (role != null) role = role.replace("ROLE_", ""); // DRIVER/PASSENGER
        }

        panicWsService.broadcast(new PanicEventDTO(
                ride.getId(),
                role,
                java.time.LocalDateTime.now()
        ));
    }

    @Transactional
    public RideStopResponseDTO stopRide(Long rideId, Location stopLocation) {

        Ride ride = rideRepository.findById(rideId)
            .orElseThrow(() -> new RuntimeException("Ride not found"));

        if (ride.getStatus() != RideStatus.STARTED) {
            throw new RuntimeException("Ride is not STARTED");
        }

        LocalDateTime start = ride.getStartingTime();
        LocalDateTime plannedEnd = ride.getEndingTime();
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);


        if (start == null || plannedEnd == null) {
            throw new RuntimeException("Ride missing start/planned end time");
        }

        long plannedMinutes = java.time.Duration.between(start, plannedEnd).toMinutes();
        long actualMinutes = java.time.Duration.between(start, end).toMinutes();

        if (plannedMinutes <= 0) plannedMinutes = 1;
        if (actualMinutes < 0) actualMinutes = 0;

        double ratio = (double) actualMinutes / (double) plannedMinutes;
        ratio = Math.max(0.0, Math.min(1.0, ratio));

        Double price = ride.getPrice();
        double originalPrice = price != null ? price : 0.0;
        double newPrice = originalPrice * ratio;
        
        newPrice = Math.min(newPrice, originalPrice*0.9);

        newPrice = Math.max(newPrice, 100.0);

        newPrice = Math.round(newPrice);

        ride.setPrice(newPrice);

        ride.setEndingTime(end);
        ride.setPrice(newPrice);
        ride.setStatus(RideStatus.FINISHED);

        Route route = ride.getRoute();
        if (route == null) throw new RuntimeException("Ride has no route");

        if (route.getLocations() == null) {
            route.setLocations(new ArrayList<>());
        }

        List<Location> locs = route.getLocations();
        if (locs.isEmpty()) {
            locs.add(stopLocation);              // fallback: nema lokacija
        } else {
            locs.set(locs.size() - 1, stopLocation);
        }

        rideRepository.save(ride);

        RideStopResponseDTO dto = new RideStopResponseDTO();
        dto.setStopAddress(stopLocation);
        dto.setEndingTime(end);
        dto.setPrice(newPrice);
        return dto;
    }

    @Transactional(readOnly = true)
    public Collection<UserRideHistoryDTO> getUserRideHistory(
            Long creatorId,
            LocalDate from,
            LocalDate to) {

        LocalDateTime fromDateTime = (from != null)
                ? from.atStartOfDay()
                : LocalDate.of(2000, 1, 1).atStartOfDay();  // SAFE MIN

        LocalDateTime toDateTime = (to != null)
                ? to.atTime(23, 59, 59)
                : LocalDate.of(2100, 1, 1).atStartOfDay();  // SAFE MAX

        List<Ride> rides = rideRepository.findByCreator_IdAndStatusAndStartingTimeBetween(
                creatorId,
                RideStatus.FINISHED,
                fromDateTime,
                toDateTime
        );

        return rides.stream().map(this::mapUserRideHistoryToDTO).filter(Objects::nonNull).toList();
    }

    private UserRideHistoryDTO mapUserRideHistoryToDTO(Ride ride) {
        UserRideHistoryDTO dto = new UserRideHistoryDTO();
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
        Rating rate = ratingRepository
        	    .findByRideIdAndPassengerId(
        	        ride.getId(),
        	        ride.getCreator().getId()
        	    )
        	    .orElse(null);
        if(rate==null) {
        	dto.setAverageRating(0);
        }
        else {
        	dto.setAverageRating((rate.getDriverRating()+rate.getVehicleRating())/2);
        }
        
        return dto;
    }

    public Collection<UserRideHistoryDTO> getAdminRideHistory(LocalDate from, LocalDate to) {

        var rides = rideRepository.findAllByOrderByStartingTimeDesc();

        if (from != null) {
        rides = rides.stream()
                .filter(r -> r.getStartingTime() != null)
                .filter(r -> !r.getStartingTime().toLocalDate().isBefore(from))
                .toList();
        }

        if (to != null) {
            rides = rides.stream()
                    .filter(r -> r.getStartingTime() != null)
                    .filter(r -> !r.getStartingTime().toLocalDate().isAfter(to))
                    .toList();
        }

        rides = rides.stream()
                .filter(r -> r.getStatus() == RideStatus.FINISHED)
                .toList();

        return rides.stream()
                .map(this::mapUserRideHistoryToDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Collection<ScheduledRideDTO> getScheduledRide(
            Long userId,
            LocalDate from,
            LocalDate to) {
        System.out.println("User id"+userId);
        LocalDateTime fromDateTime = (from != null)
                ? from.atTime(0, 0, 1)
                : LocalDate.of(2000, 1, 1).atStartOfDay();  // SAFE MIN

        LocalDateTime toDateTime = (to != null)
                ? to.atTime(23, 59, 59)
                : LocalDate.of(2100, 1, 1).atStartOfDay();  // SAFE MAX

        List<Ride> acceptedRides = rideRepository.findByCreator_IdAndStatusAndScheduledTimeBetween(
                userId,
                RideStatus.ACCEPTED,
                fromDateTime,
                toDateTime
        );
        List<Ride> rides = new ArrayList<>(acceptedRides);


        if(rides.isEmpty()){
            List<Ride> acceptedRidesDriver = rideRepository.findByDriver_IdAndStatusAndScheduledTimeBetween(
                userId,
                RideStatus.ACCEPTED,
                fromDateTime,
                toDateTime
            );
            rides = new ArrayList<>(acceptedRidesDriver);
        }

        return rides.stream().map(this::mapScheduledRideToDTO).toList();
    }

    private ScheduledRideDTO mapScheduledRideToDTO(Ride ride) {
        ScheduledRideDTO dto = new ScheduledRideDTO();
        dto.setRideId(ride.getId());
        dto.setStartingTime(ride.getStartingTime());
        dto.setPrice(ride.getPrice());
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

    @Transactional(readOnly = true)
    public DriverRideHistoryDTO getActiveRideForDriver(Long driverId) {

        Ride ride = rideRepository
                .findByDriver_IdAndStatus(driverId, RideStatus.STARTED);

        if (ride == null) {
            throw new RuntimeException("Driver nema aktivnu vožnju");
        }

        return mapDriverHistoryToDTO(ride);
    }

    public Collection<UserRideHistoryDTO> getAllPanicRides(LocalDate from, LocalDate to) {

        var rides = rideRepository.findAllByPanicActivatedTrue();

        if (from != null) {
            rides = rides.stream()
                    .filter(r -> !r.getStartingTime().toLocalDate().isBefore(from))
                    .collect(Collectors.toList());
        }
        if (to != null) {
            rides = rides.stream()
                    .filter(r -> !r.getStartingTime().toLocalDate().isAfter(to))
                    .collect(Collectors.toList());
        }

        return rides.stream()
                .map(this::mapUserRideHistoryToDTO)
                .collect(Collectors.toList());
    }

}