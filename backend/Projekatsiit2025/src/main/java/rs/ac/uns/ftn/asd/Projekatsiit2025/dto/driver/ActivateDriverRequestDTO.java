package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver;

import jakarta.validation.constraints.NotNull;

public class ActivateDriverRequestDTO {
	
	@NotNull(message = "Token is required")
	private String token;
	
	@NotNull(message = "Token is required")
	private String password;
	
	public ActivateDriverRequestDTO() {
		super();
	}
	public ActivateDriverRequestDTO(String token, String password) {
		super();
		this.token = token;
		this.password = password;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
