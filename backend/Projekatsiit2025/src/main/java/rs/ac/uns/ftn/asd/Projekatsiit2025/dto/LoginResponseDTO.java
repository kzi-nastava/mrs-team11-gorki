package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;

public class LoginResponseDTO {
    private Long id;
    private UserRole role;
    private boolean active;
    private boolean blocked;
    private String message;
    private String token;
    
    
	public LoginResponseDTO() {
		super();
	}
	
	public LoginResponseDTO(Long id, UserRole role, boolean active, boolean blocked, String message) {
		super();
		this.id = id;
		this.role = role;
		this.active = active;
		this.blocked = blocked;
		this.message = message;
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