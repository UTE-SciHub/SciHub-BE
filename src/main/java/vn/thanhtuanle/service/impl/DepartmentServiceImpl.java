package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.mapper.ExcelExporterFactory;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.common.service.ExcelExporter;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.service.DepartmentService;
import vn.thanhtuanle.model.request.DepartmentRequest;
import vn.thanhtuanle.entity.Department;
import vn.thanhtuanle.repository.DepartmentRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ExcelExporterFactory excelExporterFactory;
    @Qualifier("departmentExcelRowMapper")
    private final ExcelRowMapper<Department> departmentExcelRowMapper;

    private final List<String> EXCEL_HEADERS = List.of(
            "ID",
            "Tên phòng ban",
            "Số điện thoại",
            "Email",
            "Trạng thái"
    );

    @Override
    public Page<DepartmentDTO> findAll(Pageable pageable, String query, Boolean delFlag) {
        Specification<Department> spec = Specification.where(null);

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                );
            });
        }

        if (delFlag != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        Page<Department> departments = departmentRepository.findAll(spec, pageable);
        return departments.map(department -> modelMapper.map(department, DepartmentDTO.class));
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentRequest departmentRequest, MultipartFile logoFile) throws IOException {
        String imageUrl = null;
        String logoPublicId = null;
        if (logoFile != null && !logoFile.isEmpty() && ImageIO.read(logoFile.getInputStream()) != null) {
            try {
                Map result = cloudinaryService.upload(logoFile);
                imageUrl = String.valueOf(result.get("url"));
                logoPublicId = String.valueOf(result.get("public_id"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload logo file", e);
            }
        }

        Department department = Department.builder()
                .name(departmentRequest.getName())
                .description(departmentRequest.getDescription())
                .imageUrl(imageUrl)
                .logoPublicId(logoPublicId)
                .phoneNumber(departmentRequest.getPhoneNumber())
                .email(departmentRequest.getEmail())
                .delFlag(false)
                .build();

        return modelMapper.map(departmentRepository.save(department), DepartmentDTO.class);
    }

    @Override
    public DepartmentDTO getDepartmentById(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentRequest, MultipartFile logoFile) throws IOException {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        String oldImageUrl = department.getImageUrl();
        String oldPublicId = department.getLogoPublicId();
        Boolean oldDelFlag = department.getDelFlag();

        modelMapper.getConfiguration().setPropertyCondition(context -> context.getSource() != null); // Chỉ ánh xạ nếu giá trị không null
        modelMapper.map(departmentRequest, department);

        department.setId(id);

        String imageUrl = null;
        String newPublicId = null;
        if (logoFile != null && !logoFile.isEmpty() && ImageIO.read(logoFile.getInputStream()) != null) {
            try {
                String oldPublicIdToDelete = department.getLogoPublicId();
                String resourceType = "image";
                if (oldPublicIdToDelete != null && !oldPublicIdToDelete.isEmpty()) {
                    try {
                        cloudinaryService.delete(oldPublicIdToDelete, resourceType);
                    } catch (IOException e) {
                        System.err.println("Failed to delete old logo: " + e.getMessage());
                    }
                }

                Map result = cloudinaryService.upload(logoFile);
                imageUrl = String.valueOf(result.get("url"));
                newPublicId = String.valueOf(result.get("public_id"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload logo file", e);
            }
        }

        if (imageUrl != null && newPublicId != null) {
            department.setImageUrl(imageUrl);
            department.setLogoPublicId(newPublicId);
        } else {
            department.setImageUrl(oldImageUrl);
            department.setLogoPublicId(oldPublicId);
        }

        if (departmentRequest.getDelFlag() != null) {
            department.setDelFlag(departmentRequest.getDelFlag());
        } else {
            department.setDelFlag(oldDelFlag);
        }

        departmentRepository.saveAndFlush(department);

        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        department.setDelFlag(true);

        departmentRepository.save(department);
    }

    @Override
    public byte[] exportExcel(String query, Boolean delFlag) throws IOException {
        Specification<Department> spec = Specification.where(null);

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                );
            });
        }

        if (delFlag != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        List<Department> departments = departmentRepository.findAll(spec);

        ExcelExporter<Department> exporter = excelExporterFactory.create(EXCEL_HEADERS, departments, departmentExcelRowMapper);

        return exporter.exportToExcel();
    }
}