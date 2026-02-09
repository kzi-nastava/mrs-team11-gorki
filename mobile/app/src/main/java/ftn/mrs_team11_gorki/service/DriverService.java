package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;

import java.util.List;

import ftn.mrs_team11_gorki.dto.GetVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdateVehicleDTO;
import ftn.mrs_team11_gorki.dto.UpdatedVehicleDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DriverService {

    @GET("api/drivers/{driverId}/rides/history")
    Call<List<DriverRideHistoryDTO>> getDriverRideHistory(
            @Header("Authorization") String bearerToken,
            @Path("driverId") Long driverId,
            @Query("from") String from,   // null ili "YYYY-MM-DD"
            @Query("to") String to        // null ili "YYYY-MM-DD"
    );

    @GET("api/drivers/{id}/vehicle")
    Call<GetVehicleDTO> getVehicle(@Path("id") Long id);

    @PUT("api/drivers/{id}/vehicle")
    Call<UpdatedVehicleDTO> updateVehicle(@Path("id") Long id, @Body UpdateVehicleDTO dto);
}