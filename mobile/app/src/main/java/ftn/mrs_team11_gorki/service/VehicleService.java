package ftn.mrs_team11_gorki.service;

import java.util.List;

import ftn.mrs_team11_gorki.dto.GetVehicleHomeDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface VehicleService {

    @GET("api/vehicles")
    Call<List<GetVehicleHomeDTO>> getHomeVehicles();
}
