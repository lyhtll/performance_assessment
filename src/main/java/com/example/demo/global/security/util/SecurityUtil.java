package com.example.demo.global.security.util;

import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.error.UserError;
import com.example.demo.global.error.CustomException;
import com.example.demo.global.security.user.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class SecurityUtil {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(UserError.USER_NOT_FOUND);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new CustomException(UserError.USER_NOT_FOUND);
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        return userDetails.getUser();
    }

    public String getCurrentAccessToken() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new IllegalStateException("No request context available");
        }

        HttpServletRequest request = attributes.getRequest();
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        // 쿠키에서 토큰 가져오기
        jakarta.servlet.http.Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (jakarta.servlet.http.Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        throw new IllegalStateException("No access token found");
    }
}

