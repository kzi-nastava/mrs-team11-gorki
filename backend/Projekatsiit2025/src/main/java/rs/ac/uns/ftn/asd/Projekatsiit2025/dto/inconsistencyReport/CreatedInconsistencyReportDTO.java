package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreatedInconsistencyReportDTO {
	
	    @NotNull(message = "Id is required")
	    @Positive(message = "Id must be positive")
	    private Long inconsistencyReportId;
	    
	    @NotNull(message = "Description is required")
	    private String description;
	    
	    @NotNull(message = "Time is required")
	    private LocalDateTime timeStamp;
	    
		public CreatedInconsistencyReportDTO() {
			super();
		}
		public Long getInconsistencyReportId() {
			return inconsistencyReportId;
		}
		public void setInconsistencyReportId(Long inconsistencyReportId) {
			this.inconsistencyReportId = inconsistencyReportId;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public LocalDateTime getTimeStamp() {
			return timeStamp;
		}
		public void setTimeStamp(LocalDateTime timeStamp) {
			this.timeStamp = timeStamp;
		}

}
