package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.GetUserDTO;
import ftn.mrs_team11_gorki.dto.UpdatePasswordDTO;
import ftn.mrs_team11_gorki.dto.UpdateUserDTO;
import ftn.mrs_team11_gorki.dto.UpdatedUserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @GET("api/users/{id}")
    Call<GetUserDTO> getUser(@Path("id") Long id);

    @PUT("api/users/{id}")
    Call<UpdatedUserDTO> updateUser(@Path("id") Long id, @Body UpdateUserDTO dto);

    @PUT("api/users/{id}/change-password")
    Call<UpdatedUserDTO> changePassword(@Path("id") Long id, @Body UpdatePasswordDTO dto);
}
