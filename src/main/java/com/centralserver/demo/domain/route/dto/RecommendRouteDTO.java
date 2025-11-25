package com.centralserver.demo.domain.route.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendRouteDTO {

    private Long routeId;
    private String routeName;

    private Point myPoint;
    private Point startPoint;
    private Point endPoint;

    private List<Point> myToStart;
    private List<Point> startToEnd;

    private double distance;
    private int estimatedTime;

    private String difficulty;
    private String description;
}