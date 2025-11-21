package com.centralserver.demo.domain.googlemap.entity;

import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StreetViewImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommended_route_id")
    private RecommendedRoute recommendedRoute;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;

    @Column(nullable = false)
    private String imageUrl;   // S3 URL 저장
}
