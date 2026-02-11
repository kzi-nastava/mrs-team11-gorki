package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;

public class DriverStatusRequestDTO {

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