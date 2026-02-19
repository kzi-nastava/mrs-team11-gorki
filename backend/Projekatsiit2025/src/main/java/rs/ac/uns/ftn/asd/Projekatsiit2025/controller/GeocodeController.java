package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GeocodeController {

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .defaultHeader("User-Agent", "021RideApp/1.0")
            .build();

    @GetMapping("/geocode")
    public Map<String, Double> geocode(@RequestParam String q) {

        @SuppressWarnings("unchecked")
		List<Map> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", q)
                        .queryParam("format", "json")
                        .queryParam("limit", "1")
                        .build())
                .retrieve()
                .bodyToMono(List.class)
                .block();

        if (response == null || response.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }

        Map first = response.get(0);

        double lat = Double.parseDouble(first.get("lat").toString());
        double lon = Double.parseDouble(first.get("lon").toString());

        return Map.of(
                "lat", lat,
                "lon", lon
        );
    }
}