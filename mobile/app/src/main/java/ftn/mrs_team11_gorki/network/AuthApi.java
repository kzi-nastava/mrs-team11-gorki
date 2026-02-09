package ftn.mrs_team11_gorki.network;

import ftn.mrs_team11_gorki.model.PassengerRegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/api/auth/register")
    Call<Void> register(@Body PassengerRegisterRequest request);
}