package ftn.mrs_team11_gorki.auth;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ftn.mrs_team11_gorki.auth.TokenStorage;
public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(Context context) {
        if (retrofit == null) {
            TokenStorage tokenStorage = new TokenStorage(context.getApplicationContext());

            OkHttpClient okHttp = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenStorage))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8080/")
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}