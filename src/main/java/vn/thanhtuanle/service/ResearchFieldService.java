package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.model.dto.ResearchFieldDTO;

public interface ResearchFieldService {
    Page<ResearchFieldDTO> findAll(Pageable pageable, String query);

    @Transactional
    ResearchFieldDTO createResearchField(ResearchFieldDTO req);

    ResearchFieldDTO getResearchFieldById(Integer id);

    @Transactional
    ResearchFieldDTO updateResearchField(Integer id, ResearchFieldDTO req);

    void deleteResearchField(Integer id);

    @Transactional
    ResearchFieldDTO updateResearchFieldStatus(Integer id, Boolean isActive);
}
