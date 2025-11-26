package com.example.demo.domain.auth.service;

import com.example.demo.domain.auth.dto.request.LoginRequest;
import com.example.demo.domain.auth.dto.request.ReissueRequest;
import com.example.demo.domain.auth.dto.request.SignUpRequest;
import com.example.demo.global.security.jwt.response.TokenResponse;

import com.example.demo.global.security.jwt.type.TokenType;
import com.example.demo.global.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository tokenRepository;
    private final SecurityUtil securityUtil;
    private final BlacklistTokenRepository blacklistTokenRepository;
    private final String dummyPasswordHash;
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtProvider jwtProvider,
            RefreshTokenRepository tokenRepository,
            SecurityUtil securityUtil,
            BlacklistTokenRepository blacklistTokenRepository,
            String dummyPasswordHash) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.tokenRepository = tokenRepository;
        this.securityUtil = securityUtil;
        this.blacklistTokenRepository = blacklistTokenRepository;
        this.dummyPasswordHash = dummyPasswordHash;
    }

    @Transactional
    public void signup(SignUpRequest request) {
        validateUsernameNotExists(request.name());

        User user = new User(
                null,
                request.name(),
                passwordEncoder.encode(request.password()),
                UserRole.USER);
        userRepository.save(user);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByName(request.name())
                .orElse(null);

        if (user == null) {
            passwordEncoder.matches(request.password(), passwordEncoder.encode(dummyPasswordHash));
            throw new CustomException(UserError.INVALID_CREDENTIALS);
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new CustomException(UserError.INVALID_CREDENTIALS);
        }

        return jwtProvider.generateAndSaveTokens(user.getName(), user.getRole());
    }

    @Transactional
    public TokenResponse reissue(ReissueRequest request) {
        String refreshToken = request.refreshToken();

        jwtProvider.validateTokenType(refreshToken, TokenType.REFRESH);

        String username = jwtProvider.extractUsername(refreshToken);

        validateRefreshToken(username, refreshToken);

        User user = userRepository.findByNameOrThrow(username);
        TokenResponse tokens = jwtProvider.generateAndSaveTokens(user.getName(), user.getRole());

        return tokens;
    }

    @Transactional
    public void logout() {
        String username = securityUtil.getCurrentUser().getName();
        tokenRepository.deleteById(username);

        String accessToken = securityUtil.getCurrentAccessToken();

        Date expiredAt = jwtProvider.extractExpiration(accessToken);
        blacklistTokenRepository.save(
                new BlacklistToken(accessToken, expiredAt.getTime()));

        if (tokenRepository.existsById(username)) {
            throw new CustomException(JwtError.TOKEN_DELETE_FAILED);
        }
    }

    private void validateRefreshToken(String username, String refreshToken) {
        RefreshToken savedToken = tokenRepository.findByUsername(username);

        if (savedToken == null) {
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);
        }

        // Timing Attack 방지
        if (!MessageDigest.isEqual(
                savedToken.getRefreshToken().getBytes(),
                refreshToken.getBytes())) {
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);
        }
    }

    private void validateUsernameNotExists(String name) {
        if (userRepository.existsByName(name)) {
            throw new CustomException(UserError.USERNAME_DUPLICATION);
        }
    }
}
