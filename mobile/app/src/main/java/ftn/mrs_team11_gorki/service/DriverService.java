package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
}