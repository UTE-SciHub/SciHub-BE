package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.ErrorType;
import vn.thanhtuanle.common.enums.RoleType;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.common.mapper.ExcelExporterFactory;
import vn.thanhtuanle.common.mapper.UserExcelRowMapper;
import vn.thanhtuanle.common.service.*;
import vn.thanhtuanle.entity.Role;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.ImportUserDTO;
import vn.thanhtuanle.model.request.UserRequest;
import vn.thanhtuanle.model.request.MultipleCreateUserRequest;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.repository.RoleRepository;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.UserService;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final ExcelExporterFactory excelExporterFactory;
    @Qualifier("userExcelRowMapper")
    private final ExcelRowMapper<User> userExcelRowMapper;
    private final Validator validator;
    private final CloudinaryService cloudinaryService;

    @Value("${application.user.password.default}")
    private String USER_PASSWORD_DEFAULT;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_FILE_SIZE;

    private final List<String> EXCEL_HEADERS = List.of(
            "Mã sinh viên/giáo viên",
            "Email", "Tên",
            "Số điện thoại",
            "Trạng thái",
            "Giới tính",
            "Ngày sinh",
            "Đăng nhập lần cuối",
            "Người tạo",
            "Ngày tạo");

    @Transactional
    @Override
    public UserDTO create(UserRequest req, MultipartFile avatar) throws IOException {
        log.info("Create user with email: {}", req.getEmail());
        boolean exists = isUserExist(req.getEmail());
        if (exists) {
            log.warn("User already exists with email: {}", req.getEmail());
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String id = req.getEmail().split("@")[0];

        Role role = roleRepository.findByName(RoleType.STUDENT)
                .orElseThrow(() -> {
                    log.warn("Role not found with name: {}", RoleType.STUDENT);
                    return new ResourceNotFoundException("Role", "name", RoleType.STUDENT.name());
                });

        String avatarUrl = null;
        String publicId = null;
        if (avatar != null && !avatar.isEmpty()) {
            if (avatar.getSize() > MAX_FILE_SIZE.toBytes()) {
                throw new IllegalArgumentException("File vượt quá kích thước tối đa cho phép: " + MAX_FILE_SIZE + " bytes");
            }

            try {
                Map result = cloudinaryService.upload(avatar);
                avatarUrl = String.valueOf(result.get("url"));
                publicId = String.valueOf(result.get("public_id"));
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        User user = modelMapper.map(req, User.class);
        user.setId(id);
        user.setPassword(encoder.encode(USER_PASSWORD_DEFAULT));
        user.setRoles(Set.of(role));
        user.setStatus(UserStatus.ACTIVE);
        user.setImageUrl(avatarUrl);
        user.setImagePublicId(publicId);

        User savedUser = userRepository.save(user);

        log.info("User created with email: {}", req.getEmail());

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO getCurrentUser(String token) {
        String email = jwtService.extractUsername(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });

        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional
    @Override
    public Map<String, Object> multipleCreate(MultipleCreateUserRequest req) {
        log.info("Multiple create user with list id: {}", req.getIds());

        Role role = roleRepository.findByName(req.getRole())
                .orElseThrow(() -> {
                    log.warn("Role not found with name: {}", req.getRole());
                    return new ResourceNotFoundException("Role", "name", req.getRole().name());
                });

        String emailDomain = (role.getName() == RoleType.TEACHER) ? "@ute.udn.vn" : "@sv.ute.udn.vn";

        List<String> duplicateIds = new ArrayList<>();
        List<User> newUsers = req.getIds().stream()
                .filter(id -> {
                    String email = String.format("%s%s%s%s", req.getPrefix(), id, req.getSuffix(), emailDomain);
                    boolean exists = isUserExist(email);
                    if (exists) {
                        duplicateIds.add(email);
                    }
                    return !exists;
                })
                .map(id -> User.builder()
                        .id(String.valueOf(id))
                        .email(String.format("%s%s%s%s", req.getPrefix(), id, req.getSuffix(), emailDomain))
                        .password(encoder.encode(USER_PASSWORD_DEFAULT))
                        .roles(Set.of(role))
                        .status(UserStatus.ACTIVE)
                        .build())
                .toList();

        List<User> savedUsers = userRepository.saveAll(newUsers);
        log.info("Created {} new users, {} duplicate users found", savedUsers.size(), duplicateIds.size());

        List<UserDTO> userResponses = savedUsers.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("createdCount", savedUsers.size());
        response.put("duplicateCount", duplicateIds.size());
        response.put("duplicateIds", duplicateIds);
        response.put("createdUsers", userResponses);

        return response;
    }

    @Override
    public Page<UserDTO> getAll(Pageable pageable, String query, UserStatus status) {
        Specification<User> spec = createSpecification(query, status);

        Page<User> users = userRepository.findAll(spec, pageable);
        return users.map(user -> modelMapper.map(user, UserDTO.class));
    }

    public boolean isUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public byte[] exportExcel(String query, UserStatus status, String sort, String order) {
        try {
            Specification<User> spec = createSpecification(query, status);
            Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sorting = Sort.by(direction, sort);

            List<User> users = userRepository.findAll(spec, sorting);

            ExcelExporter<User> exporter = excelExporterFactory.create(EXCEL_HEADERS, users, userExcelRowMapper);

            return exporter.exportToExcel();
        } catch (IOException e) {
            throw new AppException(ErrorCode.EXCEL_EXPORT_ERROR);
        }
    }

    private Specification<User> createSpecification(String query, UserStatus status) {
        Specification<User> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("id")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchPattern)
                );
            });
        }

        return spec;
    }

    @Override
    public Map<String, Object> saveUsers(List<ImportUserDTO> validUsers) {
        return processUsers(validUsers, true);
    }

    @Override
    public Map<String, Object> importUsers(MultipartFile file) throws IOException {
        List<ImportUserDTO> users = readExcel(file);

        return processUsers(users, false);
    }

    @Override
    public void resetPassword(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

//        generate a new random password
//        send email to user with the new password
        String defaultPassword = encoder.encode(USER_PASSWORD_DEFAULT);
        user.setPassword(defaultPassword);
        userRepository.save(user);
    }

    @Override
    public UserDTO changeStatus(String id, UserStatus status) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setStatus(status);
        userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    private List<ImportUserDTO> readExcel(MultipartFile file) throws IOException {
        List<ImportUserDTO> users = new ArrayList<>();
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                users.add(ImportUserDTO.builder()
                                .id(getCellValue(row.getCell(0)))
                                .email(getCellValue(row.getCell(1)))
                                .name(getCellValue(row.getCell(2)))
                                .phone(getCellValue(row.getCell(3)))
                        .build());
            }
        }
        return users;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    private Map<String, Object> processUsers(List<ImportUserDTO> users, boolean save) {
        List<User> newUsers = new ArrayList<>();
        List<ImportUserDTO> validRecords = new ArrayList<>();
        List<ImportUserDTO> validationErrors = new ArrayList<>();
        List<ImportUserDTO> duplicateErrors = new ArrayList<>();

        for (ImportUserDTO dto : users) {
            String validationError = validateUser(dto);

            if (validationError != null) {
                dto.setType(ErrorType.VALIDATION);
                dto.setErrorField(getValidationField(dto));
                validationErrors.add(dto);
            } else {
                String duplicateField = getDuplicateField(dto);
                if (duplicateField != null) {
                    dto.setType(ErrorType.DUPLICATE);
                    dto.setErrorField(duplicateField);
                    duplicateErrors.add(dto);
                } else {
                    validRecords.add(dto);
                    newUsers.add(User.builder()
                            .id(dto.getId())
                            .email(dto.getEmail())
                            .name(dto.getName())
                            .phoneNumber(dto.getPhone())
                            .password(encoder.encode(USER_PASSWORD_DEFAULT))
                            .status(UserStatus.ACTIVE)
                            .roles(Set.of(roleRepository.findByName(RoleType.STUDENT)
                                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleType.STUDENT.name()))))
                            .build());
                }
            }
        }

        if (save) {
            userRepository.saveAll(newUsers);
        }

        return Map.of(
                "successCount", validRecords.size(),
                "errorCount", validationErrors.size() + duplicateErrors.size(),
                "validRecords", validRecords,
                "validationErrors", validationErrors,
                "duplicateErrors", duplicateErrors
        );
    }

    private String validateUser(ImportUserDTO dto) {
        Set<ConstraintViolation<ImportUserDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            return violations.iterator().next().getMessage();
        }
        return null;
    }

    private String getValidationField(ImportUserDTO dto) {
        Set<ConstraintViolation<ImportUserDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            return violations.iterator().next().getPropertyPath().toString();
        }
        return "Unknown";
    }

    private String getDuplicateField(ImportUserDTO dto) {
        if (userRepository.existsById(dto.getId())) {
            return "id";
        } else if (userRepository.existsByEmail(dto.getEmail())) {
            return "email";
        } else if (userRepository.existsByPhoneNumber(dto.getPhone())) {
            return "phone";
        }
        return null;
    }
}
