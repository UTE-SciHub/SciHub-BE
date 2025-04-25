package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.ResearchFieldDTO;
import vn.thanhtuanle.entity.ResearchField;
import vn.thanhtuanle.repository.ResearchFieldRepository;
import vn.thanhtuanle.service.ResearchFieldService;

@Service
@RequiredArgsConstructor
public class ResearchFieldServiceImpl implements ResearchFieldService {

    private final ResearchFieldRepository researchFieldRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<ResearchFieldDTO> findAll(Pageable pageable, String query, Boolean delFlag) {
        Specification<ResearchField> spec = Specification.where(null);

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern)
                );
            });
        }

        if (delFlag != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("delFlag"), delFlag));
        }

        Page<ResearchField> researchFields = researchFieldRepository.findAll(spec, pageable);
        return researchFields.map(r -> modelMapper.map(r, ResearchFieldDTO.class));
    }

    @Transactional
    @Override
    public ResearchFieldDTO createResearchField(ResearchFieldDTO req) {
        ResearchField researchField = modelMapper.map(req, ResearchField.class);

        return modelMapper.map(researchFieldRepository.save(researchField), ResearchFieldDTO.class);
    }

    @Override
    public ResearchFieldDTO getResearchFieldById(Integer id) {
        ResearchField ResearchField = researchFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", id));

        return modelMapper.map(ResearchField, ResearchFieldDTO.class);
    }

    @Transactional
    @Override
    public ResearchFieldDTO updateResearchField(Integer id, ResearchFieldDTO req) {
        ResearchField researchField = researchFieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", id));

        Boolean oldDelFlag = researchField.getDelFlag();
        modelMapper.map(req, researchField);

        researchField.setId(id);

        if (req.getDelFlag() != null) {
            researchField.setDelFlag(req.getDelFlag());
        } else {
            researchField.setDelFlag(oldDelFlag);
        }

        researchFieldRepository.saveAndFlush(researchField);
        return modelMapper.map(researchField, ResearchFieldDTO.class);
    }

    @Override
    public void deleteResearchField(Integer id) {
        ResearchField researchField = researchFieldRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", id));
        researchField.setDelFlag(true);

        researchFieldRepository.save(researchField);
    }

    @Override
    @Transactional
    public ResearchFieldDTO updateResearchFieldStatus(Integer id, Boolean isActive) {
        ResearchField researchField = researchFieldRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", id));

        researchField.setDelFlag(isActive);
        researchFieldRepository.save(researchField);

        return modelMapper.map(researchField, ResearchFieldDTO.class);
    }
}