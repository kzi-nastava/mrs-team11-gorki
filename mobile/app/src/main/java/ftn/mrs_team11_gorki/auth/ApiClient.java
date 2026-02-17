package ftn.mrs_team11_gorki.auth;

import android.content.Context;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ftn.mrs_team11_gorki.auth.TokenStorage;
public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/";

    private static Retrofit retrofitAuth;
    private static Retrofit retrofitPublic;

    public static Retrofit getRetrofit(Context context) {
        if (retrofitAuth == null) {
            TokenStorage tokenStorage = new TokenStorage(context.getApplicationContext());

            OkHttpClient okHttp = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenStorage))
                    .build();

            retrofitAuth = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitAuth;
    }

    public static Retrofit getPublicRetrofit() {
        if (retrofitPublic == null) {
            OkHttpClient okHttp = new OkHttpClient.Builder().build();

            retrofitPublic = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitPublic;
    }


    public static void reset() {
        retrofitAuth = null;
        retrofitPublic = null;
    }
}