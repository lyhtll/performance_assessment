package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.response.GetMeResponse;
import com.example.demo.global.security.util.SecurityUtil;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final SecurityUtil securityUtil;

    public UserService(SecurityUtil securityUtil) {
        this.securityUtil = securityUtil;
    }

    public GetMeResponse getMe() {
        return GetMeResponse.of(securityUtil.getCurrentUser());
    }
}

