package com.example.demo.global.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private String appSecret;
    private String dummyPasswordHash;
}

