package ftn.mrs_team11_gorki.service;

import ftn.mrs_team11_gorki.dto.ChatDTO;
import ftn.mrs_team11_gorki.dto.SendMessageRequest;
import ftn.mrs_team11_gorki.dto.MessageDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SupportChatService {

    @GET("api/support/chat")
    Call<ChatDTO> myChat();

    @POST("api/support/messages")
    Call<MessageDTO> sendFallback(@Body SendMessageRequest req);
}
