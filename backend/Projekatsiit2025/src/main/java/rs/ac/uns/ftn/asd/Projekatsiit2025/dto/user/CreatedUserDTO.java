package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;

public class CreatedUserDTO {
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private int phoneNumber;
	private String address;
	private String profileImage;
	private Boolean active=false;
	private UserRole role;
	
	public CreatedUserDTO() {
		super();
	}
	
	public CreatedUserDTO(Long id, String email, String firstName, String lastName, int phoneNumber, String address,
			String profileImage, Boolean active, UserRole role) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.profileImage = profileImage;
		this.active = active;
		this.role = role;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public int getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}
	
}
