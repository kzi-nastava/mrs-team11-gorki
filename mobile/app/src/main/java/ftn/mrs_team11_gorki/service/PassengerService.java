package ftn.mrs_team11_gorki.service;

import java.util.List;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PassengerService {

    @GET("api/passengers/{id}/rides/history")
    Call<List<DriverRideHistoryDTO>> getPassengerRideHistory(
            @Header("Authorization") String bearerToken,
            @Path("id") Long passengerId,
            @Query("from") String from,
            @Query("to") String to
    );
}