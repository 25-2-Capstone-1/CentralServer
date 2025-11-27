package com.centralserver.demo.domain.route.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoadviewItem {
    private double lat;
    private double lng;
    private String url;
}