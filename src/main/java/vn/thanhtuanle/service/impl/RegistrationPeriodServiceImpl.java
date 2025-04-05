package vn.thanhtuanle.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.common.enums.RoleType;
import vn.thanhtuanle.common.mapper.ExcelExporterFactory;
import vn.thanhtuanle.common.mapper.RegistrationPeriodExcelRowMapper;
import vn.thanhtuanle.common.service.ExcelExporter;
import vn.thanhtuanle.entity.RegistrationPeriod;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
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
    public RegistrationPeriodDTO create(RegistrationPeriodRequest req, MultipartFile decisionFile) {
        log.info("Creating registration period: {}", req);
        String id = generateId();

        registrationPeriodRepository.findById(id).ifPresent(p -> {
            throw new AppException(ErrorCode.REGISTRATION_PERIOD_ALREADY_EXISTED);
        });

        RegistrationPeriod registrationPeriod = modelMapper.map(req, RegistrationPeriod.class);
        registrationPeriod.setId(id);
        registrationPeriod.setStatus(RegistrationPeriodsStatus.OPEN);

        String decisionFilePath = saveFile(decisionFile);
        registrationPeriod.setDecisionFile(decisionFilePath);

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
            Path uploadPath = Paths.get("uploads");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                log.error(e.getMessage());
            }

            return "uploads/" + fileName;
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
    public RegistrationPeriodDTO update(String id, RegistrationPeriodRequest req, MultipartFile decisionFile) {
        log.info("Updating registration period with id: {}", id);

        RegistrationPeriod registrationPeriod = registrationPeriodRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REGISTRATION_PERIOD_NOT_FOUND));

        modelMapper.map(req, registrationPeriod);

        if (decisionFile != null && !decisionFile.isEmpty()) {
            String decisionFilePath = saveFile(decisionFile);
            registrationPeriod.setDecisionFile(decisionFilePath);
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

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query1, cb) ->
                    cb.and(
                            cb.greaterThanOrEqualTo(root.get("startDate"), startDate),
                            cb.lessThanOrEqualTo(root.get("endDate"), endDate)
                    ));
        } else if (startDate != null) {
            spec = spec.and((root, query1, cb) ->
                    cb.equal(root.get("startDate"), startDate));
        } else if (endDate != null) {
            spec = spec.and((root, query1, cb) ->
                    cb.equal(root.get("endDate"), endDate));
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
