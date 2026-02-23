package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport;

public class CreateInconsistencyReportDTO {
	    
		//@NotNull(message = "Description is required")
		private String description;
		
		public CreateInconsistencyReportDTO() {
			super();
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
}
