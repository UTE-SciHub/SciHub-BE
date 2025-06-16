package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.AcceptanceStatus;
import vn.thanhtuanle.model.dto.AcceptanceRequestDTO;
import vn.thanhtuanle.model.request.AcceptanceRequest;

import java.io.IOException;
import java.util.List;

public interface AcceptanceRequestService {
    Page<AcceptanceRequestDTO> findAll(Pageable pageable, String topicId, AcceptanceStatus status);

    AcceptanceRequestDTO createAcceptanceRequest(AcceptanceRequest request,
                                                 List<MultipartFile> files,
                                                 String descriptionsJson) throws IOException;

    AcceptanceRequestDTO findById(Integer id);

    @Transactional
    AcceptanceRequestDTO approveAcceptanceRequest(Integer id, MultipartFile decisionFile);

    @Transactional
    AcceptanceRequestDTO rejectAcceptanceRequest(Integer id, String rejectionReason);
}
