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
import vn.thanhtuanle.common.enums.DocumentType;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.common.enums.TopicMemberRole;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.entity.*;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.*;
import vn.thanhtuanle.model.request.AssignToDepartmentRequest;
import vn.thanhtuanle.model.request.AttachedDocumentCreation;
import vn.thanhtuanle.model.request.CouncilApprovalRequest;
import vn.thanhtuanle.model.request.TopicCreateRequest;
import vn.thanhtuanle.model.response.TopicStatisticsResponse;
import vn.thanhtuanle.repository.*;
import vn.thanhtuanle.service.TopicService;
import vn.thanhtuanle.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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
    private final UserService userService;
    private final TopicMembersRepository topicMembersRepository;
    private final UserRepository userRepository;
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final DocumentRepository documentRepository;
    private final CouncilRepository councilRepository;

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
            String investigator,
            String periodId
    ) {

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

        if (periodId != null) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("registrationPeriod").get("id"), periodId));
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
        ResearchField researchField = researchFieldRepository.findById(Integer.parseInt(req.getField()))
                .orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", req.getField()));
        ResearchType researchType = researchTypeRepository.findById(Integer.parseInt(req.getResearchType()))
                .orElseThrow(() -> new ResourceNotFoundException("ResearchType", "id", req.getResearchType()));
        Category category = categoryRepository.findById(Integer.parseInt(req.getCategory()))
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", req.getCategory()));

        String expectedProductsJson = objectMapper.writeValueAsString(req.getExpectedProducts());
        String budgetBreakdownJson = objectMapper.writeValueAsString(req.getBudgetBreakdown());

        Topic topic = modelMapper.map(req, Topic.class);

        topic.setId(UUID.randomUUID().toString());
        topic.setResearchField(researchField);
        topic.setResearchType(researchType);
        topic.setCategory(category);
        topic.setBudgetBreakdown(budgetBreakdownJson);
        topic.setExpectedProducts(expectedProductsJson);
        topic.setRemainingBudget(req.getTotalBudget());
        topic.setStatus(TopicStatus.DRAFT);

        List<Topic.AttachedDocument> attachedDocs = new ArrayList<>();
        List<Document> documents = new ArrayList<>();
        if (req.getAttachedDocuments() != null) {
            for (AttachedDocumentCreation docDTO : req.getAttachedDocuments()) {
                if (docDTO.getId() != null) {
                    log.warn("Unexpected id provided for new attached document: {}", docDTO.getId());
                    continue;
                }

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
                        doc.setOriginalFileName(file.getOriginalFilename());

                        Document document = Document.builder()
                                .topic(topic)
                                .documentType(DocumentType.TOPIC_REGISTRATION.getLabel())
                                .filePath(fileUrl)
                                .publicId(publicId)
                                .uploadDate(LocalDateTime.now())
                                .originalFileName(file.getOriginalFilename())
                                .build();

                        documents.add(document);
                    } catch (IOException e) {
                        log.error("Failed to upload PDF file for document: {}", docDTO.getDescription(), e);
                        throw new RuntimeException("Failed to upload PDF file", e);
                    }
                } else {
                    throw new IllegalArgumentException("File is required for new attached documents");
                }

                attachedDocs.add(doc);
            }
        }

        topic.setDocuments(documents);
        topic.setAttachedDocuments(attachedDocs);
        topic = topicRepository.save(topic);

        UserDTO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No logged-in user found");
        }

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUser.getId()));

        TopicMember topicMember = TopicMember.builder()
                .topic(topic)
                .user(user)
                .role(TopicMemberRole.MEMBER)
                .build();

        topicMembersRepository.save(topicMember);

        log.info("Created new topic with ID: {}. Added current user as MEMBER.", topic.getId());
        return modelMapper.map(topic, TopicDTO.class);
    }

    @Override
    @Transactional
    public TopicDTO updateTopic(String id, TopicCreateRequest topicDTO) throws JsonProcessingException {
        log.info("Updating topic with ID: {}", id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", id));

        // Update research field
        if (topicDTO.getField() != null) {
            ResearchField researchField = researchFieldRepository.findById(Integer.parseInt(topicDTO.getField()))
                    .orElseThrow(() -> new ResourceNotFoundException("ResearchField", "id", topicDTO.getField()));
            topic.setResearchField(researchField);
        }

        // Update research type
        if (topicDTO.getResearchType() != null) {
            ResearchType researchType = researchTypeRepository.findById(Integer.parseInt(topicDTO.getResearchType()))
                    .orElseThrow(() -> new ResourceNotFoundException("ResearchType", "id", topicDTO.getResearchType()));
            topic.setResearchType(researchType);
        }

        // Update category
        if (topicDTO.getCategory() != null) {
            Category category = categoryRepository.findById(Integer.parseInt(topicDTO.getCategory()))
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", topicDTO.getCategory()));
            topic.setCategory(category);
        }

        // Update expected products
        if (topicDTO.getExpectedProducts() != null) {
            ExpectedProductDTO sanitizedExpectedProducts = getExpectedProductDTO(topicDTO);
            String expectedProductsJson = objectMapper.writeValueAsString(sanitizedExpectedProducts);
            log.info("Expected products after serialization: {}", expectedProductsJson);
            topic.setExpectedProducts(expectedProductsJson);
        }

        // Update budget breakdown
        if (topicDTO.getBudgetBreakdown() != null) {
            List<BudgetBreakdownDTO> sanitizedBudgetBreakdown = topicDTO.getBudgetBreakdown().stream()
                    .map(dto -> {
                        BudgetBreakdownDTO sanitized = new BudgetBreakdownDTO();
                        sanitized.setCategory(dto.getCategory() != null ? dto.getCategory().replaceAll("[\\p{Cntrl}]", "") : null);
                        sanitized.setAmount(dto.getAmount());
                        sanitized.setDescription(dto.getDescription() != null ? dto.getDescription().replaceAll("[\\p{Cntrl}]", "") : null);
                        return sanitized;
                    })
                    .collect(Collectors.toList());
            String budgetBreakdownJson = objectMapper.writeValueAsString(sanitizedBudgetBreakdown);
            log.info("Budget breakdown after serialization: {}", budgetBreakdownJson);
            topic.setBudgetBreakdown(budgetBreakdownJson);
        }

        // Update total budget and remaining budget
        if (topicDTO.getTotalBudget() != 0) {
            topic.setRemainingBudget(topicDTO.getTotalBudget());
        }

        // Update attached documents
        if (topicDTO.getAttachedDocuments() != null) {
            List<Topic.AttachedDocument> existingDocs = topic.getAttachedDocuments() != null ? new ArrayList<>(topic.getAttachedDocuments()) : new ArrayList<>();
            List<Document> existingDocuments = topic.getDocuments() != null ? new ArrayList<>(topic.getDocuments()) : new ArrayList<>();
            Map<String, Topic.AttachedDocument> docMap = existingDocs.stream()
                    .filter(d -> d.getPublicId() != null)
                    .collect(Collectors.toMap(Topic.AttachedDocument::getPublicId, d -> d));
            Map<String, Document> docEntityMap = existingDocuments.stream()
                    .filter(d -> d.getPublicId() != null)
                    .collect(Collectors.toMap(Document::getPublicId, d -> d));

            for (AttachedDocumentCreation docDTO : topicDTO.getAttachedDocuments()) {
                Topic.AttachedDocument doc;
                Document docEntity;

                if (docDTO.getId() != null) {
                    // Update existing document
                    doc = docMap.get(docDTO.getId());
                    docEntity = docEntityMap.get(docDTO.getId());
                    if (doc == null || docEntity == null) {
                        throw new ResourceNotFoundException("AttachedDocument", "id", docDTO.getId());
                    }
                    doc.setDescription(docDTO.getDescription());
                } else {
                    // Create new document
                    doc = new Topic.AttachedDocument();
                    doc.setDescription(docDTO.getDescription());
                    docEntity = new Document();
                    docEntity.setTopic(topic);
                    docEntity.setDocumentType(DocumentType.TOPIC_REGISTRATION.getLabel());
                    docEntity.setUploadDate(LocalDateTime.now());

                    MultipartFile file = docDTO.getFile();
                    if (file != null && !file.isEmpty()) {
                        if (file.getSize() > MAX_FILE_SIZE.toBytes()) {
                            throw new IllegalArgumentException("File size exceeds the maximum limit of " + MAX_FILE_SIZE);
                        }
                        try {
                            Map result = cloudinaryService.upload(file);
                            String fileUrl = String.valueOf(result.get("url"));
                            String publicId = String.valueOf(result.get("public_id"));

                            doc.setFilePath(fileUrl);
                            doc.setPublicId(publicId);
                            doc.setOriginalFileName(file.getOriginalFilename());
                            docEntity.setFilePath(fileUrl);
                            docEntity.setPublicId(publicId);
                            docEntity.setOriginalFileName(file.getOriginalFilename());
                        } catch (IOException e) {
                            log.error("Failed to upload PDF file for document: {}", docDTO.getDescription(), e);
                            throw new RuntimeException("Failed to upload PDF file", e);
                        }
                    } else {
                        throw new IllegalArgumentException("File is required for new attached documents");
                    }
                    existingDocs.add(doc);
                    existingDocuments.add(docEntity);
                }
            }

            topic.setAttachedDocuments(existingDocs);
            topic.setDocuments(existingDocuments);
        }

        topic.setStatus(TopicStatus.DRAFT);
        topic = topicRepository.save(topic);

        log.info("Updated topic with ID: {}", topic.getId());
        return modelMapper.map(topic, TopicDTO.class);
    }

    private static ExpectedProductDTO getExpectedProductDTO(TopicCreateRequest topicDTO) {
        ExpectedProductDTO sanitizedExpectedProducts = new ExpectedProductDTO();

        // Sanitize scientific
        ScientificProductDTO sanitizedScientific = new ScientificProductDTO(
                topicDTO.getExpectedProducts().getScientific() != null ? topicDTO.getExpectedProducts().getScientific().getDomestic() : 0,
                topicDTO.getExpectedProducts().getScientific() != null ? topicDTO.getExpectedProducts().getScientific().getInternational() : 0
        );
        sanitizedExpectedProducts.setScientific(sanitizedScientific);

        // Sanitize training
        TrainingProductDTO sanitizedTraining = new TrainingProductDTO(
                topicDTO.getExpectedProducts().getTraining() != null ? topicDTO.getExpectedProducts().getTraining().getMasters() : 0,
                topicDTO.getExpectedProducts().getTraining() != null ? topicDTO.getExpectedProducts().getTraining().getStudents() : 0
        );
        sanitizedExpectedProducts.setTraining(sanitizedTraining);

        // Sanitize commercial
        String sanitizedDetails = topicDTO.getExpectedProducts().getCommercial() != null &&
                topicDTO.getExpectedProducts().getCommercial().getDetails() != null
                ? topicDTO.getExpectedProducts().getCommercial().getDetails().replaceAll("[\\p{Cntrl}]", "")
                : "";
        CommercialProductDTO sanitizedCommercial = new CommercialProductDTO(sanitizedDetails);
        sanitizedExpectedProducts.setCommercial(sanitizedCommercial);
        return sanitizedExpectedProducts;
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
        return TopicStatisticsResponse.builder()
                .totalTopics(topicRepository.count())
                .inProgressCount(topicRepository.countInProgressTopics())
                .completedCount(topicRepository.countCompletedTopics())
                .totalBudget(topicRepository.sumTotalBudget())
                .statusDistribution(topicRepository.countTopicsByStatus()
                        .stream()
                        .map(row -> TopicStatisticsResponse.StatusCount.builder()
                                .status(row[0].toString())
                                .count(((Number) row[1]).longValue())
                                .build())
                        .collect(Collectors.toList()))
                .departmentDistribution(topicRepository.countTopicsByDepartment()
                        .stream()
                        .map(row -> TopicStatisticsResponse.DepartmentCount.builder()
                                .departmentName((String) row[0])
                                .count(((Number) row[1]).longValue())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    @Transactional
    public void addMemberToTopic(String topicId, String userId, TopicMemberRole role) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (topicMembersRepository.existsByTopicIdAndUserId(topicId, userId)) {
            throw new IllegalStateException("User is already a member of this topic");
        }

        TopicMember topicMember = TopicMember.builder()
                .topic(topic)
                .user(user)
                .role(role)
                .build();

        topicMembersRepository.save(topicMember);
    }

    @Transactional
    @Override
    public void addMembersToTopic(String topicId, List<String> userIds, TopicMemberRole role) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        List<TopicMember> newMembers = new ArrayList<>();

        for (String userId : userIds) {
            if (topicMembersRepository.existsByTopicIdAndUserId(topicId, userId)) {
                continue;
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

            TopicMember topicMember = TopicMember.builder()
                    .topic(topic)
                    .user(user)
                    .role(role)
                    .build();

            newMembers.add(topicMember);
        }

        topicMembersRepository.saveAll(newMembers);
    }

    @Override
    public List<TopicMember> getMembersOfTopic(String topicId) {
        return topicMembersRepository.findByTopicId(topicId);
    }

    @Override
    public List<TopicDTO> getUserTopics() {
        UserDTO currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalStateException("No logged-in user found");
        }

        List<Topic> topics = topicRepository.findByUserId(currentUser.getId());
        return topics.stream()
                .map(topic -> modelMapper.map(topic, TopicDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TopicDTO submitTopic(String topicId, String registrationPeriodId) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        if (topic.getStatus() != TopicStatus.DRAFT) {
            throw new IllegalStateException("Only topics in DRAFT status can be submitted.");
        }

        RegistrationPeriod period = registrationPeriodRepository.findById(registrationPeriodId).orElseThrow(() -> new ResourceNotFoundException("RegistraionPeriod", "id", registrationPeriodId));

        if (period.getStatus() != RegistrationPeriodsStatus.OPEN) {
            throw new IllegalStateException("The registration period is not open.");
        }

        LocalDate currentDate = LocalDate.now();
        if (currentDate.isBefore(period.getStartDate()) ||
                currentDate.isAfter(period.getEndDate())) {
            throw new IllegalStateException("The registration period is not currently active.");
        }

        topic.setStatus(TopicStatus.SUBMITTED);
        topic.setRegistrationPeriod(period);

        Topic savedTopic = topicRepository.save(topic);
        return modelMapper.map(savedTopic, TopicDTO.class);
    }

    @Override
    public void deleteTopicById(String topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        List<Topic.AttachedDocument> documents = topic.getAttachedDocuments();
        if (documents != null) {
            for (Topic.AttachedDocument doc : documents) {
                String publicId = doc.getPublicId();
                if (publicId != null && !publicId.isBlank()) {
                    try {
                        cloudinaryService.delete(publicId, "raw");
                        log.info("Deleted file from Cloudinary with publicId: {}", publicId);
                    } catch (Exception e) {
                        log.warn("Failed to delete file from Cloudinary with publicId: {}", publicId, e);
                    }
                }
            }
        }

        topicRepository.delete(topic);
        log.info("Deleted topic with ID: {}", topicId);
    }

    @Transactional
    @Override
    public TopicDTO assignToDepartment(String topicId, AssignToDepartmentRequest request) {
        log.info("Assigning topic with ID: {} to department with ID: {}", topicId, request.getDepartmentId());
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        log.info("Fetching department with ID: {}", request.getDepartmentId());
        Department department = departmentRepository.findById(Integer.parseInt(request.getDepartmentId()))
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        topic.setStatus(TopicStatus.ASSIGNED);
        topic.setDepartment(department);
        topic.setAdditionalNotes(request.getNotes());

        Topic savedTopic = topicRepository.save(topic);
        log.info("Assigned topic with ID: {} to department with ID: {}", topicId, request.getDepartmentId());

        return modelMapper.map(savedTopic, TopicDTO.class);
    }

    @Transactional
    @Override
    public void unassignDepartment(String topicId) {
        log.info("Unassigning department from topic with ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        if (topic.getStatus() != TopicStatus.ASSIGNED) {
            throw new IllegalStateException("Đề tài chưa được phân công");
        }

        topic.setDepartment(null);
        topic.setStatus(TopicStatus.SUBMITTED);
        topic.setAdditionalNotes("");

        topicRepository.save(topic);
        log.info("Successfully unassigned department from topic with ID: {}", topicId);
    }

    @Override
    public Page<TopicDTO> getTopicsByDepartment(String departmentEmail, String query, Pageable pageable) {
        log.info("Fetching topics for department with ID: {}", departmentEmail);
        Department department = departmentRepository.findByEmail(departmentEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Department", "email", departmentEmail));

        Specification<Topic> spec = Specification.where((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("department"), department));

        if (query != null && !query.trim().isEmpty()) {
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> {
                String searchPattern = "%" + query.toLowerCase() + "%";
                return criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("vietnameseName")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("englishName")), searchPattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("topicCode")), searchPattern)
                );
            });
        }

        Page<Topic> topics = topicRepository.findAll(spec, pageable);
        log.info("Found {} topics for department with ID: {}", topics.getTotalElements(), departmentEmail);
        return topics.map(t -> modelMapper.map(t, TopicDTO.class));
    }

    @Transactional
    @Override
    public void approveTopic(String topicId, String notes) {
        log.info("Approving topic with ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        if (topic.getStatus() != TopicStatus.ASSIGNED) {
            throw new IllegalStateException("Chỉ những đề tài được phân công mới có thể chấp nhận.");
        }

        topic.setStatus(TopicStatus.REVIEWED);
        topic.setAdditionalNotes(notes);
        topicRepository.save(topic);
        log.info("Approved topic with ID: {}", topicId);
    }

    @Transactional
    @Override
    public void rejectTopic(String topicId, String notes) {
        log.info("Rejecting topic with ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        if (topic.getStatus() != TopicStatus.ASSIGNED) {
            throw new IllegalStateException("Chỉ những đề tài được phân công mới có thể từ chối.");
        }

        topic.setStatus(TopicStatus.REJECTED);
        topic.setRejectionReason(notes);
        topicRepository.save(topic);
        log.info("Rejected topic with ID: {}", topicId);
    }

    @Transactional
    @Override
    public TopicDTO reviewTopic(String topicId, boolean approved, MultipartFile file) {
        log.info("Reviewing topic with ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));

        if(approved) {
            topic.setStatus(TopicStatus.IN_CATALOG);
        } else {
            topic.setStatus(TopicStatus.REJECTED);
        }

        Document document = new Document();
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

                document.setTopic(topic);
                document.setDocumentType(DocumentType.REVIEW_RESULT.getLabel());
                document.setFilePath(fileUrl);
                document.setPublicId(publicId);
                document.setUploadDate(LocalDateTime.now());
                document.setOriginalFileName(file.getOriginalFilename());

                documentRepository.save(document);
            } catch (IOException e) {
                log.error("Failed to upload PDF file for document: {}", file.getOriginalFilename(), e);
                throw new RuntimeException("Failed to upload PDF file", e);
            }
        }
        List<Document> documents = topic.getDocuments();
        if (documents == null) {
            documents = new ArrayList<>();
        }
        documents.add(document);
        topic.setDocuments(documents);

        Topic savedTopic = topicRepository.save(topic);
        log.info("Reviewed topic with ID: {}. New status: {}", topicId, approved ? "REVIEWED" : "NEED_REVISION");

        return modelMapper.map(savedTopic, TopicDTO.class);
    }

    @Transactional
    @Override
    public void assignCategory(List<String> topicIds, Integer categoryId) {
        log.info("Assigning category with ID: {} to topics: {}", categoryId, topicIds);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId.toString()));

        List<Topic> topics = topicRepository.findAllById(topicIds);
        if (topics.isEmpty()) {
            throw new ResourceNotFoundException("Topics", "ids", topicIds.toString());
        }

        for (Topic topic : topics) {
            topic.setCategory(category);

        }

        topicRepository.saveAll(topics);
        log.info("Assigned category with ID: {} to {} topics", categoryId, topics.size());
    }

    @Transactional
    @Override
    public void approveTopicsByCouncil(CouncilApprovalRequest request) {
        log.info("Approving topics through council ID: {}", request.getCouncilId());

        Council council = councilRepository.findById(request.getCouncilId())
                .orElseThrow(() -> new ResourceNotFoundException("Council", "id", request.getCouncilId()));

        for (CouncilApprovalRequest.TopicApproval approval : request.getTopics()) {
            Topic topic = topicRepository.findById(approval.getTopicId())
                    .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", approval.getTopicId()));

            // Update topic
            topic.setStatus(TopicStatus.APPROVED);
            topic.setApprovedBudget(approval.getApprovedBudget());
            topic.setRemainingBudget(approval.getApprovedBudget());
            topic.setApprovalDecisionCode(request.getDecisionNumber());

            topicRepository.save(topic);
        }

        log.info("Successfully approved {} topics through council ID: {}",
                request.getTopics().size(), request.getCouncilId());
    }
}
