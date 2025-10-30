package com.centralserver.demo.domain.user.service;

import com.centralserver.demo.domain.jwt.service.JwtService;
import com.centralserver.demo.domain.user.dto.MessageResponseDTO;
import com.centralserver.demo.domain.user.dto.UserRequestDTO;
import com.centralserver.demo.domain.user.dto.UserResponseDTO;
import com.centralserver.demo.domain.user.dto.UserUpdateResponseDTO;
import com.centralserver.demo.domain.user.entity.UserEntity;
import com.centralserver.demo.domain.user.entity.UserRoleType;
import com.centralserver.demo.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // 자체 로그인 회원 가입 (존재 여부)
    @Transactional(readOnly = true)
    public Boolean existUser(UserRequestDTO dto) {
        return userRepository.existsByUserEmail(dto.getUsername());
    }

    // 자체 로그인 회원 가입
    @Transactional
    public Long addUser(UserRequestDTO dto) {
        if (userRepository.existsByUserEmail(dto.getUsername())) {
            throw new IllegalArgumentException("이미 유저가 존재합니다.");
        }

        UserEntity entity = new UserEntity().builder()
                .userEmail(dto.getUserEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .username(dto.getUsername())
                .nickname(dto.getNickname())
                .phoneNumber(dto.getPhoneNumber())
                .isLock(false)
                .roleType(UserRoleType.USER)
                .build();

        return userRepository.save(entity).getId();
    }

    // 자체 로그인
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {

        UserEntity entity = userRepository.findByUserEmailAndIsLock(userEmail, false)
                .orElseThrow(() -> new UsernameNotFoundException(userEmail));

        return User.builder()
                .username(entity.getUserEmail())
                .password(entity.getPassword())
                .roles(entity.getRoleType().name())
                .accountLocked(entity.getIsLock())
                .build();
    }

    // 자체 로그인 회원 정보 수정

    public UserUpdateResponseDTO updateUser(UserRequestDTO dto) throws AccessDeniedException {

        String sessionUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!sessionUserEmail.equals(dto.getUserEmail())) {
            throw new AccessDeniedException("본인 계정만 수정 가능합니다.");
        }

        UserEntity entity = userRepository.findByUserEmailAndIsLock(dto.getUserEmail(), false)
                .orElseThrow(() -> new UsernameNotFoundException(dto.getUserEmail()));

        entity.updateUser(dto);

        UserEntity updatedEntity = userRepository.save(entity);

        return UserUpdateResponseDTO.builder()
                .userId(updatedEntity.getId())
                .nickname(updatedEntity.getNickname())
                .updatedDate(updatedEntity.getUpdatedDate()) // 엔티티에 updatedAt 필드가 있다고 가정
                .build();
    }

    // 자체/소셜 로그인 회원 탈퇴
    @Transactional
    public MessageResponseDTO deleteUser(UserRequestDTO dto) throws AccessDeniedException {

        // 본인 및 어드민만 삭제 가능 검증
        SecurityContext context = SecurityContextHolder.getContext();
        String sessionUserEmail = context.getAuthentication().getName();
        String sessionRole = context.getAuthentication().getAuthorities().iterator().next().getAuthority();

        boolean isOwner = sessionUserEmail.equals(dto.getUserEmail());
        boolean isAdmin = sessionRole.equals("ROLE_"+UserRoleType.ADMIN.name());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("본인 혹은 관리자만 삭제할 수 있습니다.");
        }

        // 유저 제거
        userRepository.deleteByUserEmail(dto.getUserEmail());

        // Refresh 토큰 제거
        jwtService.removeRefreshUser(dto.getUserEmail());

        return MessageResponseDTO.builder()
                .message("Account successfully deleted.")
                .build();
    }

    // 소셜 로그인 (매 로그인 시 : 신규 = 가입, 기존 = 업데이트)

    // 자체/소셜 유저 정보 조회
    @Transactional(readOnly = true)
    public UserResponseDTO readUser() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity entity = userRepository.findByUserEmailAndIsLock(userEmail, false)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다: " + userEmail));

        return new UserResponseDTO(userEmail, entity.getNickname(), entity.getUsername(), entity.getPhoneNumber());
    }

}
