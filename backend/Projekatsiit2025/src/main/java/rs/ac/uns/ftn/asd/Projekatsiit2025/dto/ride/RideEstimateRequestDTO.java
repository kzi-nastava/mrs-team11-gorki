package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RideEstimateRequestDTO {
	@NotBlank(message = "Starting address is required")
	@Size(max = 500, message = "Starting address too long")
	private String startingAddress;

	@NotBlank(message = "Ending address is required")
	@Size(max = 500, message = "Ending address too long")
	private String endingAddress;


	public RideEstimateRequestDTO() {
		super();
	}
	
	public RideEstimateRequestDTO(String startingAddress, String endingAddress) {
		super();
		this.startingAddress = startingAddress;
		this.endingAddress = endingAddress;
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
    
}