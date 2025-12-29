package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;

public class PassengerInRideDTO {
	
	 	private String email;
	 	private String firstName;
	 	private String lastName;
	 	private String phoneNumber;
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
