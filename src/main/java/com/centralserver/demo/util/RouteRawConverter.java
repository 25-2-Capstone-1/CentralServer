package com.centralserver.demo.util;

import com.centralserver.demo.domain.route.client.dto.ExternalRouteItem;
import com.centralserver.demo.domain.route.dto.Point;
import com.centralserver.demo.domain.route.dto.RecommendedRawRouteDTO;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RouteRawConverter {

    public RecommendedRawRouteDTO convert(
            ExternalRouteItem item,
            Point myPoint
    ) {
        return RecommendedRawRouteDTO.builder()
                .myPoint(myPoint)
                .startPoint(item.getCourse_info().getStartPoint())
                .endPoint(item.getCourse_info().getEndPoint())
                .distance(item.getCourse_info().getDistance())
                .myToStart(extractPoints(item.getMy2start()))
                .startToEnd(extractPoints(item.getStart2end()))
                .build();
    }

    private List<Point> extractPoints(JsonNode node) {
        List<Point> points = new ArrayList<>();

        JsonNode coordinates = node
                .path("rawGraphhopper")
                .path("paths")
                .get(0)
                .path("points")
                .path("coordinates");

        for (JsonNode c : coordinates) {
            double lng = c.get(0).asDouble();
            double lat = c.get(1).asDouble();
            points.add(new Point(lat, lng));
        }

        return points;
    }
}
