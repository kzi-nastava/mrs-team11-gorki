package ftn.mrs_team11_gorki.auth;

import android.content.Context;
import android.content.SharedPreferences;
import ftn.mrs_team11_gorki.dto.LoginResponse;
public class TokenStorage {
    private static final String PREFS = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_ROLE = "role";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences sp;

    public TokenStorage(Context context) {
        sp = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(LoginResponse res) {
        sp.edit()
                .putString(KEY_TOKEN, res.getToken())
                .putString(KEY_ROLE, res.getRole())
                .putLong(KEY_USER_ID, res.getId() != null ? res.getId() : -1L)
                .apply();
    }

    public String getToken() {
        return sp.getString(KEY_TOKEN, null);
    }

    public Long getUserId() {
        long id = sp.getLong(KEY_USER_ID, -1L);
        return id == -1L ? null : id;
    }

    public String getRole() {
        return sp.getString(KEY_ROLE, null);
    }

    public void clear() {
        sp.edit().clear().apply();
    }


}
