package ftn.mrs_team11_gorki.dto;

public class UpdatePasswordDTO {
    private String currentPassword;
    private String newPassword;
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
