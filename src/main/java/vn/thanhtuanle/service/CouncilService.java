package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.model.dto.CouncilDTO;
import vn.thanhtuanle.model.request.CreateCouncilRequest;

import java.time.LocalDate;
import java.util.List;

public interface CouncilService {
    Page<CouncilDTO> findAll(Pageable pageable, String query, String type, String status, Boolean delFlag, Boolean isAdmin);

    CouncilDTO createCouncil(CreateCouncilRequest req);

    CouncilDTO getCouncilById(Long id);

    CouncilDTO updateCouncil(Long id, CreateCouncilRequest req);

    void softDeleteCouncil(Long id);

    byte[] exportExcel(String query, String type, String status, String sort, String order, LocalDate startDate, LocalDate endDate, Boolean delFlag, Boolean isAdmin, Boolean includeMembers, Boolean includeTopics, List<Long> selectedIds);
}
