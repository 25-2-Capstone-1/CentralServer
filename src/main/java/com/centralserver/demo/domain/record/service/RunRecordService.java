
package com.centralserver.demo.domain.record.service;

import com.centralserver.demo.domain.record.dto.*;
import com.centralserver.demo.domain.record.dto.RunRecordRequestDTO;
import com.centralserver.demo.domain.record.dto.RunRecordResponseDTO;
import com.centralserver.demo.domain.record.dto.RunRecordUpdateDTO;
import com.centralserver.demo.domain.record.entity.RunRecordEntity;
import com.centralserver.demo.domain.record.repository.RunRecordRepository;
import com.centralserver.demo.domain.route.entity.RecommendedRoute;
import com.centralserver.demo.domain.route.repository.RecommendedRouteRepository;
import com.centralserver.demo.domain.user.entity.UserEntity;
import com.centralserver.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RunRecordService {

    private final UserRepository userRepository;
    private final RecommendedRouteRepository recommendedRouteRepository;
    private final RunRecordRepository runRecordRepository;

    /** 로그인한 사용자 가져오기 */
    private UserEntity getSessionUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized access.");
        }

        String email = auth.getName();

        return userRepository.findByUserEmailAndIsLock(email, false)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /** 1) 저장(Create) */
    public RunRecordResponseDTO createRecord(RunRecordRequestDTO dto) {

        // 1. SecurityContext 에서 이메일 꺼내기
        UserEntity user = getSessionUser();

        // 2. 추천 경로 엔티티 (optional)
        RecommendedRoute recommendedRoute = null;
        if (dto.getRecommendedRouteId() != null) {
            recommendedRoute = recommendedRouteRepository
                    .getReferenceById(dto.getRecommendedRouteId());
        }

        //저장값
        RunRecordEntity record = RunRecordEntity.builder()
                .user(user)
                .recommendedRoute(recommendedRoute)
                .title(dto.getTitle())
                .startTime(dto.getStartTime())
                .durationSeconds(dto.getDurationSeconds())
                .distanceKm(dto.getDistanceKm())
                .avgPace(dto.getAvgPace())
                .calories(calculateCalories(dto))
                .cadence(calculateCadence(dto))
                .fullAddress(dto.getFullAddress())
                .waypointsJson(dto.getWaypointsJson())
                .bookmark(false)
                .build();

        RunRecordEntity saved = runRecordRepository.save(record);

        //리턴값
        return RunRecordResponseDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .bookmark(saved.isBookmark())
                .startTime(saved.getStartTime())
                .durationSeconds(saved.getDurationSeconds())
                .distanceKm(saved.getDistanceKm())
                .avgPace(saved.getAvgPace())
                .calories(saved.getCalories())
                .cadence(saved.getCadence())
                .fullAddress(saved.getFullAddress())
                .waypointsJson(saved.getWaypointsJson())
                .recommendedRouteId(
                        saved.getRecommendedRoute() != null
                                ? saved.getRecommendedRoute().getRouteId()
                                : null
                )
                .build();
    }

    /** 🔥 칼로리 계산 로직 */
    private int calculateCalories(RunRecordRequestDTO dto) {

        int weight = 70;

        double distance = dto.getDistanceKm();   // 이미 double이므로 그대로 사용

        double calories = distance * weight * 1.03;

        return (int) calories;
    }

    /** 🔥 케이던스(cadence) 계산 로직 */
    private int calculateCadence(RunRecordRequestDTO dto) {

        // "05:12" → 312.0초
        double paceSeconds = parsePaceToSeconds(dto.getAvgPace());

        if (paceSeconds <= 0) return 0;

        // 예시 공식: 케이던스 ≈ 180 - (paceSeconds / 30)
        double rawCadence = 180 - (paceSeconds / 30);

        return (int) rawCadence;
    }

    /** 🔧 "MM:SS" -> seconds 변환 함수 */
    private double parsePaceToSeconds(String paceStr) {
        try {
            if (paceStr.contains(":")) {
                String[] parts = paceStr.split(":");
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return minutes * 60 + seconds;
            }
            return Double.parseDouble(paceStr); // 혹시 "300"처럼 올 수도 있음
        } catch (Exception e) {
            return 0; // 잘못된 값일 경우 0 처리
        }
    }


    /** 2) 단일 기록 조회(Read One) */
    public RunRecordResponseDTO getRecord(Long recordId) throws AccessDeniedException {
        UserEntity user = getSessionUser();

        RunRecordEntity record = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to view this record.");
        }

        return RunRecordResponseDTO.builder()
                .id(record.getId())
                .title(record.getTitle())
                .bookmark(record.isBookmark())
                .recommendedRouteId(
                        record.getRecommendedRoute() != null
                                ? record.getRecommendedRoute().getRouteId()
                                : null
                )
                .startTime(record.getStartTime())
                .durationSeconds(record.getDurationSeconds())
                .distanceKm(record.getDistanceKm())
                .avgPace(record.getAvgPace())
                .calories(record.getCalories())
                .cadence(record.getCadence())
                .fullAddress(record.getFullAddress())
                .waypointsJson(record.getWaypointsJson())
                .build();
    }

    /** 3) 본인 기록 전체 조회(Read All) */
    public List<RunRecordSimpleResponseDTO> getMyRecords() {
        UserEntity user = getSessionUser();

        List<RunRecordEntity> records = runRecordRepository.findAllByUser_Id(user.getId());

        return records.stream()
                .map(record -> RunRecordSimpleResponseDTO.builder()
                        .id(record.getId())
                        .title(record.getTitle())
                        .bookmark(record.isBookmark())
                        .recommendedRouteId(
                                record.getRecommendedRoute() != null
                                        ? record.getRecommendedRoute().getRouteId()
                                        : null
                        )
                        .startTime(record.getStartTime())
                        .durationSeconds(record.getDurationSeconds())
                        .distanceKm(record.getDistanceKm())
                        .avgPace(record.getAvgPace())
                        .waypointsJson(record.getWaypointsJson())
                        .build()
                )
                .toList();
    }

    /** 6) 북마크된 기록만 조회(Read Bookmarked Only) */
    public List<RunRecordSimpleResponseDTO> getMyBookmarkedRecords() {
        UserEntity user = getSessionUser();

        List<RunRecordEntity> records = runRecordRepository
                .findAllByUser_IdAndBookmarkTrue(user.getId());

        return records.stream()
                .map(record -> RunRecordSimpleResponseDTO.builder()
                        .id(record.getId())
                        .title(record.getTitle())
                        .bookmark(record.isBookmark())
                        .recommendedRouteId(
                                record.getRecommendedRoute() != null
                                        ? record.getRecommendedRoute().getRouteId()
                                        : null
                        )
                        .startTime(record.getStartTime())
                        .durationSeconds(record.getDurationSeconds())
                        .distanceKm(record.getDistanceKm())
                        .avgPace(record.getAvgPace())
                        .waypointsJson(record.getWaypointsJson())
                        .build()
                )
                .toList();
    }


    /** -------- UPDATE 영역 -------- */

    /** 4) 기록 수정(Update) */
    public RunRecordResponseDTO updateRecord(Long recordId, RunRecordUpdateDTO dto) throws AccessDeniedException {
        UserEntity user = getSessionUser();

        RunRecordEntity record = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to update this record.");
        }

        // 🟣 변경 가능한 필드만 업데이트
        if (dto.getTitle() != null) {
            record.setTitle(dto.getTitle());
        }

        if (dto.getBookmark() != null) {
            record.setBookmark(dto.getBookmark());
        }

        RunRecordEntity saved = runRecordRepository.save(record);

        // ⭐ 여기서 바로 DTO로 변환해서 반환 (Lazy Proxy 직렬화 문제 방지)
        return RunRecordResponseDTO.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .bookmark(saved.isBookmark())
                .recommendedRouteId(
                        saved.getRecommendedRoute() != null
                                ? saved.getRecommendedRoute().getRouteId()
                                : null
                )
                .startTime(saved.getStartTime())
                .durationSeconds(saved.getDurationSeconds())
                .distanceKm(saved.getDistanceKm())
                .avgPace(saved.getAvgPace())
                .calories(saved.getCalories())
                .cadence(saved.getCadence())
                .fullAddress(saved.getFullAddress())
                .waypointsJson(saved.getWaypointsJson())
                .build();
    }

