package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;

import java.util.List;

public interface RegistrationPeriodService {
    Page<RegistrationPeriodDTO> getAll(Pageable pageable, String query, RegistrationPeriodsStatus status);

    RegistrationPeriodDTO create(RegistrationPeriodRequest req, MultipartFile decisionFile);

    void closeMultiple(List<String> ids);
}
