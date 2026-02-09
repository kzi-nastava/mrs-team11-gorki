package ftn.mrs_team11_gorki.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8080"; // emulator -> localhost
    private static Retrofit retrofit;

    public static ftn.mrs_team11_gorki.network.AuthApi getAuthApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ftn.mrs_team11_gorki.network.AuthApi.class);
    }
}