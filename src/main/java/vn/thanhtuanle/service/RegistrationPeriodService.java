package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;

public interface RegistrationPeriodService {
    Page<RegistrationPeriodDTO> getAll(Pageable pageable, String query);

    RegistrationPeriodDTO create(RegistrationPeriodRequest req, MultipartFile decisionFile);
}
