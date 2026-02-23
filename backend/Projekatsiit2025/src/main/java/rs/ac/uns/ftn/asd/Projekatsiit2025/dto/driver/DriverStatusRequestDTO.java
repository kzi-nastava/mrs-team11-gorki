package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver;

import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;

public class DriverStatusRequestDTO {

	@NotNull(message = "Status is required")
    private DriverStatus status;

    public DriverStatusRequestDTO() {
    }

	public DriverStatusRequestDTO(DriverStatus status) {
		super();
		this.status = status;
	}

	public DriverStatus getStatus() {
		return status;
	}

	public void setStatus(DriverStatus status) {
		this.status = status;
	}

}