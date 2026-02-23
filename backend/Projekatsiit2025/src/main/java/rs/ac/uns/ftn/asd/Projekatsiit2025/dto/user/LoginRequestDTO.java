package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class LoginRequestDTO {
	
	@NotNull(message = "Email is required")
	@NotBlank(message = "Email must not be blank")
    private String email;
	
	@NotNull(message = "Email is required")
	@NotBlank(message = "Email must not be blank")
    private String password;
    
	public LoginRequestDTO() {
		super();
	}
	
	public LoginRequestDTO(String email, String password) {
		super();
		this.email = email;
		this.password = password;
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
	
}

