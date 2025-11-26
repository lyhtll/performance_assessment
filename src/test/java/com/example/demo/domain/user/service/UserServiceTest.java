package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.dto.response.GetMeResponse;
import com.example.demo.global.security.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceTest {

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "testUser", "password123", UserRole.USER);
    }

    @Test
    @DisplayName("현재 사용자 정보 조회 성공")
    void getMe_Success() {
        // Given
        given(securityUtil.getCurrentUser()).willReturn(testUser);

        // When
        GetMeResponse result = userService.getMe();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("testUser");
        assertThat(result.role()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("관리자 사용자 정보 조회 성공")
    void getMe_Success_AdminUser() {
        // Given
        User adminUser = new User(2L, "adminUser", "password123", UserRole.ADMIN);
        given(securityUtil.getCurrentUser()).willReturn(adminUser);

        // When
        GetMeResponse result = userService.getMe();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("adminUser");
        assertThat(result.role()).isEqualTo(UserRole.ADMIN);
    }
}