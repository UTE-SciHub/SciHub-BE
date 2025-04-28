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
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.entity.*;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.TopicDTO;
import vn.thanhtuanle.model.request.AttachedDocumentCreation;
import vn.thanhtuanle.model.request.TopicCreateRequest;
import vn.thanhtuanle.model.response.TopicStatisticsResponse;
import vn.thanhtuanle.repository.*;
import vn.thanhtuanle.service.TopicService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicServiceImpl implements TopicService {

    private final ModelMapper modelMapper;
    private final TopicRepository topicRepository;
    private final DepartmentRepository departmentRepository;
    private final ResearchFieldRepository researchFieldRepository;
    private final ResearchTypeRepository researchTypeRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;
    private final CloudinaryService cloudinaryService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_FILE_SIZE;

    @Override
    public Page<TopicDTO> getAll(
            Pageable pageable,
            String query,
            TopicStatus status,
            Integer departmentId,
            Integer researchTypeId,
            Integer researchFieldId,
            Integer categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long minBudget,
            String investigator) {

        log.info("Fetching all topics with query: {}", query);
        Specification<Topic> spec = Specification.where(null);

        // Filter by status
        if (status != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }

        // Filter by query (search in vietnameseName and englishName)
        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("vietnameseName")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("englishName")), searchPattern)
                );
            });
        }

        // Filter by departmentId
        if (departmentId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("department").get("id"), departmentId));
        }

        // Filter by researchTypeId
        if (researchTypeId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("researchType").get("id"), researchTypeId));
        }

        // Filter by researchFieldId
        if (researchFieldId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("researchField").get("id"), researchFieldId));
        }

        // Filter by categoryId
        if (categoryId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("category").get("id"), categoryId));
        }

        // Filter by startDate (greater than or equal to)
        if (startDate != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), startDate));
        }

        // Filter by endDate (less than or equal to)
        if (endDate != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), endDate));
        }

        // Filter by minBudget (totalBudget greater than or equal to minBudget)
        if (minBudget != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("totalBudget"), minBudget));
        }

        // Filter by investigator (search in principalInvestigator)
        if (investigator != null && !investigator.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + investigator.toLowerCase() + "%";
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("principalInvestigator")), searchPattern);
            });
        }

        Page<Topic> topics = topicRepository.findAll(spec, pageable);

        log.info("Found {} topics", topics.getTotalElements());
        return topics.map(t -> modelMapper.map(t, TopicDTO.class));
    }

    @Override
    public TopicDTO findById(String id) {
        log.info("Fetching topic with ID: {}", id);
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Topic", "id", id));
        return modelMapper.map(topic, TopicDTO.class);
    }

    @Override
    @Transactional
    public TopicDTO createTopic(TopicCreateRequest req) throws JsonProcessingException {
        Department department = departmentRepository.findById(Integer.parseInt(req.getDepartment()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartment()));
        ResearchField researchField = researchFieldRepository.findById(Integer.parseInt(req.getField()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartment()));
        ResearchType researchType = researchTypeRepository.findById(Integer.parseInt(req.getResearchType()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", req.getDepartment()));
        Category category = categoryRepository.findById(Integer.parseInt(req.getCategory()))
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", req.getCategory()));

        String expectedProductsJson = objectMapper.writeValueAsString(req.getExpectedProducts());
        String budgetBreakdownJson = objectMapper.writeValueAsString(req.getBudgetBreakdown());

        Topic topic = modelMapper.map(req, Topic.class);

        topic.setId(UUID.randomUUID().toString());
        topic.setDepartment(department);
        topic.setResearchField(researchField);
        topic.setResearchType(researchType);
        topic.setCategory(category);
        topic.setBudgetBreakdown(budgetBreakdownJson);
        topic.setExpectedProducts(expectedProductsJson);
        topic.setRemainingBudget(req.getTotalBudget());
        topic.setStatus(TopicStatus.SUBMITTED);

        List<Topic.AttachedDocument> attachedDocs = new ArrayList<>();
        if (req.getAttachedDocuments() != null) {
            for (AttachedDocumentCreation docDTO : req.getAttachedDocuments()) {
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
        return modelMapper.map(topic, TopicDTO.class);
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

    @Override
    public boolean existsByTopicCode(String topicCode) {
        return topicRepository.existsByTopicCode(topicCode);
    }

    @Override
    public TopicStatisticsResponse getTopicStatistics() {
        TopicStatisticsResponse response = new TopicStatisticsResponse();

        // Summary metrics
        response.setTotalTopics(topicRepository.count());
        response.setInProgressCount(topicRepository.countInProgressTopics());
        response.setCompletedCount(topicRepository.countCompletedTopics());
        response.setTotalBudget(topicRepository.sumTotalBudget());

        // Status distribution
        List<TopicStatisticsResponse.StatusCount> statusCounts = topicRepository.countTopicsByStatus()
                .stream()
                .map(row -> new TopicStatisticsResponse.StatusCount(
                        row[0].toString(),
                        ((Number) row[1]).longValue()
                ))
                .collect(Collectors.toList());
        response.setStatusDistribution(statusCounts);

        // Department distribution
        List<TopicStatisticsResponse.DepartmentCount> departmentCounts = topicRepository.countTopicsByDepartment()
                .stream()
                .map(row -> new TopicStatisticsResponse.DepartmentCount(
                        (String) row[0],
                        ((Number) row[1]).longValue()
                ))
                .collect(Collectors.toList());
        response.setDepartmentDistribution(departmentCounts);

        return response;
    }
}
