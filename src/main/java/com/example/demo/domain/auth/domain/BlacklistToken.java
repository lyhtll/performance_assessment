package com.example.demo.domain.auth.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "blacklist", timeToLive = 1800)
public class BlacklistToken {

    @Id
    private String token;

    private Long expiredAt;
}

