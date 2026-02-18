package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ResetPasswordDTO {
	@NotBlank(message = "Token is required")
	private String token;

	@NotBlank(message = "New password is required")
	@Size(min = 3, max = 100, message = "Password must be between 3 and 100 characters")
	private String newPassword;

	@NotBlank(message = "Confirm new password is required")
	@Size(min = 3, max = 100, message = "Password must be between 3 and 100 characters")
	private String confirmNewPassword;

    public ResetPasswordDTO() {}

    public ResetPasswordDTO(String token, String newPassword, String confirmNewPassword) {
        super();
        this.token = token;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
    
}