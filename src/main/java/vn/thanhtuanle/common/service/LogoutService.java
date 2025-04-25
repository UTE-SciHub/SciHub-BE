package vn.thanhtuanle.common.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.entity.Token;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.repository.TokenRepository;
import vn.thanhtuanle.repository.UserRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Logout attempt with invalid or missing Authorization header");
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        String token = authHeader.substring(7);
        Token existedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.warn("Logout failed - Token not found");
                    return new AppException(ErrorCode.INVALID_TOKEN);
                });

        String userEmail = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.warn("Logout failed - User not found");
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });

        if (existedToken.isRevoked() || existedToken.isExpired()) {
            log.warn("Logout failed - Token is already revoked or expired");
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        log.info("Logout attempt for email: {}", user.getEmail());

        tokenRepository.findAllValidTokenByUser(user.getId()).forEach(tokenRepository::delete);

        SecurityContextHolder.clearContext();

        log.info("Logout successful for email: {}", user.getEmail());
    }
}