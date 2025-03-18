package vn.thanhtuanle.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email) throws ResourceNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
