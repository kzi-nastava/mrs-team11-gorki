package ftn.mrs_team11_gorki.dto;

public class FinishRideDTO {
    private Long rideId;
    private Boolean paid;

    public FinishRideDTO() {}

    public FinishRideDTO(Long rideId, Boolean paid) {
        this.rideId = rideId;
        this.paid = paid;
    }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public Boolean getPaid() { return paid; }
    public void setPaid(Boolean paid) { this.paid = paid; }
}
