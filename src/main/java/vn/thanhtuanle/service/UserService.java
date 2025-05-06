package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.model.dto.ImportUserDTO;
import vn.thanhtuanle.model.request.UserRequest;
import vn.thanhtuanle.model.request.MultipleCreateUserRequest;
import vn.thanhtuanle.model.dto.UserDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    @Transactional
    UserDTO create(UserRequest req, MultipartFile avatar) throws IOException;

    UserDTO getCurrentUser(String token);

    UserDTO getCurrentUser();

    @Transactional
    Map<String, Object> multipleCreate(MultipleCreateUserRequest req);

    Page<UserDTO> getAll(Pageable pageable, String query, UserStatus status);

    byte[] exportExcel(String query, UserStatus status, String sort, String order);

    Map<String, Object> saveUsers(List<ImportUserDTO> validUsers);

    Map<String, Object> importUsers(MultipartFile file) throws IOException;

    void resetPassword(String id);

    UserDTO changeStatus(String id, UserStatus status);

    UserDTO getUserByEmail(String email);

    User getCurrentUserEntity();

    User getUserById(String id);
}
