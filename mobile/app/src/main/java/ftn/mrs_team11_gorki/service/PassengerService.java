package ftn.mrs_team11_gorki.service;

import java.util.Collection;
import java.util.List;

import ftn.mrs_team11_gorki.dto.DriverRideHistoryDTO;
import ftn.mrs_team11_gorki.dto.GetRouteDTO;
import ftn.mrs_team11_gorki.dto.InconsistencyRequestDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
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

    @GET("api/passengers/{id}/ride/active")
    Call<DriverRideHistoryDTO> getActiveRide(@Path("id") long passengerId);

    @POST("api/rides/{rideId}/inconsistencies")
    Call<ResponseBody> reportInconsistency(
            @Path("rideId") long rideId,
            @Body InconsistencyRequestDTO body
    );

    @GET("api/passengers/{id}/favourite-routes")
    Call<Collection<GetRouteDTO>> getFavouriteRoutes(@Path("id") Long id);

    @POST("api/passengers/{id}/favourite-routes/{rideId}")
    Call<GetRouteDTO> addFavouriteRoute(@Path("id") Long id, @Path("rideId") Long rideId);

    @DELETE("api/passengers/{id}/favourite-routes/{routeId}")
    Call<Void> deleteFavouriteRoute(@Path("id") Long id, @Path("routeId") Long routeId);
}