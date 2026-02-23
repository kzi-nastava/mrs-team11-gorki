package ftn.mrs_team11_gorki.service;

import java.util.List;

import ftn.mrs_team11_gorki.dto.CreateRideDTO;
import ftn.mrs_team11_gorki.dto.CreatedRideDTO;
import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import ftn.mrs_team11_gorki.dto.GetRideDTO;
import ftn.mrs_team11_gorki.dto.RideCancelRequestDTO;
import ftn.mrs_team11_gorki.dto.RideCancelResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RideService {

    @GET("api/rides/{userId}/scheduled-rides")
    Call<List<DriverRideHistoryDTO>> getScheduledRides(
            @Header("Authorization") String bearerToken,
            @Path("userId") Long userId,
            @Query("from") String from,
            @Query("to") String to
    );

    @POST("api/rides/{id}/cancel")
    Call<RideCancelResponseDTO> cancelRide(
            @Header("Authorization") String bearer,
            @Path("id") Long rideId,
            @Body RideCancelRequestDTO body
    );

    @POST("api/rides")
    Call<CreatedRideDTO> createRide(@Body CreateRideDTO dto);

    @GET("api/rides/{id}/next-ride")
    Call<GetRideDTO> getNextScheduledRide(@Path("id") Long id);

    @PUT("api/rides/{id}/start")
    Call<GetRideDTO> startRide(@Path("id") Long id);
}