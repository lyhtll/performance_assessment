package com.example.demo.global.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RateLimitConfig {

    private static final long REFILL_DURATION_SECONDS = 60L;

    private final RedisClient redisClient;

    @Bean
    public ProxyManager<String> proxyManager() {
        StatefulRedisConnection<String, byte[]> connection =
                redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        return LettuceBasedProxyManager.builderFor(connection)
                .withExpirationStrategy(
                        ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(
                                Duration.ofMinutes(2)
                        )
                )
                .build();
    }

    public static BucketConfiguration createBucketConfiguration(String endpoint) {
        Pair<Long, Long> rateLimit = getRateLimitForEndpoint(endpoint);
        long capacity = rateLimit.getFirst();
        long refillTokens = rateLimit.getSecond();

        return BucketConfiguration.builder()
                .addLimit(
                        Bandwidth.builder()
                                .capacity(capacity)
                                .refillGreedy(refillTokens, Duration.ofMinutes(1))
                                .build()
                )
                .build();
    }

    public static Pair<Long, Long> getRateLimitForEndpoint(String endpoint) {
        // 로그인/회원가입: 1분당 5개
        if (endpoint.startsWith("/auth/login") || endpoint.startsWith("/auth/signup")) {
            return new Pair<>(5L, 5L);
        }
        // 기타 인증 API: 1분당 10개
        if (endpoint.startsWith("/auth/")) {
            return new Pair<>(10L, 10L);
        }
        // 일반 API: 1분당 100개
        return new Pair<>(100L, 100L);
    }

    public static long getRetryAfterSeconds() {
        return REFILL_DURATION_SECONDS;
    }

    public static class Pair<F, S> {
        private final F first;
        private final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public F getFirst() {
            return first;
        }

        public S getSecond() {
            return second;
        }
    }
}

