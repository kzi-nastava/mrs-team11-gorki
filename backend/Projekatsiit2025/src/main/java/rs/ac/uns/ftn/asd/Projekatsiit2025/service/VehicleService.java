package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle.UpdateVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle.UpdatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.DriverRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.VehicleRepository;

@Service
public class VehicleService {
	private final VehicleRepository vehicleRepository;
	private final DriverRepository driverRepository;
	
	public VehicleService(VehicleRepository vehicleRepository, DriverRepository driverRepository) {
		this.vehicleRepository = vehicleRepository;
		this.driverRepository = driverRepository;
	}
	
	public GetVehicleDTO getById(Long id) {
		Driver driver = driverRepository.findById(id).get();
		Vehicle vehicle = driver.getVehicle();
		return mapToGetVehicleDTO(vehicle);
	}
	
	@Transactional
	public UpdatedVehicleDTO updateVehicle(Long id, UpdateVehicleDTO dto) {
		Driver driver = driverRepository.findById(id).get();
		Vehicle vehicle = driver.getVehicle();
		vehicle.setModel(dto.getModel());
		vehicle.setBabyTransport(dto.getBabyTransport());
		vehicle.setPetFriendly(dto.getPetFriendly());
		vehicle.setPlateNumber(dto.getPlateNumber());
		vehicle.setSeats(dto.getSeats());
		vehicle.setType(dto.getType());
		vehicleRepository.save(vehicle);
		return mapToUpdatedVehicleDTO(vehicle);
	}
	
	private UpdatedVehicleDTO mapToUpdatedVehicleDTO(Vehicle vehicle) {
		UpdatedVehicleDTO dto = new UpdatedVehicleDTO();
		dto.setId(vehicle.getId());
		dto.setModel(vehicle.getModel());
		dto.setBabyTransport(vehicle.getBabyTransport());
		dto.setPetFriendly(vehicle.getPetFriendly());
		dto.setPlateNumber(vehicle.getPlateNumber());
		dto.setSeats(vehicle.getSeats());
		dto.setType(vehicle.getType());
		return dto;
	}
	
	private GetVehicleDTO mapToGetVehicleDTO(Vehicle vehicle){
		GetVehicleDTO dto = new GetVehicleDTO();
		dto.setId(vehicle.getId());
		dto.setModel(vehicle.getModel());
		dto.setBabyTransport(vehicle.getBabyTransport());
		dto.setPetFriendly(vehicle.getPetFriendly());
		dto.setPlateNumber(vehicle.getPlateNumber());
		dto.setSeats(vehicle.getSeats());
		dto.setType(vehicle.getType());
		return dto;
	}
	
	public List<GetVehicleDTO> getAllVehicles(){
		return vehicleRepository.findAll()
				.stream()
				.map(this::mapVehicleToDTO)
				.toList();
	}
	
	private GetVehicleDTO mapVehicleToDTO(Vehicle vehicle) {
		GetVehicleDTO dto = new GetVehicleDTO();
		dto.setId(vehicle.getId());
		dto.setModel(vehicle.getModel());
		dto.setType(vehicle.getType());
		dto.setPlateNumber(vehicle.getPlateNumber());
		dto.setBabyTransport(vehicle.getBabyTransport());
		dto.setPetFriendly(vehicle.getPetFriendly());
		dto.setSeats(vehicle.getSeats());
		
		LocationDTO location=new LocationDTO();
		location.setAddress(vehicle.getCurrentLocation().getAddress());
		location.setLatitude(vehicle.getCurrentLocation().getLatitude());
		location.setLongitude(vehicle.getCurrentLocation().getLongitude());
		
		dto.setCurrentLocation(location);
		
		return dto;
	}
}
