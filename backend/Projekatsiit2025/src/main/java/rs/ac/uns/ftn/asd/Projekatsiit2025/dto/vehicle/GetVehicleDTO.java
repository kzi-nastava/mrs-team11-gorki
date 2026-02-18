package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;

public class GetVehicleDTO {
	@Positive(message = "Id must be positive")
	private Long id;

	@NotBlank(message = "Model is required")
	@Size(max = 100, message = "Model too long")
	private String model;

	@NotNull(message = "Vehicle type is required")
	private VehicleType type;

	@NotBlank(message = "Plate number is required")
	@Size(max = 20, message = "Plate number too long")
	private String plateNumber;

	@NotNull(message = "Seats are required")
	@Min(value = 1, message = "Vehicle must have at least 1 seat")
	@Max(value = 20, message = "Too many seats")
	private Integer seats;

	@NotNull(message = "Baby transport flag is required")
	private Boolean babyTransport;

	@NotNull(message = "Pet friendly flag is required")
	private Boolean petFriendly;

	@NotNull(message = "Current location is required")
	@Valid
	private LocationDTO currentLocation;
	
	public GetVehicleDTO() {
		super();
	}
	
	public GetVehicleDTO(Long id, String model, VehicleType type, String plateNumber, int seats, Boolean babyTransport,
			Boolean petFriendly, LocationDTO currentLocation) {
		super();
		this.id = id;
		this.model = model;
		this.type = type;
		this.plateNumber = plateNumber;
		this.seats = seats;
		this.babyTransport = babyTransport;
		this.petFriendly = petFriendly;
    this.currentLocation=currentLocation;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
  public LocationDTO getCurrentLocation() {
    return currentLocation;
  }
  public void setCurrentLocation(LocationDTO currentLocation) {
    this.currentLocation = currentLocation;
  }
}