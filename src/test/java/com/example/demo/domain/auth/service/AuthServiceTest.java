package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.SignUpRequest;
import com.example.demo.domain.auth.repository.BlacklistTokenRepository;
import com.example.demo.domain.auth.repository.RefreshTokenRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.error.UserError;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.error.CustomException;
import com.example.demo.global.security.jwt.provider.JwtProvider;
import com.example.demo.global.security.jwt.response.TokenResponse;
import com.example.demo.global.security.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 테스트")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private RefreshTokenRepository tokenRepository;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private BlacklistTokenRepository blacklistTokenRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private String dummyPasswordHash = "dummyHash";

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testUser", "encodedPassword", UserRole.USER);
        // dummyPasswordHash 필드는 생성자에서 설정되므로 리플렉션으로 설정
        try {
            var field = AuthService.class.getDeclaredField("dummyPasswordHash");
            field.setAccessible(true);
            field.set(authService, dummyPasswordHash);
        } catch (Exception e) {
            // Ignore for test
        }
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // Given
        SignUpRequest request = new SignUpRequest("newUser", "password123");
        given(userRepository.existsByName("newUser")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encodedPassword");

        // When
        authService.signup(request);

        // Then
        verify(userRepository).existsByName("newUser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복된 사용자명")
    void signup_Fail_DuplicateUsername() {
        // Given
        SignUpRequest request = new SignUpRequest("existingUser", "password123");
        given(userRepository.existsByName("existingUser")).willReturn(true);

        // When & Then
        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(UserError.USERNAME_DUPLICATION.getMessage());

        verify(userRepository).existsByName("existingUser");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // Given
        LoginRequest request = new LoginRequest("testUser", "password123");
        TokenResponse tokenResponse = new TokenResponse("accessToken", "refreshToken");
        
        given(userRepository.findByName("testUser")).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches("password123", "encodedPassword")).willReturn(true);
        given(jwtProvider.generateAndSaveTokens("testUser", UserRole.USER)).willReturn(tokenResponse);

        // When
        TokenResponse result = authService.login(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo("accessToken");
        assertThat(result.refreshToken()).isEqualTo("refreshToken");
        verify(userRepository).findByName("testUser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtProvider).generateAndSaveTokens("testUser", UserRole.USER);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_Fail_UserNotFound() {
        // Given
        LoginRequest request = new LoginRequest("nonExistentUser", "password123");
        given(userRepository.findByName("nonExistentUser")).willReturn(Optional.empty());
        given(passwordEncoder.encode(dummyPasswordHash)).willReturn("dummyEncoded");

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(UserError.INVALID_CREDENTIALS.getMessage());

        verify(userRepository).findByName("nonExistentUser");
        verify(passwordEncoder).matches("password123", "dummyEncoded");
        verify(jwtProvider, never()).generateAndSaveTokens(anyString(), any(UserRole.class));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_Fail_InvalidPassword() {
        // Given
        LoginRequest request = new LoginRequest("testUser", "wrongPassword");
        given(userRepository.findByName("testUser")).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches("wrongPassword", "encodedPassword")).willReturn(false);

        // When & Then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(UserError.INVALID_CREDENTIALS.getMessage());

        verify(userRepository).findByName("testUser");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
        verify(jwtProvider, never()).generateAndSaveTokens(anyString(), any(UserRole.class));
    }
}