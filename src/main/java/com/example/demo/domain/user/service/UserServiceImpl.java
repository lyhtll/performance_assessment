package com.example.demo.domain.user.service;

import com.example.demo.domain.user.dto.response.GetMeResponse;
import com.example.demo.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SecurityUtil securityUtil;

    public GetMeResponse getMe() {
        return GetMeResponse.of(securityUtil.getCurrentUser());
    }
}