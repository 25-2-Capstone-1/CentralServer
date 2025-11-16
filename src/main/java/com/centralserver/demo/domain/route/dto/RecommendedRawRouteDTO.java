package com.centralserver.demo.domain.route.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedRawRouteDTO {

    private Point startPoint;
    private Point endPoint;
    private List<Point> waypoints;
    private double distance; //m 단위
}