package vn.thanhtuanle.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.entity.*;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.AttachedDocumentDTO;
import vn.thanhtuanle.model.request.TopicCreateRequest;
import vn.thanhtuanle.repository.DepartmentRepository;
import vn.thanhtuanle.repository.ResearchFieldRepository;
import vn.thanhtuanle.repository.ResearchTypeRepository;
import vn.thanhtuanle.repository.TopicRepository;
import vn.thanhtuanle.service.TopicService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final ModelMapper modelMapper;
    private final TopicRepository topicRepository;
    private final DepartmentRepository departmentRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final ResearchTypeRepository researchTypeRepository;
//    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    private final CloudinaryService cloudinaryService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_FILE_SIZE;

    @Override
    public Page<TopicCreateRequest> getAll(Pageable pageable, String query, TopicStatus status, Integer departmentId) {
        log.info("Fetching all topics with query: {}", query);
        Specification<Topic> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("vietnameseName")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("englishName")), searchPattern)
                );
            });
        }

        if (departmentId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("department").get("id"), departmentId));
        }

        Page<Topic> topics = topicRepository.findAll(spec, pageable);

        log.info("Found {} topics", topics.getTotalElements());
        return topics.map(t -> modelMapper.map(t, TopicCreateRequest.class));
    }

    @Override
    public TopicCreateRequest findById(String id) {
        log.info("Fetching topic with ID: {}", id);
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic", "id", id));
        return modelMapper.map(topic, TopicCreateRequest.class);
    }

    @Override
    @Transactional
    public TopicCreateRequest createTopic(TopicCreateRequest req) throws JsonProcessingException {
        if(topicRepository.existsByTopicCode(req.getTopicCode())) {
            throw new AppException(ErrorCode.TOPIC_CODE_EXISTS);
        }

        Department department = departmentRepository.findById(Integer.parseInt(req.getDepartment()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartment()));
        ResearchField researchField = researchFieldRepository.findById(Integer.parseInt(req.getField()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartment()));
        ResearchType researchType = researchTypeRepository.findById(Integer.parseInt(req.getResearchType()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartment()));
//        Category category = req.getCategory() != null
//                ? categoryRepository.findByCode(req.getCategory().getCode())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid category code: " + req.getCategory().getCode()))
//                : null;
//        return modelMapper.map(topic, TopicCreateRequest.class);

        String expectedProductsJson = objectMapper.writeValueAsString(req.getExpectedProducts());
        String budgetBreakdownJson = objectMapper.writeValueAsString(req.getBudgetBreakdown());

        Topic topic = modelMapper.map(req, Topic.class);

        topic.setId(UUID.randomUUID().toString());
        topic.setDepartment(department);
        topic.setResearchField(researchField);
        topic.setResearchType(researchType);
//        topic.setCategory(category);
        topic.setBudgetBreakdown(budgetBreakdownJson);
        topic.setExpectedProducts(expectedProductsJson);
        topic.setRemainingBudget(req.getTotalBudget());
        topic.setStatus(TopicStatus.SUBMITTED);

        List<Topic.AttachedDocument> attachedDocs = new ArrayList<>();
        if (req.getAttachedDocuments() != null) {
            for (AttachedDocumentDTO docDTO : req.getAttachedDocuments()) {
                Topic.AttachedDocument doc = new Topic.AttachedDocument();
                doc.setDescription(docDTO.getDescription());

                MultipartFile file = docDTO.getFile();
                if (file != null && !file.isEmpty()) {
                    if (file.getSize() > MAX_FILE_SIZE.toBytes()) {
                        throw new IllegalArgumentException("File size exceeds the maximum limit of " + MAX_FILE_SIZE);
                    }
                    String fileUrl = "";
                    String publicId = "";
                    try {
                        Map result = cloudinaryService.upload(file);
                        fileUrl = String.valueOf(result.get("url"));
                        publicId = String.valueOf(result.get("public_id"));

                        doc.setFilePath(fileUrl);
                        doc.setPublicId(publicId);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload PDF file", e);
                    }
                } else {
                    throw new IllegalArgumentException("File is required for attached documents");
                }

                attachedDocs.add(doc);
            }
        }
        topic.setAttachedDocuments(attachedDocs);

        topic = topicRepository.save(topic);

        log.info("Created new topic with ID: {}", topic.getId());
        return modelMapper.map(topic, TopicCreateRequest.class);
    }

    @Override
    @Transactional
    public TopicCreateRequest updateTopic(String id, TopicCreateRequest topicDTO) {
        log.info("Updating topic with ID: {}", id);
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic", "id", id));

        modelMapper.map(topicDTO, topic);
        topic = topicRepository.save(topic);

        log.info("Updated topic with ID: {}", topic.getId());
        return modelMapper.map(topic, TopicCreateRequest.class);
    }

    @Override
    @Transactional
    public void changeStatus(String id, TopicStatus status) {
        log.info("Changing status of topic with ID: {} to {}", id, status);
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic", "id", id));
        topic.setStatus(status);
        topicRepository.save(topic);
        log.info("Changed status of topic with ID: {} to {}", id, status);
    }
}
