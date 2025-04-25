package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.model.dto.ResearchTypeDTO;

public interface ResearchTypeService {
    Page<ResearchTypeDTO> findAll(Pageable pageable, String query, Boolean delFlag);

    @Transactional
    ResearchTypeDTO createResearchType(ResearchTypeDTO req);

    ResearchTypeDTO getResearchTypeById(Integer id);

    @Transactional
    ResearchTypeDTO updateResearchType(Integer id, ResearchTypeDTO req);

    void deleteResearchType(Integer id);

    @Transactional
    ResearchTypeDTO updateResearchTypeStatus(Integer id, Boolean isActive);
}
