package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

public class RideEstimateRequestDTO {
    private String startingAddress;
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