package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.EmailAlreadyExistsException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;

@Service
public class DriverService {
	private final DriverRepository driverRepository;
	private final UserRepository userRepository;
	
	public DriverService(DriverRepository driverRepository, UserRepository userRepository) {
		this.driverRepository = driverRepository;
		this.userRepository = userRepository;
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
        driver.setRole(UserRole.DRIVER);
        driver.setActive(false);
        driver.setStatus(DriverStatus.ACTIVE);
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(dto.getVehicle().getModel());
        vehicle.setType(dto.getVehicle().getType());
        vehicle.setPlateNumber(dto.getVehicle().getPlateNumber());
        vehicle.setSeats(dto.getVehicle().getSeats());
        vehicle.setBabyTransport(dto.getVehicle().getBabyTransport());
        vehicle.setPetFriendly(dto.getVehicle().getPetFriendly());
        driver.setVehicle(vehicle);
        Driver saved = driverRepository.save(driver);
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
