package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;

public class RideStopResponseDTO {

	@NotNull(message = "Stop address is required")
	@Valid
	private Location stopAddress;

	@NotNull(message = "Ending time is required")
	@PastOrPresent(message = "Ending time cannot be in the future")
	private LocalDateTime endingTime;

	@NotNull(message = "Price is required")
	@PositiveOrZero(message = "Price must be >= 0")
	private Double price;

    
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