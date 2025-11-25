package com.centralserver.demo.api.handler;

import com.centralserver.demo.domain.jwt.service.JwtService;
import com.centralserver.demo.domain.settings.service.SettingsService;
import com.centralserver.demo.domain.user.entity.UserEntity;
import com.centralserver.demo.domain.user.repository.UserRepository;
import com.centralserver.demo.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Qualifier("LoginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final SettingsService settingsService;

    public LoginSuccessHandler(
            JwtService jwtService,
            UserRepository userRepository,
            SettingsService settingsService
    ) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.settingsService = settingsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // username, role
        String userEmail =  authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // 1) 유저 엔티티 조회
        UserEntity user = userRepository.findByUserEmailAndIsLock(userEmail, false)
                .orElseThrow(() -> new RuntimeException("유저가 존재하지 않습니다."));

        // 2) 첫 로그인 여부 확인
        boolean firstLogin = settingsService.isFirstLogin(user);

        // 3) JWT 생성 (claim에 firstLogin 포함)
        String accessToken = JWTUtil.createJWT(userEmail, role, true, firstLogin);
        String refreshToken = JWTUtil.createJWT(userEmail, role, false, firstLogin);

        // 4) Refresh Token 저장 (Whitelist)
        jwtService.addRefresh(userEmail, refreshToken);

        // 5) 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format(
                "{\"accessToken\":\"%s\", \"refreshToken\":\"%s\", \"firstLogin\": %s}",
                accessToken, refreshToken, firstLogin
        );

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
