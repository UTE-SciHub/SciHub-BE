package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.model.response.UserResponse;

public interface UserService {
    @Transactional
    UserResponse create(UserDTO req);
}
