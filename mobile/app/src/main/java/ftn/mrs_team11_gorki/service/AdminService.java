package ftn.mrs_team11_gorki.service;

import java.util.List;

import ftn.mrs_team11_gorki.dto.AdminRideMonitorDTO;
import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import ftn.mrs_team11_gorki.dto.GetDriverInfoDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminService {

    @GET("api/admin/rides/history")
    Call<List<DriverRideHistoryDTO>> getAdminRideHistory(
            @Header("Authorization") String bearerToken,
            @Query("from") String from,
            @Query("to") String to
    );

    @GET("api/admin/rides/panic")
    Call<List<DriverRideHistoryDTO>> getPanicRides(
            @Header("Authorization") String bearerToken,
            @Query("from") String from,
            @Query("to") String to
    );

    @GET("/api/admin/drivers/search")
    Call<GetDriverInfoDTO> searchDriver(@Query("q") String q);

    @GET("/api/admin/drivers/{driverId}/ride/active")
    Call<AdminRideMonitorDTO> getActiveRide(@Path("driverId") long driverId);
}