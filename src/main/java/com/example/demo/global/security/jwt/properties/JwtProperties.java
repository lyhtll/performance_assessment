package com.example.demo.global.security.jwt.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secretKey;
    private long accessTokenExpiration;
    private long refreshTokenExpiration;

}

