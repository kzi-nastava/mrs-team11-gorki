package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;

public class GetVehicleDTO {
	private Long id;
	private String model;
	private VehicleType type;
	private String plateNumber;
	private int seats;
	private Boolean babyTransport;
	private Boolean petFriendly;
	
	public GetVehicleDTO() {
		super();
	}
	
	public GetVehicleDTO(Long id, String model, VehicleType type, String plateNumber, int seats, Boolean babyTransport,
			Boolean petFriendly) {
		super();
		this.id = id;
		this.model = model;
		this.type = type;
		this.plateNumber = plateNumber;
		this.seats = seats;
		this.babyTransport = babyTransport;
		this.petFriendly = petFriendly;
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
	
}
