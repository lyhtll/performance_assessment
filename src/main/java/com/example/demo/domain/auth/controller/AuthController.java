package com.example.demo.domain.auth.controller;

import com.example.demo.domain.auth.docs.AuthDocs;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ReissueRequest;
import com.example.demo.domain.auth.dto.request.SignUpRequest;
import com.example.demo.domain.auth.service.AuthService;
import com.example.demo.global.common.BaseResponse;
import com.example.demo.global.security.jwt.response.TokenResponse;
import com.example.demo.global.security.jwt.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController implements AuthDocs {

    private final AuthService authService;
    private final CookieUtil cookieUtil;

    public AuthController(AuthService authService, CookieUtil cookieUtil) {
        this.authService = authService;
        this.cookieUtil = cookieUtil;
    }

    @PostMapping("/signup")
    @Override
    public ResponseEntity<BaseResponse.Empty> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signup(signUpRequest);
        return BaseResponse.success(HttpStatus.CREATED.value(), "Success");
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<BaseResponse.Empty> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        TokenResponse tokenResponse = authService.login(loginRequest);

        // HTTP-Only 쿠키에 토큰 저장
        cookieUtil.addTokenCookies(response, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        return BaseResponse.success(HttpStatus.OK.value(), "Success");
    }

    @PostMapping("/reissue")
    @Override
    public ResponseEntity<BaseResponse.Empty> reissue(
            HttpServletRequest request,
            HttpServletResponse response) {
        // 쿠키에서 Refresh Token 추출
        String refreshToken = cookieUtil.getRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new IllegalArgumentException("Refresh token not found in cookie");
        }

        TokenResponse tokenResponse = authService.reissue(new ReissueRequest(refreshToken));

        // 새로운 토큰을 쿠키에 저장
        cookieUtil.addTokenCookies(response, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());

        return BaseResponse.success(HttpStatus.OK.value(), "Success");
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<BaseResponse.Empty> logout(HttpServletResponse response) {
        authService.logout();
        // 쿠키에서 토큰 삭제
        cookieUtil.deleteTokenCookies(response);
        return BaseResponse.success(HttpStatus.OK.value(), "Success");
    }
}

