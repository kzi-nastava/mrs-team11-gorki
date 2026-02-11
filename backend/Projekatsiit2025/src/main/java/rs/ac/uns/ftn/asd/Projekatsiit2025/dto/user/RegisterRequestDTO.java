package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

public class RegisterRequestDTO {
    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String address;
    private int phoneNumber;
    private String profileImage;
    
	public RegisterRequestDTO() {
		super();
	}
	
	public RegisterRequestDTO(String email, String password, String confirmPassword, String firstName, String lastName,
			String address, int phoneNumber, String profileImage) {
		super();
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.profileImage = profileImage;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
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
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}
	
	
}