package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.ReportDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportService {

    @GET("api/users/passenger/{id}/report")
    Call<ReportDTO> generatePassengerReport(
            @Path("id") Long id,
            @Query("from") String from,   // "yyyy-MM-dd" ili null
            @Query("to") String to        // "yyyy-MM-dd" ili null
    );

    @GET("api/users/driver/{id}/report")
    Call<ReportDTO> generateDriverReport(
            @Path("id") Long id,
            @Query("from") String from,
            @Query("to") String to
    );

    @GET("api/users/all/report")
    Call<ReportDTO> generateAdminAggregateReport(
            @Query("from") String from,
            @Query("to") String to
    );

    @GET("api/users/report")
    Call<ReportDTO> generateAdminUserReport(
            @Query("email") String email,
            @Query("from") String from,
            @Query("to") String to
    );
}
