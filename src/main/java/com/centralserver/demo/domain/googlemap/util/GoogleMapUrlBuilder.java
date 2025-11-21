package com.centralserver.demo.domain.googlemap.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GoogleMapUrlBuilder {

    @Value("${google.api.key}")
    private String apiKey;

    public String buildStaticMapURL(List<double[]> waypoints) {

        StringBuilder path = new StringBuilder("path=color:0xff0000ff|weight:5");

        for (double[] p : waypoints) {
            path.append("|").append(p[0]).append(",").append(p[1]);
        }

        return "https://maps.googleapis.com/maps/api/staticmap?"
                + "size=800x800&scale=2&"
                + path
                + "&key=" + apiKey;
    }
}