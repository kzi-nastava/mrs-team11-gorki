package ftn.mrs_team11_gorki.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import ftn.mrs_team11_gorki.adapter.LocalDateTimeAdapter;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ftn.mrs_team11_gorki.BuildConfig;

public final class ClientUtils {

    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static Retrofit retrofit;
    private static AuthService authService;

    private ClientUtils() {}

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public static AuthService auth() {
        if (authService == null) {
            authService = getRetrofit().create(AuthService.class);
        }
        return authService;
    }

    public static AdminService getAdminService() {
        return getRetrofit().create(AdminService.class);
    }

    public static RideService getRideService() {
        return getRetrofit().create(RideService.class);
    }
}