package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;

public class RideStopResponseDTO {
	private Location stopAddress;
	private LocalDateTime endingTime;
	private double price;
    
	public RideStopResponseDTO() {
		super();
	}
	
	public RideStopResponseDTO(Location stopAddress, double price, LocalDateTime endingTime) {
		super();
		this.stopAddress = stopAddress;
		this.price = price;
		this.endingTime = endingTime;
	}
	
	public Location getStopAddress() {
		return stopAddress;
	}

	public void setStopAddress(Location stopAddress) {
		this.stopAddress = stopAddress;
	}

	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public LocalDateTime getEndingTime() {
		return endingTime;
	}
	
	public void setEndingTime(LocalDateTime endingTime) {
		this.endingTime = endingTime;
	}
		
}