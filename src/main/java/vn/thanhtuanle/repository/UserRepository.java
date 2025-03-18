package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