//    public RunRecordEntity patchRecord(Long recordId, RunRecordPatchDTO dto) throws AccessDeniedException {
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String email = auth.getName();
//
//        UserEntity user = userRepository.findByUserEmailAndIsLock(email, false)
//                .orElseThrow(() -> new UsernameNotFoundException(email));
//
//        RunRecordEntity record = runRecordRepository.findById(recordId)
//                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
//
//        // 권한 체크
//        if (!record.getUser().getId().equals(user.getId())) {
//            throw new AccessDeniedException("You don't have permission to edit this record.");
//        }
//
//        // ✔ PATCH – null 아닌 값만 업데이트
//        if (dto.getTitle() != null) record.setTitle(dto.getTitle());
//        if (dto.getStartTime() != null) record.setStartTime(dto.getStartTime());
//        if (dto.getDurationSeconds() != null) record.setDurationSeconds(dto.getDurationSeconds());
//        if (dto.getDistanceKm() != null) record.setDistanceKm(dto.getDistanceKm());
//        if (dto.getAvgPace() != null) record.setAvgPace(dto.getAvgPace());
//        if (dto.getCalories() != null) record.setCalories(dto.getCalories());
//        if (dto.getCadence() != null) record.setCadence(dto.getCadence());
//        if (dto.getFullAddress() != null) record.setFullAddress(dto.getFullAddress());
//        if (dto.getWaypointsJson() != null) record.setWaypointsJson(dto.getWaypointsJson());
//
//        return runRecordRepository.save(record);
//    }

    /** -------- DELETE 영역 -------- */

    /** 5) 기록 삭제(Delete) */
    public void deleteRecord(Long recordId) throws AccessDeniedException {
        UserEntity user = getSessionUser();

        RunRecordEntity record = runRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found."));

        if (!record.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You don't have permission to delete this record.");
        }

        runRecordRepository.delete(record);
    }
}
