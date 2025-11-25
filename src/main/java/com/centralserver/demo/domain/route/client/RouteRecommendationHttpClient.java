package com.centralserver.demo.domain.route.client;

import com.centralserver.demo.domain.route.client.dto.ExternalRouteResponse;
import com.centralserver.demo.domain.route.dto.RecommendedRawRouteDTO;
import com.centralserver.demo.domain.route.dto.RouteRequestDTO;
import com.centralserver.demo.util.RouteRawConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class RouteRecommendationHttpClient implements RouteRecommendationClient {

    private final WebClient.Builder webClientBuilder;
    private final RouteRawConverter converter;

    @Override
    public List<RecommendedRawRouteDTO> getRecommendedRoutes(RouteRequestDTO req) {

        ExternalRouteResponse response = webClientBuilder.build()
                .post()
                .uri("http://13.125.217.144:5000/routes/findway")
                .bodyValue(req)
                .retrieve()
                .bodyToMono(ExternalRouteResponse.class)
                .block();

        return response.getRoute().stream()
                .map(item -> converter.convert(item, req.myPoint()))
                .toList();
    }
}
