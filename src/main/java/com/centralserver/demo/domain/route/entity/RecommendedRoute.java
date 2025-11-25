package com.centralserver.demo.domain.route.entity;

// import com.centralserver.demo.domain.googlemap.entity.StreetViewImage;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecommendedRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long routeId;

//    @OneToMany(mappedBy = "recommendedRoute", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StreetViewImage> streetViewImages = new ArrayList<>();

    private String routeName;

    /** 전체 거리 (m 단위) */
    private double distance;

    private int estimatedTime;
    private String difficulty;
    private String description;

    /** 내 위치 */
    @Lob
    private String myPointJson;

    /** 시작점 */
    @Lob
    private String startPointJson;

    /** 종료점 */
    @Lob
    private String endPointJson;

    /** 내 위치 → 시작점 */
    @Lob
    private String myToStartJson;

    /** 시작점 → 종료점 */
    @Lob
    private String startToEndJson;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}