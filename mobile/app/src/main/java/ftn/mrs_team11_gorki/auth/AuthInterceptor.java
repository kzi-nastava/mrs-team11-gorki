package ftn.mrs_team11_gorki.auth;

import androidx.annotation.NonNull;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final TokenStorage tokenStorage;

    public AuthInterceptor(TokenStorage tokenStorage) {
        this.tokenStorage = tokenStorage;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        String token = tokenStorage.getToken();
        if (token == null || token.isEmpty()) {
            return chain.proceed(original);
        }

        Request withAuth = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        return chain.proceed(withAuth);
    }
}