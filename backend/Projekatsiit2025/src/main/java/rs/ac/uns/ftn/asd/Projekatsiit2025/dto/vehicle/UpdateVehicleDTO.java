package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;

public class UpdateVehicleDTO {
	@Size(max = 100, message = "Model too long")
	private String model;

	private VehicleType type;

	@Size(max = 20, message = "Plate number too long")
	private String plateNumber;

	@Min(value = 1, message = "Vehicle must have at least 1 seat")
	@Max(value = 20, message = "Too many seats")
	private Integer seats;

	private Boolean babyTransport;

	private Boolean petFriendly;

	public UpdateVehicleDTO() {
		super();
	}
	public UpdateVehicleDTO(String model, VehicleType type, String plateNumber, int seats, Boolean babyTransport,
			Boolean petFriendly) {
		super();
		this.model = model;
		this.type = type;
		this.plateNumber = plateNumber;
		this.seats = seats;
		this.babyTransport = babyTransport;
		this.petFriendly = petFriendly;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public VehicleType getType() {
		return type;
	}
	public void setType(VehicleType type) {
		this.type = type;
	}
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public int getSeats() {
		return seats;
	}
	public void setSeats(int seats) {
		this.seats = seats;
	}
	public Boolean getBabyTransport() {
		return babyTransport;
	}
	public void setBabyTransport(Boolean babyTransport) {
		this.babyTransport = babyTransport;
	}
	public Boolean getPetFriendly() {
		return petFriendly;
	}
	public void setPetFriendly(Boolean petFriendly) {
		this.petFriendly = petFriendly;
	}
	
}
