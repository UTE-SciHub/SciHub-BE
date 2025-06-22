package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.model.request.DepartmentRequest;

import java.io.IOException;

public interface DepartmentService {

    Page<DepartmentDTO> findAll(Pageable pageable, String query, Boolean delFlag);

    DepartmentDTO createDepartment(DepartmentRequest departmentRequest, MultipartFile logoFile) throws IOException;

    DepartmentDTO getDepartmentById(Integer id);

    DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentRequest, MultipartFile logoFile) throws IOException;

    void deleteDepartment(Integer id);

    byte[] exportExcel(String query, Boolean delFlag) throws IOException;
}
