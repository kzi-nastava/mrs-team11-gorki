package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;

import java.util.List;

import ftn.mrs_team11_gorki.dto.FinishRideDTO;
import ftn.mrs_team11_gorki.dto.FinishedRideDTO;
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
import retrofit2.http.POST;

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


    //End ride
    @GET("/api/drivers/{id}/ride/active")
    Call<DriverRideHistoryDTO> getActiveRideForEnd(@Path("id") long driverId);

    @PUT("/api/drivers/{driverId}/rides/finish")
    Call<FinishedRideDTO> finishRide(@Path("driverId") long driverId,
                                     @Body FinishRideDTO dto);

    // STOP RIDE (Driver)
    @POST("api/rides/{id}/stop")
    Call<ftn.mrs_team11_gorki.dto.RideStopResponseDTO> stopRide(
            @Path("id") long rideId,
            @Body ftn.mrs_team11_gorki.dto.RideStopRequestDTO dto
    );

    // PANIC
    @PUT("api/rides/{id}/panic")
    Call<Void> panicRide(@Path("id") long rideId);

}