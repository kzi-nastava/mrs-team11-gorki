package ftn.mrs_team11_gorki.dto;

public class GetVehicleDTO {
    private Long id;
    private String model;
    private String type;
    private String plateNumber;
    private int seats;
    private Boolean babyTransport;
    private Boolean petFriendly;
    private LocationDTO currentLocation;

    public GetVehicleDTO() {
        super();
    }

    public GetVehicleDTO(Long id, String model, String type, String plateNumber, int seats, Boolean babyTransport,
                         Boolean petFriendly, LocationDTO currentLocation) {
        super();
        this.id = id;
        this.model = model;
        this.type = type;
        this.plateNumber = plateNumber;
        this.seats = seats;
        this.babyTransport = babyTransport;
        this.petFriendly = petFriendly;
        this.currentLocation=currentLocation;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public LocationDTO getCurrentLocation() {
        return currentLocation;
    }
    public void setCurrentLocation(LocationDTO currentLocation) {
        this.currentLocation = currentLocation;
    }
}
