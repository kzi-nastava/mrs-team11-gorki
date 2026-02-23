package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

@Service
public class DriverAssignmentService {
	 private final DriverRepository driverRepository;
	 private final RideRepository rideRepository;

	 public DriverAssignmentService(DriverRepository driverRepository, RideRepository rideRepository) {
        this.driverRepository = driverRepository;
        this.rideRepository = rideRepository;
    }
	 
	public Driver selectDriver(Boolean babyTransport, Boolean petFriendly, VehicleType vehicleType, Route route, LocalDateTime scheduledTime) {
        List<Driver> eligible = driverRepository.findAll().stream()
            .filter(d -> d.getStatus() == DriverStatus.ACTIVE || d.getStatus() == DriverStatus.BUSY)
            .filter(this::isNotBlocked)
            .filter(this::hasLessThan8HoursLast24h)
            .filter(this::isNotBusyWithFutureRide)
            .filter(d -> d.getVehicle().getBabyTransport() == babyTransport)
            .filter(d -> d.getVehicle().getPetFriendly() == petFriendly)
            .filter(d -> d.getVehicle().getType() == vehicleType)
            .toList();

        if (eligible.isEmpty()) {
            return null;
        }

        List<Driver> freeDrivers = eligible.stream()
            .filter(d -> isFreeAt(d, scheduledTime, route))
            .toList();

        if (!freeDrivers.isEmpty()) {
            return findNearestDriver(freeDrivers, route.getLocations().get(0));
        }

        return findBestBusyDriver(eligible, route.getLocations().get(0));
    }
	
	private boolean hasLessThan8HoursLast24h(Driver driver) {
		if(driver.getActivityLast24h() > 8) {
			return false;
		}
		return true;
	}
	
	private boolean isNotBlocked(Driver driver) {
		return !driver.getBlocked();
	}
	
	private boolean isNotBusyWithFutureRide(Driver driver) {
		Ride activeRide = this.getActiveRide(driver);
		List<Ride> scheduledRides = rideRepository.findAllByDriverIdAndStatus(driver.getId(), RideStatus.ACCEPTED);
		boolean hasScheduledRideInFuture = false;
		for(Ride ride : scheduledRides) {
			if(ride.getScheduledTime().plusMinutes(ride.getRoute().getEstimatedTime().getHour() * 60 + ride.getRoute().getEstimatedTime().getMinute()).isAfter(LocalDateTime.now())) {
				hasScheduledRideInFuture = true;
				break;
			}
		}
		return activeRide == null || !hasScheduledRideInFuture;
	}
	
	private boolean isFreeAt(Driver driver, LocalDateTime scheduledTime, Route route) {
	    List<Ride> scheduledRides = rideRepository.findAllByDriverIdAndStatus(driver.getId(), RideStatus.ACCEPTED);
	    if (scheduledRides.isEmpty()) {
	        return true;
	    }
	    LocalDateTime approximateEndingTime = scheduledTime.plusMinutes(route.getEstimatedTime().getHour() * 60 + route.getEstimatedTime().getMinute());
	    for(Ride ride : scheduledRides) {
	    	LocalDateTime acceptedRideApproximatedEndingTime = ride.getScheduledTime().plusMinutes(ride.getRoute().getEstimatedTime().getHour() * 60 + ride.getRoute().getEstimatedTime().getMinute()); 
	    	if(scheduledTime.isBefore(acceptedRideApproximatedEndingTime) && ride.getScheduledTime().isBefore(approximateEndingTime)) {
	    		return false;
	    	}
	    }
	    return true;
	}
	
	private Driver findNearestDriver(List<Driver> drivers, Location start) {

	    return drivers.stream()
	        .min(Comparator.comparingDouble(
	            d -> distance(
	                d.getVehicle().getCurrentLocation().getLatitude(),
	                d.getVehicle().getCurrentLocation().getLongitude(),
	                start.getLatitude(),
	                start.getLongitude()
	            )
	        ))
	        .orElse(null);
	}
	
	private double distance(
	        double lat1, double lon1,
	        double lat2, double lon2
	) {
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
	
	private Driver findBestBusyDriver(List<Driver> drivers, Location start) {
	    return drivers.stream()
	    	.filter(driver -> getActiveRide(driver) != null)
	        .map(driver -> Map.entry(driver, getActiveRide(driver)))
	        .filter(entry -> finishesWithin10Minutes(entry.getValue()))
	        .min(Comparator.comparingDouble(entry -> {

	            Ride ride = entry.getValue();
	            Location rideEnd = ride.getRoute().getLocations().get(ride.getRoute().getLocations().size() - 1);

	            return distance(
	                rideEnd.getLatitude(),
	                rideEnd.getLongitude(),
	                start.getLatitude(),
	                start.getLongitude()
	            );
	        }))
	        .map(Map.Entry::getKey)
	        .orElse(null);
	}
	
	private Ride getActiveRide(Driver driver) {
	    return rideRepository
	        .findByDriverIdAndStatus(driver.getId(), RideStatus.STARTED)
	        .orElse(null);
	}
	
	private boolean finishesWithin10Minutes(Ride ride) {
	    return Duration.between(LocalDateTime.now(), ride.getEndingTime()).toMinutes() <= 10;
	}

}
