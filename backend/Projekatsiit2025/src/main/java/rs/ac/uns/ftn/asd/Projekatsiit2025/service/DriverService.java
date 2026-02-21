package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.CreateDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.CreatedDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.CreatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle.CreatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.EmailAlreadyExistsException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.security.jwt.ActivationTokenUtil;

@Service
public class DriverService {
	@Autowired private final DriverRepository driverRepository;
	@Autowired private final UserRepository userRepository;
	private final EmailService emailService;
	@Autowired private final ActivationTokenUtil util;
	
	public DriverService(DriverRepository driverRepository, UserRepository userRepository, EmailService emailService, ActivationTokenUtil util) {
		this.driverRepository = driverRepository;
		this.userRepository = userRepository;
		this.emailService = emailService;
		this.util = util;
	}
	
	@Transactional
	public CreatedDriverDTO createDriver(CreateDriverDTO dto) {
		if(userRepository.findByEmail(dto.getUser().getEmail()).isPresent()) {
			throw new EmailAlreadyExistsException("Email already exists.");
		}
		Driver driver = new Driver();
		driver.setEmail(dto.getUser().getEmail());
        driver.setFirstName(dto.getUser().getFirstName());
        driver.setLastName(dto.getUser().getLastName());
        driver.setPhoneNumber(dto.getUser().getPhoneNumber());
        driver.setAddress(dto.getUser().getAddress());
        driver.setProfileImage(dto.getUser().getProfileImage());
        driver.setRole(UserRole.DRIVER);
        driver.setActive(false);
        driver.setBlocked(false);
        driver.setStatus(DriverStatus.ACTIVE);
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(dto.getVehicle().getModel());
        vehicle.setType(dto.getVehicle().getType());
        vehicle.setPlateNumber(dto.getVehicle().getPlateNumber());
        vehicle.setSeats(dto.getVehicle().getSeats());
        vehicle.setBabyTransport(dto.getVehicle().getBabyTransport());
        vehicle.setPetFriendly(dto.getVehicle().getPetFriendly());
        double randomLat = 45.2450 + Math.random() * (45.2700 - 45.2450);
        double randomLon = 19.8150 + Math.random() * (19.8550 - 19.8150);
        Location location=new Location(randomLat,randomLon,dto.getUser().getAddress());
        vehicle.setCurrentLocation(location);
        driver.setVehicle(vehicle);
        Driver saved = driverRepository.save(driver);
        String activationToken = util.generateActivationToken(driver.getEmail());
        //emailService.sendActivationLinkToDriverMail(activationToken);
        emailService.sendDriverActivationMobileDeepLink(activationToken);
        return mapToCreatedDriverDTO(saved);
	}
	
	public GetDriverDTO getActivity(Long id) {
		Driver driver = driverRepository.findById(id).get();
		return mapToGetDriverDTO(driver);
	}
	
	private CreatedDriverDTO mapToCreatedDriverDTO(Driver driver) {
		CreatedDriverDTO dto = new CreatedDriverDTO();
		dto.setUser(new CreatedUserDTO(driver.getId(), driver.getEmail(), driver.getFirstName(), driver.getLastName(), driver.getPhoneNumber(), driver.getAddress(), driver.getProfileImage(), driver.getBlocked(), driver.getRole()));
		dto.setVehicle(new CreatedVehicleDTO(driver.getVehicle().getId(), driver.getVehicle().getModel(), driver.getVehicle().getType(), driver.getVehicle().getPlateNumber(), driver.getVehicle().getSeats(), driver.getVehicle().getBabyTransport(), driver.getVehicle().getPetFriendly()));
		dto.setStatus(driver.getStatus());
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
	
}
