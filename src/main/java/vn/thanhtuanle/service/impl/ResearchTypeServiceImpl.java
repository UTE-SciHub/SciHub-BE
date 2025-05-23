package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.entity.ResearchType;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.ResearchTypeDTO;
import vn.thanhtuanle.repository.ResearchTypeRepository;
import vn.thanhtuanle.service.ResearchTypeService;

@Service
@RequiredArgsConstructor
public class ResearchTypeServiceImpl implements ResearchTypeService {

    private final ResearchTypeRepository researchTypeRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<ResearchTypeDTO> findAll(Pageable pageable, String query, Boolean delFlag) {
        Specification<ResearchType> spec = Specification.where(null);

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

        Page<ResearchType> ResearchTypes = researchTypeRepository.findAll(spec, pageable);
        return ResearchTypes.map(ResearchType -> modelMapper.map(ResearchType, ResearchTypeDTO.class));
    }

    @Transactional
    @Override
    public ResearchTypeDTO createResearchType(ResearchTypeDTO req) {
        ResearchType researchType = modelMapper.map(req, ResearchType.class);

        return modelMapper.map(researchTypeRepository.save(researchType), ResearchTypeDTO.class);
    }

    @Override
    public ResearchTypeDTO getResearchTypeById(Integer id) {
        ResearchType ResearchType = researchTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ResearchType", "id", id));

        return modelMapper.map(ResearchType, ResearchTypeDTO.class);
    }

    @Transactional
    @Override
    public ResearchTypeDTO updateResearchType(Integer id, ResearchTypeDTO req) {
        ResearchType researchType = researchTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResearchType", "id", id));

        Boolean oldDelFlag = researchType.getDelFlag();
        modelMapper.map(req, researchType);

        researchType.setId(id);

        if (req.getDelFlag() != null) {
            researchType.setDelFlag(req.getDelFlag());
        } else {
            researchType.setDelFlag(oldDelFlag);
        }

        researchTypeRepository.saveAndFlush(researchType);
        return modelMapper.map(researchType, ResearchTypeDTO.class);
    }

    @Override
    public void deleteResearchType(Integer id) {
        ResearchType researchType = researchTypeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ResearchType", "id", id));
        researchType.setDelFlag(true);

        researchTypeRepository.save(researchType);
    }

    @Transactional
    @Override
    public ResearchTypeDTO updateResearchTypeStatus(Integer id, Boolean isActive) {
        ResearchType researchType = researchTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ResearchType", "id", id));

        researchType.setDelFlag(isActive);
        researchTypeRepository.save(researchType);

        return modelMapper.map(researchType, ResearchTypeDTO.class);
    }
}