package com.centralserver.demo.domain.route.client.dto;

import com.centralserver.demo.domain.route.dto.Point;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExternalCourseInfo {
    private Point startPoint;
    private Point endPoint;
    private List<Point> waypoints;   // 필요 없으면 그냥 없애도 됨
    private double distance;
}
