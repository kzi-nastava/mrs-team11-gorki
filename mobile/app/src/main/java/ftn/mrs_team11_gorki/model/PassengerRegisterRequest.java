package ftn.mrs_team11_gorki.model;

public class PassengerRegisterRequest {

    private String email;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String address;
    private int phoneNumber;
    private String profileImage;

    public PassengerRegisterRequest(String email, String password, String confirmPassword,
                                    String firstName, String lastName, String address,
                                    int phoneNumber, String profileImage) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
    }
}