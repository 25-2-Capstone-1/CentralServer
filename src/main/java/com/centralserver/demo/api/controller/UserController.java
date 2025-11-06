package com.centralserver.demo.api.controller;

import com.centralserver.demo.domain.common.dto.ApiResponseDTO;
import com.centralserver.demo.domain.common.dto.MessageResponseDTO;
import com.centralserver.demo.domain.user.dto.*;

import com.centralserver.demo.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.Map;

@RestController

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainP(){
        return "USER CONTROLLER!";
    }

    // 자체 로그인 유저 존재 확인
    @PostMapping(value = "/user/exist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> existUserApi(
            @Validated(UserRequestDTO.onExist.class) @RequestBody UserRequestDTO dto
    ) {
        return ResponseEntity.ok(userService.existUser(dto));
    }

    // 회원가입
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDTO<UserJoinResponseDTO>> joinApi(
            @Validated(UserRequestDTO.onCreate.class) @RequestBody UserRequestDTO dto
    ) {
        Long id = userService.addUser(dto);
        Map<String, Long> responseBody = Collections.singletonMap("userEntityId", id);

        UserJoinResponseDTO responseData = UserJoinResponseDTO.builder()
                .userId(id)
                .username(dto.getUserEmail()) // dto 안에 username 필드 있다고 가정
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(responseData));
    }

    // 유저 수정 (자체 로그인 유저만)
    @PutMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDTO<UserUpdateResponseDTO>> updateUserApi(
            @Validated(UserRequestDTO.onUpdate.class) @RequestBody UserRequestDTO dto
    ) throws AccessDeniedException {

        UserUpdateResponseDTO responseData = userService.updateUser(dto); // 이제 DTO를 반환하게 변경

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(responseData));
    }



    // 유저 제거 (자체/소셜)
    @DeleteMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseDTO<MessageResponseDTO>> deleteUserApi(
            @Validated(UserRequestDTO.onDelete.class) @RequestBody UserRequestDTO dto
    ) throws AccessDeniedException {

        MessageResponseDTO responseData = userService.deleteUser(dto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponseDTO.success(responseData)); // 감싸서 리턴
    }

    // 유저 정보 GETMAPPING 추가

    // 유저 정보
    @GetMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserResponseDTO userMeApi() {
        return userService.readUser();
    }

    // 유저 이메일 조회
    @GetMapping("/user/userEmail")
    public UserEmailDTO getUserEmail() {
        return userService.readUserEmail(); // 서비스에서 현재 유저 이메일 가져오기
    }

    // 유저 이름 조회
    @GetMapping("/user/username")
    public UsernameDTO getUserUsername() {
        return userService.readUsername();
    }

    // 유저 ID 조회
    @GetMapping("/user/userId")
    public UserIdDTO  getUserId() {
        return userService.readUserId();
    }

}
