package com.centralserver.demo.util;

import com.centralserver.demo.domain.settings.detail.dto.DetailSettingsResponseDTO;
import com.centralserver.demo.domain.settings.detail.entity.Gender;
import org.springframework.stereotype.Component;
import com.centralserver.demo.domain.settings.pace.dto.PaceRecommendationResponseDTO;


@Component
public class PaceCalculator {

    public PaceRecommendationResponseDTO calculatePace(DetailSettingsResponseDTO userDetail) {

        double heightM = userDetail.getHeight() / 100.0;
        double bmi = userDetail.getWeight() / (heightM * heightM);

        // 성별 보정
        double genderFactor = (userDetail.getGender() == Gender.MALE) ? 1.05 : 0.95;

        // BMI 보정
        double bmiFactor =
                (bmi < 18.5) ? 1.08 :
                        (bmi < 25)   ? 1.00 :
                                (bmi < 30)   ? 1.05 : 1.10;

        // 기준 중급 Pace = 6분 30초 (390초)
        int baseSeconds = 6 * 60 + 30;
        int intermediateSec = (int)(baseSeconds * genderFactor * bmiFactor);

        // 초급 & 고급 (원생 값)
        int beginnerSec = intermediateSec + 30;
        int advancedSec = intermediateSec - 40;

        // 🔥 10초 단위로 반올림 적용
        beginnerSec = roundTo10Seconds(beginnerSec);
        intermediateSec = roundTo10Seconds(intermediateSec);
        advancedSec = roundTo10Seconds(advancedSec);

        return new PaceRecommendationResponseDTO(
                toPaceFormat(beginnerSec),
                toPaceFormat(intermediateSec),
                toPaceFormat(advancedSec)
        );
    }

    private String toPaceFormat(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }

    private int roundTo10Seconds(int sec) {
        return (int)(Math.round(sec / 10.0) * 10);
    }
}
