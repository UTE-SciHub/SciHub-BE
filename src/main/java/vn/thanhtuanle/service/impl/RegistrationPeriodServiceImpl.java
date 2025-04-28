package vn.thanhtuanle.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.common.enums.RoleType;
import vn.thanhtuanle.common.mapper.ExcelExporterFactory;
import vn.thanhtuanle.common.mapper.RegistrationPeriodExcelRowMapper;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.common.service.ExcelExporter;
import vn.thanhtuanle.common.service.FileUtil;
import vn.thanhtuanle.entity.RegistrationPeriod;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
import vn.thanhtuanle.model.request.UpdateRegistrationRequest;
import vn.thanhtuanle.repository.RegistrationPeriodRepository;
import vn.thanhtuanle.service.RegistrationPeriodService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegistrationPeriodServiceImpl implements RegistrationPeriodService {
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final ModelMapper modelMapper;
    private final ExcelExporterFactory excelExporterFactory;
    @Qualifier("registrationPeriodExcelRowMapper")
    private final RegistrationPeriodExcelRowMapper excelRowMapper;
    private final CloudinaryService cloudinaryService;

    private static final String UPLOAD_DIR = "uploads";
    private static final Path storageFolder = Paths.get(UPLOAD_DIR);

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_FILE_SIZE;

    private final List<String> EXCEL_HEADERS = List.of(
            "Mã đợt đăng ký",
            "Số quyết định",
            "Tiêu đề",
            "Ngày bắt đầu",
            "Ngày kết thúc",
            "Trạng thái"
    );

    private String generateId() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        RegistrationPeriod latestPeriod = registrationPeriodRepository.findTopByOrderByCreatedAtDesc();

        if(latestPeriod == null) {
            return String.format("001_UTE_%s_DK", datePart);
        }

        int count = Integer.parseInt(latestPeriod.getId().split("_")[0]);
        String no = String.format("%03d", count + 1);

        return String.format("%s_UTE_%s_DK", no, datePart);
    }

    @Override
    public Page<RegistrationPeriodDTO> getAll(Pageable pageable, String query, RegistrationPeriodsStatus status, LocalDate startDate, LocalDate endDate) {
        Specification<RegistrationPeriod> spec = createSpecification(query, status, startDate, endDate);

        Page<RegistrationPeriod> registrationPeriods = registrationPeriodRepository.findAll(spec, pageable);
        return registrationPeriods.map(period -> modelMapper.map(period, RegistrationPeriodDTO.class));
    }

    @Override
    @Transactional
    public RegistrationPeriodDTO create(RegistrationPeriodRequest req, MultipartFile decisionFile) throws IOException {
        log.info("Creating registration period: {}", req);
        String id = generateId();

        registrationPeriodRepository.findById(id).ifPresent(p -> {
            throw new AppException(ErrorCode.REGISTRATION_PERIOD_ALREADY_EXISTED);
        });

        RegistrationPeriod registrationPeriod = modelMapper.map(req, RegistrationPeriod.class);
        registrationPeriod.setId(id);
        registrationPeriod.setStatus(RegistrationPeriodsStatus.OPEN);

        String fileUrl = null;
        String publicId = null;
        if (decisionFile != null && !decisionFile.isEmpty()) {
            if (decisionFile.getSize() > MAX_FILE_SIZE.toBytes()) {
                throw new IllegalArgumentException("File vượt quá kích thước tối đa cho phép: " + MAX_FILE_SIZE + " bytes");
            }

            String fileName = decisionFile.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                try {
                    Map result = cloudinaryService.upload(decisionFile);
                    fileUrl = String.valueOf(result.get("url"));
                    publicId = String.valueOf(result.get("public_id"));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload PDF file", e);
                }
            } else {
                throw new IllegalArgumentException("File phải có định dạng PDF (.pdf)");
            }
        } else {
            throw new IllegalArgumentException("File PDF không được để trống hoặc null");
        }

        if (fileUrl != null && publicId != null) {
            registrationPeriod.setDecisionFile(fileUrl);
            registrationPeriod.setFilePublicId(publicId);
        }

        registrationPeriod = registrationPeriodRepository.saveAndFlush(registrationPeriod);

        log.info("Registration period created: {}", registrationPeriod);
        return modelMapper.map(registrationPeriod, RegistrationPeriodDTO.class);
    }

    private String saveFile(MultipartFile file) {
        log.info("Uploading file: {}", file.getOriginalFilename());
        if (file == null || file.isEmpty()) {
            log.error("File not provided");
            throw new AppException(ErrorCode.FILE_NOT_PROVIDED);
        }

        try {
            String fileName = file.getOriginalFilename();
            if (!Files.exists(storageFolder)) {
                Files.createDirectories(storageFolder);
            }

            try (InputStream inputStream = file.getInputStream()) {
                assert fileName != null;
                Path filePath = storageFolder.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            return String.format("%s/%s", UPLOAD_DIR, file.getOriginalFilename());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    @Override
    @Transactional
    public void closeMultiple(List<String> ids) {
        log.info("Closing registration periods: {}", ids);
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Danh sách mã đợt đăng ký không được để trống");
        }

        List<RegistrationPeriod> periods = registrationPeriodRepository.findAllById(ids);
        if (periods.size() != ids.size()) {
            Set<String> foundIds = periods.stream()
                    .map(RegistrationPeriod::getId)
                    .collect(Collectors.toSet());
            Set<String> missingIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            log.warn("Không tìm thấy đợt đăng ký có id: {}", missingIds);
            throw new EntityNotFoundException("Không tìm thấy đợt đăng ký có id: " + missingIds);
        }

        periods.forEach(period -> {
            if (period.getStatus() != RegistrationPeriodsStatus.CLOSED) {
                period.setStatus(RegistrationPeriodsStatus.CLOSED);
            }
        });

        log.info("Updating status of registration periods: {}", ids);
        registrationPeriodRepository.saveAll(periods);
    }

    @Transactional
    @Override
    public RegistrationPeriodDTO update(String id, UpdateRegistrationRequest req, MultipartFile decisionFile) throws IOException {
        log.info("Updating registration period with id: {}", id);

        RegistrationPeriod registrationPeriod = registrationPeriodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REGISTRATION_PERIOD_NOT_FOUND));

        String oldPublicId = registrationPeriod.getFilePublicId();
        String oldDecisionFile = registrationPeriod.getDecisionFile();
        RegistrationPeriodsStatus oldStatus = registrationPeriod.getStatus();
        modelMapper.map(req, registrationPeriod);

        String fileUrl = null;
        String publicId = null;
        if (decisionFile != null && !decisionFile.isEmpty()) {
            if (decisionFile.getSize() > MAX_FILE_SIZE.toBytes()) {
                throw new IllegalArgumentException("File vượt quá kích thước tối đa cho phép: " + MAX_FILE_SIZE + " bytes");
            }

            String fileName = decisionFile.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                try {
                    Map result = cloudinaryService.upload(decisionFile);
                    fileUrl = String.valueOf(result.get("url"));
                    publicId = String.valueOf(result.get("public_id"));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload PDF file", e);
                }
            } else {
                throw new IllegalArgumentException("File phải có định dạng PDF (.pdf)");
            }
        }

        if (fileUrl != null && publicId != null) {
            registrationPeriod.setDecisionFile(fileUrl);
            registrationPeriod.setFilePublicId(publicId);
        } else {
            registrationPeriod.setDecisionFile(oldDecisionFile);
            registrationPeriod.setFilePublicId(oldPublicId);
        }

        if (req.getStatus() != null) {
            registrationPeriod.setStatus(req.getStatus());
        } else {
            registrationPeriod.setStatus(oldStatus);
        }

        registrationPeriod = registrationPeriodRepository.save(registrationPeriod);

        log.info("Registration period updated: {}", registrationPeriod);
        return modelMapper.map(registrationPeriod, RegistrationPeriodDTO.class);
    }

    @Override
    public byte[] exportExcel(String query, RegistrationPeriodsStatus status, String sort, String order, LocalDate startDate, LocalDate endDate) {
        try {
            Specification<RegistrationPeriod> spec = createSpecification(query, status, startDate, endDate);
            Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sorting = Sort.by(direction, sort);

            List<RegistrationPeriod> registrations = registrationPeriodRepository.findAll(spec, sorting);

            ExcelExporter<RegistrationPeriod> exporter = excelExporterFactory.create(
                    EXCEL_HEADERS, registrations, excelRowMapper
            );

            return exporter.exportToExcel();
        } catch (IOException e) {
            throw new AppException(ErrorCode.EXCEL_EXPORT_ERROR);
        }
    }

    @Override
    public RegistrationPeriodDTO getById(String id) {
        RegistrationPeriod registrationPeriod = registrationPeriodRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Role not found with name: {}", RoleType.STUDENT);
                    return new ResourceNotFoundException("Registraton", "id", id);
                });

        return modelMapper.map(registrationPeriod, RegistrationPeriodDTO.class);
    }

    private Specification<RegistrationPeriod> createSpecification(String query, RegistrationPeriodsStatus status, LocalDate startDate, LocalDate endDate) {
        Specification<RegistrationPeriod> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }

        if (startDate != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }

        if (endDate != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
        }

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("id")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                );
            });
        }

        return spec;
    }
}
