package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger;

import jakarta.validation.constraints.NotNull;

public class PassengerInRideDTO {
	    
		@NotNull(message = "Email is required")
	 	private String email;
		@NotNull(message = "First name is required")
	 	private String firstName;
		@NotNull(message = "Last name is required")
	 	private String lastName;
		@NotNull(message = "Phone number is required")
	 	
		private String phoneNumber;
		public PassengerInRideDTO(String email, String firstName, String lastName, String phoneNumber) {
			super();
			this.email = email;
			this.firstName = firstName;
			this.lastName = lastName;
			this.phoneNumber = phoneNumber;
		}
		public PassengerInRideDTO() {
			super();
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	 	
	 	
}
