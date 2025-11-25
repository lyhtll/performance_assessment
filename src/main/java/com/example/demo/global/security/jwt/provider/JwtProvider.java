package com.example.demo.global.security.jwt.provider;

import com.example.demo.domain.auth.domain.RefreshToken;
import com.example.demo.domain.auth.repository.BlacklistTokenRepository;
import com.example.demo.domain.auth.repository.RefreshTokenRepository;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.domain.UserRole;
import com.example.demo.domain.user.repository.UserCacheRepository;
import com.example.demo.domain.user.repository.UserRepository;
import com.example.demo.global.error.CustomException;
import com.example.demo.global.security.jwt.error.JwtError;
import com.example.demo.global.security.jwt.properties.JwtProperties;
import com.example.demo.global.security.jwt.response.TokenResponse;
import com.example.demo.global.security.jwt.type.TokenType;
import com.example.demo.global.security.user.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtProvider {

    private final JwtProperties jwtProperties;
    private final UserCacheRepository userCacheRepository;
    private final RefreshTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final BlacklistTokenRepository blacklistTokenRepository;

    private SecretKey secretKey;
    private io.jsonwebtoken.JwtParser jwtParser;

    public JwtProvider(JwtProperties jwtProperties,
                      UserCacheRepository userCacheRepository,
                      RefreshTokenRepository tokenRepository,
                      UserRepository userRepository,
                      BlacklistTokenRepository blacklistTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.userCacheRepository = userCacheRepository;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.blacklistTokenRepository = blacklistTokenRepository;
        initializeKeys();
    }

    private void initializeKeys() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes());
        this.jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();
    }

    public TokenResponse generateAndSaveTokens(String username, UserRole role) {
        String accessToken = generateAccessToken(username, role);
        String refreshToken = generateRefreshToken(username);

        // RefreshToken 엔티티로 저장 - username을 ID로 사용
        RefreshToken refreshTokenEntity = new RefreshToken(
                username,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtProperties.getRefreshTokenExpiration() / 1000)
        );

        tokenRepository.save(refreshTokenEntity);

        return new TokenResponse(accessToken, refreshToken);
    }

    public String generateAccessToken(String username, UserRole role) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(username)
                .claim("role", role.name())
                .claim("type", TokenType.ACCESS.name())
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .subject(username)
                .claim("type", TokenType.REFRESH.name())
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public UserRole extractRole(String token) {
        String roleString = (String) getClaims(token).get("role");
        if (roleString == null) {
            throw new CustomException(JwtError.INVALID_TOKEN);
        }

        try {
            return UserRole.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            throw new CustomException(JwtError.INVALID_TOKEN);
        }
    }

    public TokenType extractTokenType(String token) {
        String typeString = (String) getClaims(token).get("type");
        if (typeString == null) {
            throw new CustomException(JwtError.INVALID_TOKEN);
        }

        try {
            return TokenType.valueOf(typeString);
        } catch (IllegalArgumentException e) {
            throw new CustomException(JwtError.INVALID_TOKEN);
        }
    }

    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    public void validateTokenType(String token, TokenType expectedType) {
        TokenType actualType = extractTokenType(token);
        if (actualType != expectedType) {
            throw new CustomException(JwtError.INVALID_TOKEN_TYPE);
        }
    }

    public boolean isBlacklisted(String token) {
        return blacklistTokenRepository.existsById(token);
    }

    public Claims getClaims(String token) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new CustomException(JwtError.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new CustomException(JwtError.UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            throw new CustomException(JwtError.MALFORMED_TOKEN);
        } catch (Exception e) {
            throw new CustomException(JwtError.INVALID_TOKEN);
        }
    }

    public Authentication getAuthentication(String token) {
        validateTokenType(token, TokenType.ACCESS);

        if (isBlacklisted(token)) {
            throw new CustomException(JwtError.BLACKLISTED_TOKEN);
        }

        String username = extractUsername(token);
        UserRole role = extractRole(token);

        User user = userCacheRepository.findByUsername(username);
        if (user == null) {
            user = userRepository.findByNameOrThrow(username);
            userCacheRepository.save(user);
        }

        CustomUserDetails details = new CustomUserDetails(user);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

