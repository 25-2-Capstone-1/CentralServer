package com.centralserver.demo.domain.googlemap.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleStreetViewUrlBuilder {

    @Value("${google.api.key}")
    private String apiKey;

    public String buildStreetViewURL(double lat, double lng) {
        return String.format(
                "https://maps.googleapis.com/maps/api/streetview?size=800x600&location=%f,%f&fov=80&heading=0&pitch=0&key=%s",
                lat, lng, apiKey
        );
    }
}
