package ftn.mrs_team11_gorki.service;

import java.util.List;

import ftn.mrs_team11_gorki.dto.NotificationResponseDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    @GET("api/notifications/user/{userId}")
    Call<List<NotificationResponseDTO>> getAllForUser(@Path("userId") long userId);

    @PUT("api/notifications/{id}/read/{userId}")
    Call<ResponseBody> markAsRead(@Path("id") long notificationId, @Path("userId") long userId);
}