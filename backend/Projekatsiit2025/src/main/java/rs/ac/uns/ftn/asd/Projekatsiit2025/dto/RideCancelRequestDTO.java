package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

public class RideCancelRequestDTO {
	private String cancellationReason;
	private String cancelledBy;

	public RideCancelRequestDTO() {
		super();
	}
	
	public RideCancelRequestDTO(String cancellationReason, String cancelledBy) {
		super();
		this.cancellationReason = cancellationReason;
		this.cancelledBy = cancelledBy;
	}
	
	public String getCancellationReason() {
		return cancellationReason;
	}
	
	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	public String getCancelledBy() {
		return cancelledBy;
	}

	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	
}