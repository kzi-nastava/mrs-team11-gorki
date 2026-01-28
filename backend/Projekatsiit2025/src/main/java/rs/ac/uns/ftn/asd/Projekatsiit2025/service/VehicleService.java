package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.VehicleRepository;

@Service
public class VehicleService {
	private VehicleRepository vehicleRepository;
	
	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}
	
	public GetVehicleDTO getById(Long id) {
		Vehicle vehicle = vehicleRepository.findById(id).get();
		return mapToGetVehicleDTO(vehicle);
	}
	
	@Transactional
	public UpdatedVehicleDTO updateVehicle(Long id, UpdateVehicleDTO dto) {
		Vehicle vehicle = vehicleRepository.findById(id).get();
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
	
}
