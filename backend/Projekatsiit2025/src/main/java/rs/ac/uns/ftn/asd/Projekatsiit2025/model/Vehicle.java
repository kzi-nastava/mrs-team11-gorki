package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Embedded
	private Location currentLocation;
    
	private String model;
	@Enumerated(EnumType.STRING)
	
	private VehicleType type;
	private String plateNumber;
	private int seats;
	private Boolean babyTransport;
	private Boolean petFriendly;
	
	public Vehicle() {
		
	}
	public Vehicle(Long id,Location currentLocation, String model, VehicleType type,
			String plateNumber, int seats, Boolean babyTransport, Boolean petFriendly) {
		this.id = id;
		this.currentLocation=currentLocation;
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
	public Location getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
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