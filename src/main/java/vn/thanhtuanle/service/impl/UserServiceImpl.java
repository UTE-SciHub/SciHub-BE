package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.service.JwtService;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.model.response.UserResponse;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.UserService;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @Transactional
    @Override
    public UserResponse create(UserDTO req) {
        log.info("Create user with email: {}", req.getEmail());
        String id = req.getEmail().split("@")[0];
        User user = User.builder()
                .id(id)
                .email(req.getEmail())
                .password(encoder.encode(Constant.USER_PASSWORD_DEFAULT.getValue()))
                .build();

        User savedUser = userRepository.save(user);

        log.info("User created with email: {}", req.getEmail());

        return modelMapper.map(savedUser, UserResponse.class);
    }

    @Override
    public UserResponse getCurrentUser(String token) {
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });

        return modelMapper.map(user, UserResponse.class);
    }
}
