package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;

public class LoginResponseDTO {
	@Positive(message = "Id must be positive")
	private Long id;

	@NotNull(message = "User role is required")
	private UserRole role;

	@NotNull(message = "Active flag is required")
	private Boolean active;

	@NotNull(message = "Blocked flag is required")
	private Boolean blocked;

	@NotBlank(message = "Message is required")
	@Size(max = 255, message = "Message too long")
	private String message;

	@NotBlank(message = "Token is required")
	private String token;

    
    
	public LoginResponseDTO() {
		super();
	}
	
	public LoginResponseDTO(Long id, UserRole role, boolean active, boolean blocked, String message, String token) {
		super();
		this.id = id;
		this.role = role;
		this.active = active;
		this.blocked = blocked;
		this.message = message;
		this.token = token;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public UserRole getRole() {
		return role;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}