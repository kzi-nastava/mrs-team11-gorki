package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport;

import java.time.LocalDateTime;

public class CreatedInconsistencyReportDTO {
	    private Long inconsistencyReportId;
	    private String description;
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
