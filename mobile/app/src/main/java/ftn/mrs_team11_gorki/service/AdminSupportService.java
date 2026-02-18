package ftn.mrs_team11_gorki.service;


import java.util.List;
import ftn.mrs_team11_gorki.dto.ChatDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AdminSupportService {
    @GET("api/admin/support/chats")
    Call<List<ChatDTO>> listChats();
}
