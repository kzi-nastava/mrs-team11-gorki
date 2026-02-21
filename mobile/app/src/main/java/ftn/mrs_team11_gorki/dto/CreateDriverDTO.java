package ftn.mrs_team11_gorki.dto;

public class CreateDriverDTO {
    private CreateUserDTO user;
    private CreateVehicleDTO vehicle;

    public CreateDriverDTO() {}

    public CreateDriverDTO(CreateUserDTO user, CreateVehicleDTO vehicle) {
        this.user = user;
        this.vehicle = vehicle;
    }

    public CreateUserDTO getUser() { return user; }
    public void setUser(CreateUserDTO user) { this.user = user; }

    public CreateVehicleDTO getVehicle() { return vehicle; }
    public void setVehicle(CreateVehicleDTO vehicle) { this.vehicle = vehicle; }
}
