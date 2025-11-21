package com.centralserver.demo.domain.googlemap.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WaypointParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<double[]> parse(String json) {
        try {
            JsonNode arrayNode = mapper.readTree(json);
            List<double[]> waypoints = new ArrayList<>();

            for (JsonNode n : arrayNode) {
                double lat = n.get("lat").asDouble();
                double lng = n.get("lng").asDouble();
                waypoints.add(new double[]{lat, lng});
            }

            return waypoints;

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid waypointsJson format");
        }
    }
}