package ftn.mrs_team11_gorki.service;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WsManager {

    public interface PanicListener {
        void onPanicEvent(String payloadJson);
        void onError(Throwable t);
    }

    private final String wsUrl;
    private StompClient stomp;
    private final CompositeDisposable bag = new CompositeDisposable();

    public WsManager(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    public void connect(String jwtToken, PanicListener listener) {
        disconnect();

        String auth = "Bearer " + jwtToken;

        // 1) HTTP headers za WebSocket HANDSHAKE (ovo ti rešava 401 na upgrade)
        Map<String, String> httpHeaders = new HashMap<>();
        httpHeaders.put("Authorization", auth);

        stomp = Stomp.over(Stomp.ConnectionProvider.OKHTTP, wsUrl, httpHeaders);

        // 2) STOMP CONNECT headers (da bi tvoj StompAuthChannelInterceptor video Authorization header)
        List<StompHeader> stompHeaders = Arrays.asList(
                new StompHeader("Authorization", auth)
        );

        bag.add(stomp.lifecycle().subscribe(lc -> {
            if (lc.getType() == LifecycleEvent.Type.OPENED) {
                Log.d("WS", "OPENED");
            } else if (lc.getType() == LifecycleEvent.Type.ERROR) {
                Log.e("WS", "ERROR", lc.getException());
                if (listener != null) listener.onError(lc.getException());
            } else if (lc.getType() == LifecycleEvent.Type.CLOSED) {
                Log.d("WS", "CLOSED");
            }
        }));

        // connect + headers
        stomp.connect(stompHeaders); // :contentReference[oaicite:2]{index=2}

        // subscribe
        bag.add(
                stomp.topic("/topic/panic")
                        .subscribe(msg -> {
                            if (listener != null) listener.onPanicEvent(msg.getPayload());
                        }, t -> {
                            if (listener != null) listener.onError(t);
                        })
        );
    }

    public void disconnect() {
        try { bag.clear(); } catch (Exception ignored) {}
        try { if (stomp != null) stomp.disconnect(); } catch (Exception ignored) {}
        stomp = null;
    }
}