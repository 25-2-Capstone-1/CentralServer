package com.centralserver.demo.domain.route.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedRawRouteDTO {
    private Point myPoint;
    private Point startPoint;
    private Point endPoint;

    private List<Point> myToStart;
    private List<Point> startToEnd;

    private double distance;
}