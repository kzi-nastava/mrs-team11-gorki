package ftn.mrs_team11_gorki.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class ImageUtil {

    /**
     * Supports:
     * - "data:image/png;base64,AAAA..."
     * - "AAAA..." (raw base64)
     */
    public static Bitmap decodeBase64ToBitmap(String base64OrDataUrl) {
        if (base64OrDataUrl == null) return null;

        String s = base64OrDataUrl.trim();
        if (s.isEmpty()) return null;

        int comma = s.indexOf(',');
        if (s.startsWith("data:") && comma >= 0) {
            s = s.substring(comma + 1);
        }

        try {
            byte[] bytes = Base64.decode(s, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}
