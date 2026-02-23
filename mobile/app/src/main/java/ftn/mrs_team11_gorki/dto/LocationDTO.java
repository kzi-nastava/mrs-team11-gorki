package ftn.mrs_team11_gorki.dto;

public class LocationDTO {
    private double latitude;
    private double longitude;
    private String address;

    public LocationDTO() {
        super();
    }

    public LocationDTO(double latitude, double longitude, String address) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
