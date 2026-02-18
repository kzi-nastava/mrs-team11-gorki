package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver;

public class ActivateDriverRequestDTO {
	private String token;
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
