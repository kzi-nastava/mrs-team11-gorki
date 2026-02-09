package ftn.mrs_team11_gorki.dto;

public class UpdateVehicleDTO {
    private String model;
    private String type;
    private String plateNumber;
    private int seats;
    private Boolean babyTransport;
    private Boolean petFriendly;
    public UpdateVehicleDTO() {
        super();
    }
    public UpdateVehicleDTO(String model, String type, String plateNumber, int seats, Boolean babyTransport,
                            Boolean petFriendly) {
        super();
        this.model = model;
        this.type = type;
        this.plateNumber = plateNumber;
        this.seats = seats;
        this.babyTransport = babyTransport;
        this.petFriendly = petFriendly;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPlateNumber() {
        return plateNumber;
    }
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }
    public int getSeats() {
        return seats;
    }
    public void setSeats(int seats) {
        this.seats = seats;
    }
    public Boolean getBabyTransport() {
        return babyTransport;
    }
    public void setBabyTransport(Boolean babyTransport) {
        this.babyTransport = babyTransport;
    }
    public Boolean getPetFriendly() {
        return petFriendly;
    }
    public void setPetFriendly(Boolean petFriendly) {
        this.petFriendly = petFriendly;
    }
}
