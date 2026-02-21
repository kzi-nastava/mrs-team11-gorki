package ftn.mrs_team11_gorki.auth;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

import ftn.mrs_team11_gorki.adapter.LocalDateTimeAdapter;
import ftn.mrs_team11_gorki.BuildConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import ftn.mrs_team11_gorki.adapter.LocalDateTimeAdapter;


public class ApiClient {

    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static Retrofit retrofitAuth;
    private static Retrofit retrofitPublic;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    public static Retrofit getRetrofit(Context context) {

        if (retrofitAuth == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setLenient()
                    .create();

            TokenStorage tokenStorage = new TokenStorage(context.getApplicationContext());

            // OkHttp client sa interceptorom
            OkHttpClient okHttp = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(tokenStorage))
                    .build();

            retrofitAuth = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttp)
                    // bitno: koristi custom gson (sa LocalDateTime adapterom)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofitAuth;
    }
    public static Retrofit getPublicRetrofit() {

        if (retrofitPublic == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setLenient()
                    .create();

            OkHttpClient okHttp = new OkHttpClient.Builder().build();

            retrofitPublic = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttp)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofitPublic;
    }


    /*
        Reset — koristi se npr. posle logout-a
        da se ponovo napravi retrofit sa novim tokenom
     */
    public static void reset() {
        retrofitAuth = null;
        retrofitPublic = null;
    }
}
