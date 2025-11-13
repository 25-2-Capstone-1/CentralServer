package com.centralserver.demo.api.handler;

import com.centralserver.demo.domain.jwt.service.JwtService;
import com.centralserver.demo.security.CustomUserDetails;
import com.centralserver.demo.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@Qualifier("LoginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;

    public LoginSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        // CustomUserDetails 로 캐스팅
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getUserId();
        String userEmail = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        log.info("로그인 성공! userId: {}, userEmail: {}, role: {}", userId, userEmail, role);

        // JWT(Access/Refresh) 발급
        String accessToken = JWTUtil.createJWT(userId, userEmail, role, true);
        String refreshToken = JWTUtil.createJWT(userId, userEmail, role, false);

        // 발급한 Refresh 저장 (Refresh whitelist)
        jwtService.addRefresh(userEmail, refreshToken);

        // 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = String.format(
                "{\"userId\":\"%d\", \"accessToken\":\"%s\", \"refreshToken\":\"%s\"}",
                userId, accessToken, refreshToken
        );

        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
