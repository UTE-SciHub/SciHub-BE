package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.common.enums.RoleType;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.common.mapper.ExcelExporterFactory;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.common.service.ExcelExporter;
import vn.thanhtuanle.common.service.ExcelRowMapper;
import vn.thanhtuanle.entity.Role;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.model.request.UserRequest;
import vn.thanhtuanle.repository.RoleRepository;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.DepartmentService;
import vn.thanhtuanle.model.request.DepartmentRequest;
import vn.thanhtuanle.entity.Department;
import vn.thanhtuanle.repository.DepartmentRepository;
import vn.thanhtuanle.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final List<String> EXCEL_HEADERS = List.of(
            "ID",
            "Tên phòng ban",
            "Số điện thoại",
            "Email",
            "Trạng thái"
    );

    @Value("${application.user.password.default}")
    private String USER_PASSWORD_DEFAULT;

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

        Department savedDepartment = departmentRepository.save(department);

        Role role = roleRepository.findByName(RoleType.BCNKHOA)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "type", RoleType.BCNKHOA.name()));
        User user = User.builder()
                .email(departmentRequest.getEmail())
                .name(departmentRequest.getName())
                .phoneNumber(departmentRequest.getPhoneNumber())
                .roles(Set.of(role))
                .password(USER_PASSWORD_DEFAULT)
                .imageUrl(imageUrl)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);

        return modelMapper.map(savedDepartment, DepartmentDTO.class);
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
        User user = userRepository.findByEmail(departmentRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", departmentRequest.getEmail()));

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
            user.setImageUrl(imageUrl);
        } else {
            department.setImageUrl(oldImageUrl);
            department.setLogoPublicId(oldPublicId);
            user.setImageUrl(oldImageUrl);
        }

        if (departmentRequest.getDelFlag() != null) {
            department.setDelFlag(departmentRequest.getDelFlag());
        } else {
            department.setDelFlag(oldDelFlag);
        }

        departmentRepository.saveAndFlush(department);

        user.setEmail(departmentRequest.getEmail());
        user.setName(departmentRequest.getName());
        user.setPhoneNumber(departmentRequest.getPhoneNumber());

        if(departmentRequest.getDelFlag()) {
            user.setStatus(UserStatus.BLOCKED);
        }

        userRepository.save(user);
        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        department.setDelFlag(true);
        departmentRepository.save(department);

        User user = userRepository.findByEmail(department.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", department.getEmail()));
        user.setStatus(UserStatus.BLOCKED);
        userRepository.save(user);
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