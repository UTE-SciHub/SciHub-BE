package vn.thanhtuanle.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vn.thanhtuanle.entity.User;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of("sys_admin");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User userPrincipal = (User) principal;
            return Optional.of(userPrincipal.getId().toString());
        } else if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.of("unknown");
    }
}
