package vn.thanhtuanle.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.thanhtuanle.entity.User;

import java.util.Optional;
import java.util.UUID;

public class AuditorAwareImpl implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }

        User userPrincipal = (User) authentication.getPrincipal();
        return Optional.of(userPrincipal.getId());
    }
}
