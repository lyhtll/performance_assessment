package com.example.demo.domain.user.controller;

import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.dto.response.GetMeResponse;
import com.example.demo.domain.user.service.UserServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController 테스트")
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceInterface userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("현재 사용자 정보 조회 성공")
    void getMe_Success() throws Exception {
        // Given
        GetMeResponse response = new GetMeResponse(1L, "testUser", UserRole.USER);
        given(userService.getMe()).willReturn(response);

        // When & Then
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("사용자 정보를 가져왔습니다."))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("testUser"))
                .andExpect(jsonPath("$.data.role").value("USER"))
                .andDo(print());
    }
}