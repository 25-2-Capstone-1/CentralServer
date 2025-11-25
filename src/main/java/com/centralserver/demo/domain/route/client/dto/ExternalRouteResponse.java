package com.centralserver.demo.domain.route.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExternalRouteResponse {
    private List<ExternalRouteItem> route;
    private boolean success;
}