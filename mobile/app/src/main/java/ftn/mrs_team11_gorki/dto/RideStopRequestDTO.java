package ftn.mrs_team11_gorki.dto;

public class RideStopRequestDTO {
    private double latitude;
    private double longitude;
    private String address;

    public RideStopRequestDTO(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAddress() { return address; }
}