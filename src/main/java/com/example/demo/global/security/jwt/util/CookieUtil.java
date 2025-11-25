package com.example.demo.global.security.jwt.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    private static final int ACCESS_TOKEN_MAX_AGE = 30 * 60; // 30분
    private static final int REFRESH_TOKEN_MAX_AGE = 7 * 24 * 60 * 60; // 7일

    public void addTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        Cookie accessCookie = createCookie(ACCESS_TOKEN_COOKIE_NAME, accessToken, ACCESS_TOKEN_MAX_AGE);
        Cookie refreshCookie = createCookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken, REFRESH_TOKEN_MAX_AGE);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, ACCESS_TOKEN_COOKIE_NAME);
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, REFRESH_TOKEN_COOKIE_NAME);
    }

    public void deleteTokenCookies(HttpServletResponse response) {
        Cookie accessCookie = createCookie(ACCESS_TOKEN_COOKIE_NAME, "", 0);
        Cookie refreshCookie = createCookie(REFRESH_TOKEN_COOKIE_NAME, "", 0);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private Cookie createCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}

