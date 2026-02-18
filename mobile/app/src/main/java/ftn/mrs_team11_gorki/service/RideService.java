package ftn.mrs_team11_gorki.service;

import java.util.List;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
}