package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.entity.Department;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.model.request.DepartmentRequest;

import java.util.List;

public interface DepartmentService {

    Page<DepartmentDTO> findAll(Pageable pageable, String query);

    DepartmentDTO createDepartment(DepartmentRequest departmentRequest);

    DepartmentDTO getDepartmentById(Integer id);

    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentRequest);

    void deleteDepartment(Integer id);
}
