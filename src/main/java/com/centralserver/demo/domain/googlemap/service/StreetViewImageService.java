package com.centralserver.demo.domain.googlemap.service;

import com.centralserver.demo.domain.googlemap.repository.StreetViewImageRepository;
import com.centralserver.demo.domain.googlemap.entity.StreetViewImage;
import com.centralserver.demo.domain.googlemap.util.GoogleMapImageDownloader;
import com.centralserver.demo.domain.googlemap.util.GoogleStreetViewUrlBuilder;
import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StreetViewImageService {

    private final GoogleStreetViewUrlBuilder streetUrlBuilder;
    private final GoogleMapImageDownloader downloader;
    private final S3Service s3Service;
    private final StreetViewImageRepository streetViewImageRepository;

    @Transactional
    public StreetViewImage createAndSaveImage(RecommendedRoute route, double lat, double lng) {

        try {
            // 1) Google Street View URL 생성
            String url = streetUrlBuilder.buildStreetViewURL(lat, lng);

            // 2) 이미지 다운로드
            byte[] bytes = downloader.download(url);

            // 3) S3 Key 생성
            String key = "recommended-routes/" + route.getRouteId() +
                    "/street_" + lat + "_" + lng + ".jpg";

            // 4) S3 업로드
            String uploadedUrl = s3Service.uploadBytes(bytes, key, "image/jpeg");

            // 5) DB 저장
            StreetViewImage entity = StreetViewImage.builder()
                    .recommendedRoute(route)
                    .lat(lat)
                    .lng(lng)
                    .imageUrl(uploadedUrl)
                    .build();

            return streetViewImageRepository.save(entity);

        } catch (Exception e) {
            throw new RuntimeException("Street View 이미지 생성에 실패했습니다.", e);
        }
    }
}
