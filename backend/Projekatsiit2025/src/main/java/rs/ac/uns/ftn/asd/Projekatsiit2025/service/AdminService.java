package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverInfoDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.GetPassengerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.AdminRideMonitorDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.GetRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.UserOptionDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.AdminRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;

@Service
public class AdminService {
	
	 //@Autowired
	 //private final AdminRepository adminRepository;
	 @Autowired
	 private final RideRepository rideRepository;
	 @Autowired
	 private final DriverRepository driverRepository;
	 @Autowired
	 private final UserRepository userRepository;
	
	 public AdminService(AdminRepository adminRepository, RideRepository rideRepository,
		 	DriverRepository driverRepository, UserRepository userRepository) {
		 //this.adminRepository = adminRepository;
		 this.rideRepository = rideRepository;
		 this.driverRepository = driverRepository;
		 this.userRepository = userRepository;
	 } 	
	
	 public GetDriverInfoDTO searchDrivers(String q) {

		 Driver driver = driverRepository.searchByName(q, PageRequest.of(0, 1))
				    .stream()
				    .findFirst()
				    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

		    return mapToGetDriverDTO(driver);
	 }
	 
	 @Transactional(readOnly = true)
	 public AdminRideMonitorDTO getActiveRideForDriver(Long driverId) {
		  Ride ride = rideRepository.findActiveRideForDriver(driverId)
		      .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,""
		        		+ "Driver has no active rise."));

		  AdminRideMonitorDTO dto = new AdminRideMonitorDTO();

		  dto.setRide(mapRideToDTO(ride));

		  dto.setDriver(mapToGetDriverDTO(ride.getDriver()));

		  dto.setCurrentLocation(extractCurrentLocation(ride.getDriver()));

		  return dto;
	 }
	 
	 private GetDriverInfoDTO mapToGetDriverDTO(Driver driver) {

		    GetDriverInfoDTO dto = new GetDriverInfoDTO();

		    GetUserDTO userDTO = new GetUserDTO();
		    userDTO.setId(driver.getId());
		    userDTO.setEmail(driver.getEmail());
		    userDTO.setFirstName(driver.getFirstName());
		    userDTO.setLastName(driver.getLastName());
		    userDTO.setPhoneNumber(driver.getPhoneNumber());
		    userDTO.setAddress(driver.getAddress());
		    userDTO.setRole(driver.getRole());
		    userDTO.setActive(driver.getActive());
		    userDTO.setProfileImage(driver.getProfileImage());
		    dto.setUser(userDTO);

		    dto.setActivityLast24h(driver.getActivityLast24h());

		    Vehicle v = driver.getVehicle();
		    if (v == null) {
		        dto.setVehicle(null);
		        return dto;
		    }

		    LocationDTO locationDTO = null;
		    Location loc = v.getCurrentLocation();
		    if (loc != null) {
		        locationDTO = new LocationDTO(loc.getLatitude(), loc.getLongitude(), loc.getAddress());
		    }

		    GetVehicleDTO vehicleDTO = new GetVehicleDTO(
		        v.getId(),
		        v.getModel(),
		        v.getType(),
		        v.getPlateNumber(),
		        v.getSeats(),
		        v.getBabyTransport(),
		        v.getPetFriendly(),
		        locationDTO
		    );

		    dto.setVehicle(vehicleDTO);
		    return dto;
		}

	 private GetRideDTO mapRideToDTO(Ride r) {
		    GetRideDTO dto = new GetRideDTO();
		    dto.setId(r.getId());
		    dto.setStartingTime(r.getStartingTime());
		    dto.setEndingTime(r.getEndingTime());
		    dto.setStatus(r.getStatus());
		    dto.setPrice(r.getPrice());
		    
		    dto.setCreator(null);

		    // linked passengers (null-safe)
		    var passengers = (r.getLinkedPassengers() == null) ? List.<rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger>of()
		                                                       : r.getLinkedPassengers();

		    dto.setLinkedPassengers(
		        passengers.stream()
		            .map(p -> {
		                GetUserDTO userDTO = new GetUserDTO(
		                    p.getId(),
		                    p.getEmail(),
		                    p.getFirstName(),
		                    p.getLastName(),
		                    p.getPhoneNumber(),
		                    p.getAddress(),
		                    p.getProfileImage(),
		                    p.getActive(),
		                    p.getBlocked(),
		                    p.getBlockReason(),
		                    p.getRole()
		                );
		                
		                var fav = (p.getFavouriteRoutes() == null) ? List.<Route>of() : p.getFavouriteRoutes();

		                List<GetRouteDTO> favouriteRoutes = fav.stream()
		                    .map(fr -> new GetRouteDTO(
		                        fr.getId(),
		                        (fr.getLocations() == null ? List.<LocationDTO>of()
		                          : fr.getLocations().stream()
		                              .map(loc -> new LocationDTO(loc.getLatitude(), loc.getLongitude(), loc.getAddress()))
		                              .toList()
		                        ),
		                        fr.getDistance(),
		                        fr.getEstimatedTime()
		                    ))
		                    .toList();

		                return new GetPassengerDTO(userDTO, favouriteRoutes);
		            })
		            .toList()
		    );

		    
		    for (GetPassengerDTO passenger : dto.getLinkedPassengers()) {
				if(passenger.getUser().getId()==r.getCreator().getId()) {
					dto.setCreator(passenger);
					break;
				}
			}
		    
		    
		    // route (null-safe)
		    Route route = r.getRoute();
		    if (route != null) {
		        List<LocationDTO> locs = (route.getLocations() == null) ? List.of()
		            : route.getLocations().stream()
		                .map(loc -> new LocationDTO(loc.getLatitude(), loc.getLongitude(), loc.getAddress()))
		                .toList();

		        GetRouteDTO routeDTO = new GetRouteDTO(
		            route.getId(),
		            locs,
		            route.getDistance(),
		            route.getEstimatedTime()
		        );
		        dto.setRoute(routeDTO);
		    } else {
		        dto.setRoute(null);
		    }

		    return dto;
		}

	 private LocationDTO extractCurrentLocation(Driver driver) {
		    if (driver == null) return null;

		    Vehicle v = driver.getVehicle();
		    if (v == null) return null;

		    Location loc = v.getCurrentLocation();
		    if (loc == null) return null;

		    return new LocationDTO(loc.getLatitude(), loc.getLongitude(), loc.getAddress());
		}

	@Transactional(readOnly = true)
	public List<UserOptionDTO> getAllUsers() {
		return userRepository.findAll().stream()
			.map(u -> new UserOptionDTO(
				u.getId(),
				u.getFirstName(),
				u.getLastName(),
				u.getEmail()
			))
			.toList();
	}
}