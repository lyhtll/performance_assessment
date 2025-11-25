package com.example.demo.domain.auth.docs;

import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.SignUpRequest;
import com.example.demo.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "인증 API")
public interface AuthDocs {

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    ResponseEntity<BaseResponse.Empty> signup(SignUpRequest signUpRequest);

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인합니다. 토큰은 HTTP-Only 쿠키로 전달됩니다."
    )
    ResponseEntity<BaseResponse.Empty> login(LoginRequest loginRequest, HttpServletResponse response);

    @Operation(
            summary = "토큰 재발급",
            description = "쿠키의 리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다. 새 토큰은 HTTP-Only 쿠키로 전달됩니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    ResponseEntity<BaseResponse.Empty> reissue(HttpServletRequest request, HttpServletResponse response);

    @Operation(
            summary = "로그아웃",
            description = "사용자를 로그아웃 처리하고 쿠키를 삭제합니다"
    )
    ResponseEntity<BaseResponse.Empty> logout(HttpServletResponse response);
}

