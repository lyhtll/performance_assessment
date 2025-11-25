package com.example.demo.domain.user.dto.response;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;

public record GetMeResponse(
        Long id,
        String name,
        UserRole role
) {
    public static GetMeResponse of(User user) {
        return new GetMeResponse(
                user.getId(),
                user.getName(),
                user.getRole()
        );
    }
}

