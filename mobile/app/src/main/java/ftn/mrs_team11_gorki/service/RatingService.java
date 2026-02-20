package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.CreateRatingDTO;
import ftn.mrs_team11_gorki.dto.CreatedRatingDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RatingService {

    @GET("api/rides/ratings/pending-latest")
    Call<Long> getPendingLatestRideId();

    @POST("api/rides/{id}/rating")
    Call<CreatedRatingDTO> rateRide(@Path("id") long rideId, @Body CreateRatingDTO dto);
}
