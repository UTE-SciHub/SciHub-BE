package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.entity.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Integer>, JpaSpecificationExecutor<Department> {
    Optional<Department> findByEmail(String email);
}
