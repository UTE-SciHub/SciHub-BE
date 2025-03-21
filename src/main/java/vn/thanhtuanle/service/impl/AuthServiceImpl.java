package vn.thanhtuanle.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.TokenType;
import vn.thanhtuanle.common.service.JwtService;
import vn.thanhtuanle.entity.Token;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.model.request.LoginRequest;
import vn.thanhtuanle.model.response.AuthResponse;
import vn.thanhtuanle.repository.TokenRepository;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.AuthService;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    private void savedUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
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
        savedUserToken(user, jwtToken);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        log.info("Login successful for email: {}", req.getEmail());
        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public boolean introspect(String token) {
        Token existedToken = tokenRepository.findByToken(token)
                .orElse(null);

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
            savedUserToken(user, accessToken);

            log.info("Refresh token successful for email: {}", email);
            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }

        log.warn("Refresh token failed for email: {} - Reason: Invalid token", email);
        throw new AppException(ErrorCode.INVALID_TOKEN);
    }
}
