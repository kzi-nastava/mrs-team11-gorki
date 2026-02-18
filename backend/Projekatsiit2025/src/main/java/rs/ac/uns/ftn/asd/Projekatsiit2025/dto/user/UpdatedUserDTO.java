package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdatedUserDTO {
	@Positive(message = "Id must be positive")
	private Long id;

	@NotBlank(message = "Email is required")
	@Size(max = 255, message = "Email too long")
	private String email;

	@NotBlank(message = "First name is required")
	@Size(max = 100, message = "First name too long")
	private String firstName;

	@NotBlank(message = "Last name is required")
	@Size(max = 100, message = "Last name too long")
	private String lastName;

	@NotNull(message = "Phone number is required")
	@Positive(message = "Phone number must be positive")
	private Integer phoneNumber;

	@Size(max = 500, message = "Address too long")
	private String address;

	@Size(max = 1000000, message = "Profile image too large")
	private String profileImage;

	
	public UpdatedUserDTO() {
		super();
	}
	public UpdatedUserDTO(Long id, String email, String firstName, String lastName, int phoneNumber, String address,
			String profileImage) {
		super();
		this.id = id;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.profileImage = profileImage;
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
	
}
