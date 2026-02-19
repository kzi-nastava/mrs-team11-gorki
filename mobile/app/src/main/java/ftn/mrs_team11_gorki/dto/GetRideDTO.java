package ftn.mrs_team11_gorki.dto;
import java.util.List;

public class GetRideDTO {
    public Long id;
    public String status;
    public Double price;

    public String scheduledTime;
    public String startingTime;
    public String endingTime;

    public Boolean panicActivated;
    public String cancellationReason;

    public GetDriverDTO driver;
    public GetRouteDTO route;

    public List<GetPassengerDTO> linkedPassengers;
    public GetPassengerDTO creator;
}

