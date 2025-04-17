package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.entity.Department;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.model.request.DepartmentRequest;

import java.io.IOException;
import java.util.List;

public interface DepartmentService {

    Page<DepartmentDTO> findAll(Pageable pageable, String query);

    DepartmentDTO createDepartment(DepartmentRequest departmentRequest, MultipartFile logoFile) throws IOException;

    DepartmentDTO getDepartmentById(Integer id);

    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentRequest, MultipartFile logoFile) throws IOException;

    void deleteDepartment(Integer id);
}
