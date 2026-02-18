package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdatePasswordDTO {
	@NotBlank(message = "Current password is required")
	private String currentPassword;

	@NotBlank(message = "New password is required")
	@Size(min = 3, max = 100, message = "Password must be between 3 and 100 characters")
	private String newPassword;

	@NotBlank(message = "Confirm new password is required")
	private String newPasswordConfirmed;

	public UpdatePasswordDTO() {
		super();
	}
	public UpdatePasswordDTO(String currentPassword, String newPassword, String newPasswordConfirmed) {
		super();
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.newPasswordConfirmed = newPasswordConfirmed;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getNewPasswordConfirmed() {
		return newPasswordConfirmed;
	}
	public void setNewPasswordConfirmed(String newPasswordConfirmed) {
		this.newPasswordConfirmed = newPasswordConfirmed;
	}
	
}
