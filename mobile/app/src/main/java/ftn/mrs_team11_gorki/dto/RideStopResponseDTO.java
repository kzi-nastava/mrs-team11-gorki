package ftn.mrs_team11_gorki.dto;

public class RideStopResponseDTO {
    private LocationDTO stopAddress;
    private String endingTime;
    private double price;

    public LocationDTO getStopAddress() { return stopAddress; }
    public String getEndingTime() { return endingTime; }
    public double getPrice() { return price; }
}