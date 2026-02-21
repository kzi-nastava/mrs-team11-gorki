package ftn.mrs_team11_gorki.service;
import ftn.mrs_team11_gorki.dto.PassengerRegisterRequest;
import ftn.mrs_team11_gorki.dto.ResetPasswordDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ftn.mrs_team11_gorki.dto.LoginResponse;
import ftn.mrs_team11_gorki.dto.LoginRequest;
import retrofit2.http.Query;

public interface AuthService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/auth/register")
    Call<Void> register(@Body PassengerRegisterRequest request);

    @POST("api/auth/forgot-password")
    Call<Void> forgotPassword(@Query("email") String email);

    @POST("api/auth/reset-password")
    Call<Void> resetPassword(@Body ResetPasswordDTO body);
}
