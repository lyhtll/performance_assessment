package com.example.demo.domain.auth.controller;

import com.example.demo.domain.auth.docs.AuthDocs;
import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ReissueRequest;
import com.example.demo.domain.auth.dto.request.SignUpRequest;
import com.example.demo.domain.auth.service.AuthServiceInterface;
import com.example.demo.domain.auth.error.AuthError;
import com.example.demo.global.common.BaseResponse;
import com.example.demo.global.config.properties.SecurityProperties;
import com.example.demo.global.error.CustomException;
import com.example.demo.global.security.jwt.response.TokenResponse;
import com.example.demo.global.security.jwt.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthDocs {

    private final AuthServiceInterface authService;
    private final CookieUtil cookieUtil;
    private final SecurityProperties securityProperties;

    @PostMapping("/signup")
    @Override
    public ResponseEntity<BaseResponse.Empty> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signup(signUpRequest);
        return BaseResponse.success(HttpStatus.CREATED.value(), "Success");
    }

    @PostMapping("/login")
    @Override
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            @RequestHeader(value = "X-App-Secret", required = false) String appSecretHeader,
            HttpServletRequest request,
            HttpServletResponse response) {
        validateClientAuthentication(request, appSecretHeader);
        TokenResponse tokenResponse = authService.login(loginRequest);
        return handleTokenResponse(tokenResponse, appSecretHeader, response);
    }

    @PostMapping("/reissue")
    @Override
    public ResponseEntity<?> reissue(
            @RequestHeader(value = "X-App-Secret", required = false) String appSecretHeader,
            HttpServletRequest request,
            HttpServletResponse response) {
        validateClientAuthentication(request, appSecretHeader);
        String refreshToken = extractRefreshToken(request);
        TokenResponse tokenResponse = authService.reissue(new ReissueRequest(refreshToken));
        return handleTokenResponse(tokenResponse, appSecretHeader, response);
    }

    @PostMapping("/logout")
    @Override
    public ResponseEntity<BaseResponse.Empty> logout(HttpServletResponse response) {
        authService.logout();
        cookieUtil.deleteTokenCookies(response);
        return BaseResponse.success(HttpStatus.OK.value(), "Success");
    }

    private void validateClientAuthentication(HttpServletRequest request, String appSecretHeader) {
        if (appSecretHeader != null) {
            // 앱: Secret 검증
            if (!appSecretHeader.equals(securityProperties.getAppSecret())) {
                throw new CustomException(AuthError.INVALID_APP_SECRET);
            }
        }
    }

    private ResponseEntity<?> handleTokenResponse(
            TokenResponse tokenResponse,
            String appSecretHeader,
            HttpServletResponse response) {
        if (appSecretHeader != null) {
            // 앱: JSON으로 토큰 반환
            return BaseResponse.of(tokenResponse, HttpStatus.OK.value(), "Success");
        } else {
            // 웹: HTTP-Only 쿠키에 토큰 저장
            cookieUtil.addTokenCookies(response, tokenResponse.accessToken(), tokenResponse.refreshToken());
            return BaseResponse.success(HttpStatus.OK.value(), "Success");
        }
    }

    private String extractRefreshToken(HttpServletRequest request) {
        String tokenFromCookie = cookieUtil.getRefreshTokenFromCookie(request);
        String authHeader = request.getHeader("Authorization");
        String tokenFromHeader = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String extracted = authHeader.substring("Bearer ".length());
            if (!extracted.isBlank()) {
                tokenFromHeader = extracted;
            }
        }

        if (tokenFromCookie != null) {
            return tokenFromCookie;
        } else if (tokenFromHeader != null) {
            return tokenFromHeader;
        } else {
            throw new CustomException(AuthError.REFRESH_TOKEN_NOT_FOUND);
        }
    }
}

