package vn.thanhtuanle.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.TokenType;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.common.service.JwtService;
import vn.thanhtuanle.entity.Token;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.model.request.LoginRequest;
import vn.thanhtuanle.model.request.TokenRequest;
import vn.thanhtuanle.model.request.VerificationRequest;
import vn.thanhtuanle.model.response.AuthResponse;
import vn.thanhtuanle.repository.TokenRepository;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.AuthService;
import vn.thanhtuanle.service.TwoFactorAuthService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final TwoFactorAuthService tfaService;

    private void savedUserToken(User user, String jwtToken, TokenType type) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(type)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        tokenRepository.deleteAll(validTokens);
    }

    @Override
    public AuthResponse login(LoginRequest req) {
        log.info("Login attempt with email: {}", req.getEmail());

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed for email: {} - Reason: User not found", req.getEmail());
                    return new AppException(ErrorCode.INVALID_CREDENTIALS);
                });

        if (user.getStatus().equals(UserStatus.BLOCKED)) {
            log.warn("Login failed for email: {} - Reason: User is inactive", req.getEmail());
            throw new AppException(ErrorCode.USER_BLOCKED);
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (BadCredentialsException e) {
            log.warn("Login failed for email: {} - Reason: Invalid credentials", req.getEmail());
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        savedUserToken(user, jwtToken, TokenType.ACCESS);
        savedUserToken(user, refreshToken, TokenType.REFRESH);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        log.info("Login successful for email: {}", req.getEmail());

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        if (user.isMfaEnabled()) {
            return AuthResponse.builder()
                    .mfaEnabled(true)
                    .secretImageUri(tfaService.generateQrCodeImageUri(user.getSecret()))
                    .build();
        }

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(true)
                .build();
    }

    @Override
    public boolean introspect(TokenRequest token) {
        if (token.getToken().startsWith("Bearer ")) {
            token.setToken(token.getToken().substring(7));
        }

        Token existedToken = tokenRepository.findByToken(token.getToken()).orElse(null);

        if (existedToken == null) {
            return false;
        }

        return !jwtService.isTokenExpired(existedToken.getToken());
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Refresh token attempt");
        String email = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Refresh token failed for email: {} - Reason: User not found", email);
                    return new AppException(ErrorCode.INVALID_CREDENTIALS);
                });

        if(jwtService.isTokenValid(refreshToken, user)) {
            String accessToken = jwtService.generateToken(user);

            revokeAllUserTokens(user);
            savedUserToken(user, accessToken, TokenType.ACCESS);

            log.info("Refresh token successful for email: {}", email);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        log.warn("Refresh token failed for email: {} - Reason: Invalid token", email);
        throw new AppException(ErrorCode.INVALID_TOKEN);
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            log.info("Logout attempt for email: {}", user.getEmail());

            revokeAllUserTokens(user);
            SecurityContextHolder.clearContext();

            log.info("Logout successful for email: {}", user.getEmail());
        } else {
            log.warn("Logout attempt with no authenticated user");
            throw new AppException(ErrorCode.NOT_AUTHENTICATED);
        }
    }

    @Override
    public AuthResponse verifyMfaCode(VerificationRequest req) {
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (tfaService.isOtpNotValid(user.getSecret(), req.getCode())) {
            log.warn("MFA verification failed for email: {} - Reason: Invalid OTP code", req.getEmail());
            throw new AppException(ErrorCode.INVALID_2FA_CODE);
        }

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        savedUserToken(user, jwtToken, TokenType.ACCESS);
        savedUserToken(user, refreshToken, TokenType.REFRESH);

        log.info("MFA verification successful for email: {}", req.getEmail());

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(user.isMfaEnabled())
                .build();
    }
}
