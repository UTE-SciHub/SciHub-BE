package vn.thanhtuanle.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.service.DepartmentService;
import vn.thanhtuanle.model.request.DepartmentRequest;
import vn.thanhtuanle.entity.Department;
import vn.thanhtuanle.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<DepartmentDTO> findAll(Pageable pageable, String query) {
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

        Page<Department> departments = departmentRepository.findAll(spec, pageable);
        return departments.map(department -> modelMapper.map(department, DepartmentDTO.class));
    }

    @Override
    public DepartmentDTO createDepartment(DepartmentRequest departmentRequest) {
        Department department = Department.builder()
                .name(departmentRequest.getName())
                .description(departmentRequest.getDescription())
                .imageUrl(departmentRequest.getImageUrl())
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
    public DepartmentDTO updateDepartment(Integer id, DepartmentDTO departmentRequest) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));

        department.setName(departmentRequest.getName());
        department.setDescription(departmentRequest.getDescription());
        department.setImageUrl(departmentRequest.getImageUrl());
        department.setPhoneNumber(departmentRequest.getPhoneNumber());
        department.setEmail(departmentRequest.getEmail());
        department.setDelFlag(departmentRequest.getDelFlag());

        departmentRepository.save(department);

        return modelMapper.map(department, DepartmentDTO.class);
    }

    @Override
    public void deleteDepartment(Integer id) {
        Department department = departmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
        department.setDelFlag(true);

        departmentRepository.save(department);
    }
}