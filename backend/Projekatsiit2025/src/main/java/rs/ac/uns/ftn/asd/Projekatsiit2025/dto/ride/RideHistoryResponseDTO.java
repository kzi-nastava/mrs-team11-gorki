package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

public class RideHistoryResponseDTO {
	@NotNull(message = "Route is required")
	@Valid
	private Route route;

	@NotNull(message = "Starting time is required")
	@PastOrPresent(message = "Starting time cannot be in the future")
	private LocalDateTime startingTime;

	@PastOrPresent(message = "Ending time cannot be in the future")
	private LocalDateTime endingTime;

	@NotBlank(message = "Starting address is required")
	@Size(max = 500, message = "Starting address too long")
	private String startingAddress;

	@NotBlank(message = "Ending address is required")
	@Size(max = 500, message = "Ending address too long")
	private String endingAddress;

	@NotNull(message = "Status is required")
	private RideStatus status;

	@Size(max = 100, message = "CancelledBy value too long")
	private String cancelledBy;

	@NotNull(message = "Price is required")
	@PositiveOrZero(message = "Price must be >= 0")
	private Double price;

	@NotNull(message = "Panic flag is required")
	private Boolean panicActivated;


    public RideHistoryResponseDTO() {
    }
    
    public RideHistoryResponseDTO(Route route, LocalDateTime startingTime, LocalDateTime endingTime, String startingAddress,
			String endingAddress, RideStatus status, String cancelledBy, double price, boolean panicActivated) {
		super();
		this.route = route;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.startingAddress = startingAddress;
		this.endingAddress = endingAddress;
		this.status = status;
		this.cancelledBy = cancelledBy;
		this.price = price;
		this.panicActivated = panicActivated;
	}
    
	public Route getRoute() {
        return route;
    }
	
    public void setRoute(Route route) {
        this.route = route;
    }
    
    public LocalDateTime getStartingTime() {
        return startingTime;
    }
    
    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }
    
    public LocalDateTime getEndingTime() {
        return endingTime;
    }
    
    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }
    
    public String getStartingAddress() {
        return startingAddress;
    }
    
    public void setStartingAddress(String startingAddress) {
        this.startingAddress = startingAddress;
    }
    
    public String getEndingAddress() {
        return endingAddress;
    }
    
    public void setEndingAddress(String endingAddress) {
        this.endingAddress = endingAddress;
    }
    
    public RideStatus getStatus() {
		return status;
	}

	public void setStatus(RideStatus status) {
		this.status = status;
	}

	public boolean isPanicActivated() {
		return panicActivated;
	}

	public void setPanicActivated(boolean panicActivated) {
		this.panicActivated = panicActivated;
	}

	public String getCancelledBy() {
        return cancelledBy;
    }
    
    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public boolean isPanicUsed() {
        return panicActivated;
    }
    
    public void setPanicUsed(boolean panicActivated) {
        this.panicActivated = panicActivated;
    }
    
}