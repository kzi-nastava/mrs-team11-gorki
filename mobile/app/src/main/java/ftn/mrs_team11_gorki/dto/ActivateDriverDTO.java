package ftn.mrs_team11_gorki.dto;

public class ActivateDriverDTO {
    private String token;
    private String password;

    public ActivateDriverDTO(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() { return token; }
    public String getPassword() { return password; }
}