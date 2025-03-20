package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.entity.RegistrationPeriod;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
import vn.thanhtuanle.repository.RegistrationPeriodRepository;
import vn.thanhtuanle.service.RegistrationPeriodService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
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
    public RegistrationPeriodDTO create(RegistrationPeriodRequest req) {
        String id = generateId();

        registrationPeriodRepository.findById(id).ifPresent(p -> {
            throw new AppException(ErrorCode.REGISTRATION_PERIOD_ALREADY_EXISTED);
        });

        RegistrationPeriod registrationPeriod = modelMapper.map(req, RegistrationPeriod.class);
        registrationPeriod.setId(id);
        registrationPeriod.setStatus(RegistrationPeriodsStatus.OPEN);

        registrationPeriod = registrationPeriodRepository.saveAndFlush(registrationPeriod);

        return modelMapper.map(registrationPeriod, RegistrationPeriodDTO.class);
    }
}
