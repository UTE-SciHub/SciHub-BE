package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.entity.RegistrationPeriod;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
import vn.thanhtuanle.repository.RegistrationPeriodRepository;
import vn.thanhtuanle.service.RegistrationPeriodService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Log4j2
public class RegistrationPeriodServiceImpl implements RegistrationPeriodService {
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final ModelMapper modelMapper;

    private String generateId() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        RegistrationPeriod latestPeriod = registrationPeriodRepository.findTopByOrderByCreatedAtDesc();

        int count = Integer.parseInt(latestPeriod.getId().split("_")[0]);
        String no = String.format("%03d", count + 1);

        return String.format("%s_UTE_%s_DK", no, datePart);
    }

    @Override
    public Page<RegistrationPeriodDTO> getAll(Pageable pageable, String query) {
        Page<RegistrationPeriod> registrationPeriods;

        if (query != null && !query.trim().isEmpty()) {
            Specification<RegistrationPeriod> spec = (root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("id")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern)
                );
            };

            registrationPeriods = registrationPeriodRepository.findAll(spec, pageable);
        } else {
            registrationPeriods = registrationPeriodRepository.findAll(pageable);
        }

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
}
