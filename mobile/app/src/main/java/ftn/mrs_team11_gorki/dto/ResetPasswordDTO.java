package ftn.mrs_team11_gorki.dto;

public class ResetPasswordDTO {
    private String token;
    private String newPassword;
    private String confirmNewPassword;

    public ResetPasswordDTO(String token, String newPassword, String confirmNewPassword) {
        this.token = token;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getToken() { return token; }
    public String getNewPassword() { return newPassword; }
    public String getConfirmNewPassword() { return confirmNewPassword; }
}
