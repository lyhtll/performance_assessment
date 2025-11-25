package com.example.demo.domain.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "refresh_token", timeToLive = 604800) // 7일
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    private String username; // username을 ID로 사용

    private String refreshToken;

    private LocalDateTime expiresAt;
}

