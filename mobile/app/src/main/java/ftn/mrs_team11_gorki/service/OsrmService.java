package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.OsrmRouteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OsrmService {
    // coords mora biti "lon1,lat1;lon2,lat2" (OBRATI PAŽNJU: lon,lat) :contentReference[oaicite:3]{index=3}
    @GET("route/v1/driving/{coords}")
    Call<OsrmRouteResponse> route(
            @Path(value = "coords", encoded = true) String coords,
            @Query("overview") String overview,      // "full"
            @Query("geometries") String geometries   // "polyline6"
    );
}