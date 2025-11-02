package com.centralserver.demo.domain.route.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RecommendedRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name = "route-id-generator", strategy = "com.centralserver.demo.domain.route.util.RouteIdGenerator")
    private String routeId;

    private String routeName;
    private double distance;
    private int estimatedTime;
    private String difficulty;
    private String description;

    @Lob
    private String startPointJson;

    @Lob
    private String endPointJson;

    @Lob
    private String waypointsJson;

}
