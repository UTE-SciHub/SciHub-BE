package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.Constant;
import vn.thanhtuanle.entity.User;
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
}
