package vn.thanhtuanle.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ContractStatus;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.entity.Contract;
import vn.thanhtuanle.entity.Topic;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.ContractDTO;
import vn.thanhtuanle.model.request.ContractFilterParams;
import vn.thanhtuanle.model.request.ContractRequest;
import vn.thanhtuanle.repository.ContractRepository;
import vn.thanhtuanle.repository.TopicRepository;
import vn.thanhtuanle.service.ContractService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final TopicRepository topicRepository;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_FILE_SIZE;

    @Override
    public ContractDTO createContract(ContractRequest req, MultipartFile contractFile) throws IOException {
        log.info("Creating contract with details: {}", req.getContractDetails());

        Contract contract = Contract.builder()
                .code(req.getCode())
                .name(req.getName())
                .contractDetails(req.getContractDetails())
                .signedDate(req.getSignedDate())
                .status(req.getStatus() != null ? req.getStatus() : ContractStatus.PENDING)
                .delFlag(false)
                .build();

        String fileUrl = null;
        if(contractFile != null && !contractFile.isEmpty()) {
            if (contractFile.getSize() > MAX_FILE_SIZE.toBytes()) {
                throw new IllegalArgumentException("File size exceeds the maximum limit of " + MAX_FILE_SIZE);
            }

            log.info("Uploading contract file: {}", contractFile.getOriginalFilename());
            Map result = cloudinaryService.upload(contractFile);
            fileUrl = String.valueOf(result.get("url"));

            contract.setContractPath(fileUrl);
            log.info("Contract file uploaded to: {}", fileUrl);
        }

        contract.setTopic(topicRepository.findById(req.getIdTopic())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "ID", req.getIdTopic())));

        Contract savedContract = contractRepository.save(contract);
        log.info("Contract created with ID: {}", savedContract.getId());

        return modelMapper.map(savedContract, ContractDTO.class);
    }

    @Override
    public ContractDTO getContractById(Integer id) {
        log.info("Fetching contract with ID: {}", id);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "ID", id));
        return modelMapper.map(contract, ContractDTO.class);
    }

    @Override
    public void deleteContract(Integer id) {
        log.info("Deleting contract with ID: {}", id);
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "ID", id));

        contract.setDelFlag(true);
        contract.setStatus(ContractStatus.CANCELLED);
        contractRepository.save(contract);
        log.info("Contract with ID: {} deleted successfully", id);
    }

    @Override
    public Page<ContractDTO> findAll(ContractFilterParams params) {
        log.info("Fetching contracts with filters: {}", params);

        // Handle default values
        int page = params.getP() != null ? params.getP() : 0;
        int size = params.getS() != null ? params.getS() : 10;
        String sortBy = params.getSort() != null ? params.getSort() : "createdDate";
        String orderDir = params.getOrder() != null && params.getOrder().equalsIgnoreCase("asc") ?
                "asc" : "desc";

        // Create sort object
        Sort sorting = Sort.by(orderDir.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC, sortBy);

        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sorting);

        // Create specifications
        Specification<Contract> spec = Specification.where(null);

        // Contract status filter
        if (params.getStatus() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), params.getStatus()));
        }

        // Topic status filter
        if (params.getTopicStatus() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("topic").get("status"), params.getTopicStatus()));
        }

        // Research type filter
        if (params.getResearchTypeId() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("topic").get("researchType").get("id"),
                            params.getResearchTypeId()));
        }

        // Research field filter
        if (params.getResearchFieldId() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("topic").get("researchField").get("id"),
                            params.getResearchFieldId()));
        }

        // Registration period filter
        if (params.getRegistrationPeriodId() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("topic").get("registrationPeriod").get("id"),
                            params.getRegistrationPeriodId()));
        }

        // Department filter
        if (params.getDepartmentId() != null && !params.getDepartmentId().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("topic").get("department").get("id"),
                            params.getDepartmentId()));
        }

        // Signed date range filter
        if (params.getSignedDateFrom() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("signedDate"),
                            params.getSignedDateFrom()));
        }

        if (params.getSignedDateTo() != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("signedDate"),
                            params.getSignedDateTo()));
        }

        // Query filter (search in contract details, code, name and topic name)
        if (params.getQ() != null && !params.getQ().trim().isEmpty()) {
            String searchPattern = "%" + params.getQ().toLowerCase() + "%";
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("code")), searchPattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("topic").get("vietnameseName")),
                                    searchPattern)
                    ));
        }

        // Deleted flag filter
        boolean deletedFilter = params.getDelFlag() != null ? params.getDelFlag() : false;
        spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("delFlag"), deletedFilter));

        // Execute query
        Page<Contract> contractsPage = contractRepository.findAll(spec, pageable);

        log.info("Found {} contracts, page {} of {}",
                contractsPage.getNumberOfElements(),
                contractsPage.getNumber() + 1,
                contractsPage.getTotalPages());

        // Map to DTOs
        return contractsPage.map(contract -> modelMapper.map(contract, ContractDTO.class));
    }

    @Override
    public List<ContractDTO> findAllContractsByTopicId(String topicId) {
        log.info("Fetching all contracts for topic ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "ID", topicId));
        List<Contract> contracts = contractRepository.findAllByTopic(topic);
        log.info("Found {} contracts for topic ID: {}", contracts.size(), topicId);
        return contracts.stream()
                .map(contract -> modelMapper.map(contract, ContractDTO.class))
                .toList();
    }

    @Override
    public ContractDTO update(ContractRequest req, MultipartFile file) throws IOException {
        log.info("Updating contract with ID: {}", req.getId());

        Contract contract = contractRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "ID", req.getId()));

        contract.setCode(req.getCode());
        contract.setName(req.getName());
        contract.setContractDetails(req.getContractDetails());
        contract.setSignedDate(req.getSignedDate());
        contract.setStatus(req.getStatus() != null ? req.getStatus() : ContractStatus.PENDING);
        contract.setTopic(topicRepository.findById(req.getIdTopic())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "ID", req.getIdTopic())));

        if (file != null && !file.isEmpty()) {
            if (file.getSize() > MAX_FILE_SIZE.toBytes()) {
                throw new IllegalArgumentException("File size exceeds the maximum limit of " + MAX_FILE_SIZE);
            }

            log.info("Uploading new contract file: {}", file.getOriginalFilename());
            Map result = cloudinaryService.upload(file);
            String fileUrl = String.valueOf(result.get("url"));
            contract.setContractPath(fileUrl);
            log.info("New contract file uploaded to: {}", fileUrl);
        }

        Contract updatedContract = contractRepository.save(contract);
        log.info("Contract with ID: {} updated successfully", updatedContract.getId());

        return modelMapper.map(updatedContract, ContractDTO.class);
    }
}
