package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ReissueRequest;
import com.example.demo.domain.auth.dto.request.SignUpRequest;
import com.example.demo.global.security.jwt.response.TokenResponse;

public interface AuthService {
    void signup(SignUpRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse reissue(ReissueRequest request);
    void logout();
}