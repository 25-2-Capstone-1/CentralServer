package com.centralserver.demo.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserRequestDTO {

    public interface onExist {} // 회원 가입시 username 존재 확인
    public interface onCreate {} // 회원 가입시
    public interface onPasswordChange {} // 비밀번호 변경시
    public interface onNicknameUpdate {} // 회원 수정시
    public interface onDelete {} // 회원 삭제시

    @Email(groups = {onExist.class, onCreate.class, onNicknameUpdate.class, onDelete.class, onPasswordChange.class})
    private String userEmail;

    @NotBlank(groups = {onCreate.class, onPasswordChange.class}) @Size(min = 4, max = 20)
    private String password;

    @NotBlank(groups = {onCreate.class}) @Size(min = 4, max = 20)
    private String username;

    @NotBlank(groups = {onCreate.class, onNicknameUpdate.class}) @Size(min = 4, max = 20)
    private String nickname;

    @NotBlank(groups = {onCreate.class})
    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호 형식은 010-XXXX-XXXX 이어야 합니다.")
    private String phoneNumber;
}
