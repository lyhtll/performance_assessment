package com.example.demo.global.interceptor;

import com.example.demo.global.error.ErrorResponse;
import com.example.demo.global.ratelimit.RateLimitConfig;
import com.example.demo.global.ratelimit.RateLimitError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final ProxyManager<String> proxyManager;
    private final ObjectMapper objectMapper;

    public RateLimitInterceptor(ProxyManager<String> proxyManager, ObjectMapper objectMapper) {
        this.proxyManager = proxyManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                            HttpServletResponse response,
                            Object handler) throws IOException {
        String key = getClientKey(request);
        String endpoint = request.getRequestURI();

        Bucket bucket = proxyManager.builder()
                .build(key, () -> RateLimitConfig.createBucketConfiguration(endpoint));

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        RateLimitConfig.Pair<Long, Long> rateLimit = RateLimitConfig.getRateLimitForEndpoint(endpoint);
        long capacity = rateLimit.getFirst();

        response.addHeader("X-Rate-Limit-Limit", String.valueOf(capacity));
        response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));

        if (probe.isConsumed()) {
            return true;
        }

        // Rate limit 초과 시
        long retryAfter = RateLimitConfig.getRetryAfterSeconds();
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Retry-After", String.valueOf(retryAfter));

        ErrorResponse errorResponse = new ErrorResponse(
                RateLimitError.RATE_LIMIT_EXCEEDED.getStatus(),
                RateLimitError.RATE_LIMIT_EXCEEDED.getMessage()
        );
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

        return false;
    }

    private String getClientKey(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        String path = request.getRequestURI();
        return "rate_limit:" + ip + ":" + path;
    }
}

