package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
import vn.thanhtuanle.model.request.UpdateRegistrationRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface RegistrationPeriodService {
    Page<RegistrationPeriodDTO> getAll(Pageable pageable, String query, RegistrationPeriodsStatus status, LocalDate startDate, LocalDate endDate, Integer year);

    RegistrationPeriodDTO create(RegistrationPeriodRequest req, MultipartFile decisionFile) throws IOException;

    void closeMultiple(List<String> ids);

    @Transactional
    RegistrationPeriodDTO update(String id, UpdateRegistrationRequest req, MultipartFile decisionFile) throws IOException;

    byte[] exportExcel(String query, RegistrationPeriodsStatus status, String sort, String order, LocalDate startDate, LocalDate endDate, Integer year);

    RegistrationPeriodDTO getById(String id);
}
