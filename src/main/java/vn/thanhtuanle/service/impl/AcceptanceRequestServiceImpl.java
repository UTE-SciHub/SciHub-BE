package vn.thanhtuanle.service.impl;

import jakarta.mail.MessagingException;
import jakarta.persistence.criteria.Join;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.unit.DataSize;
import vn.thanhtuanle.common.service.MailService;
import vn.thanhtuanle.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.AcceptanceStatus;
import vn.thanhtuanle.common.enums.DocumentType;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.common.service.CloudinaryService;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.AcceptanceRequestDTO;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.repository.AcceptanceRequestRepository;
import vn.thanhtuanle.repository.TopicRepository;
import vn.thanhtuanle.service.AcceptanceRequestService;
import vn.thanhtuanle.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AcceptanceRequestServiceImpl implements AcceptanceRequestService {

    private final AcceptanceRequestRepository acceptanceRequestRepository;
    private final TopicRepository topicRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ObjectMapper objectMapper;
    private final UserService userService;
    private final MailService mailService;

    @Value("${spring.servlet.multipart.max-file-size}")
    private DataSize MAX_FILE_SIZE;

    @Value("${application.domain.url}")
    private String DOMAIN_URL;

    @Override
    public Page<AcceptanceRequestDTO> findAll(Pageable pageable, String councilId, AcceptanceStatus status) {
        log.info("Fetching acceptance requests with filters - councilId: {}, status: {}", councilId, status);

        Specification<AcceptanceRequest> spec = Specification.where(null);

        if (councilId != null && !councilId.isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                // Join with topic to access its topic councils
                Join<AcceptanceRequest, Topic> topicJoin = root.join("topic");
                Join<Topic, TopicCouncil> topicCouncilJoin = topicJoin.join("topicCouncils");
                Join<TopicCouncil, Council> councilJoin = topicCouncilJoin.join("council");
                return criteriaBuilder.equal(councilJoin.get("id"), councilId);
            });
        }

        if (status != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
        }

        Page<AcceptanceRequest> acceptanceRequests = acceptanceRequestRepository.findAll(spec, pageable);
        return acceptanceRequests.map(request -> modelMapper.map(request, AcceptanceRequestDTO.class));
    }

    @Override
    @Transactional
    public AcceptanceRequestDTO createAcceptanceRequest(
            vn.thanhtuanle.model.request.AcceptanceRequest request,
            List<MultipartFile> files,
            String descriptionsJson) {

        log.info("Creating acceptance request for topic ID: {}", request.getTopicId());

        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", request.getTopicId()));

        List<AcceptanceRequest> existingRequests = acceptanceRequestRepository.findByTopic(topic);
        if (!existingRequests.isEmpty()) {
            existingRequests.forEach(existingRequest -> existingRequest.setIsFinal(false));
            acceptanceRequestRepository.saveAll(existingRequests);
            log.info("Updated {} previous acceptance requests to non-final", existingRequests.size());
        }

        vn.thanhtuanle.entity.AcceptanceRequest acceptanceRequest = vn.thanhtuanle.entity.AcceptanceRequest.builder()
                .topic(topic)
                .status(AcceptanceStatus.PENDING)
                .submissionDate(request.getSubmissionDate())
                .notes(request.getNotes())
                .acknowledgment(request.getAcknowledgment())
                .attemptNumber(1)
                .isFinal(true)
                .build();

        Integer existingAttempts = acceptanceRequestRepository.countByTopicId(topic.getId());
        if (existingAttempts > 0) {
            acceptanceRequest.setAttemptNumber(existingAttempts + 1);
        }

        acceptanceRequest = acceptanceRequestRepository.save(acceptanceRequest);

        if (files != null && !files.isEmpty()) {
            List<Map<String, String>> descriptions = parseDescriptions(descriptionsJson);
            List<Map<String, Object>> uploadResults = cloudinaryService.uploadAll(files);
            List<Document> documents = new ArrayList<>();

            for (int i = 0; i < uploadResults.size(); i++) {
                Map<String, Object> uploadResult = uploadResults.get(i);

                if (!"success".equals(uploadResult.get("status"))) {
                    log.error("Failed to upload file: {}", uploadResult.get("error"));
                    continue;
                }

                Map<String, Object> cloudinaryData = (Map<String, Object>) uploadResult.get("uploadResult");
                String originalFilename = (String) uploadResult.get("originalFilename");

                String description = "";
                DocumentType documentType = DocumentType.COMPLETION_REPORT;
                if (descriptions != null && i < descriptions.size()) {
                    description = descriptions.get(i).get("description");
                    String typeFromFE = descriptions.get(i).get("type");
                    if (typeFromFE != null && !typeFromFE.isEmpty()) {
                        try {
                            documentType = DocumentType.valueOf(typeFromFE);
                        } catch (IllegalArgumentException e) {
                            log.error("Invalid document type: {}", typeFromFE);
                        }
                    }
                }

                Document document = Document.builder()
                        .topic(topic)
                        .acceptanceRequest(acceptanceRequest)
                        .documentType(documentType.getLabel())
                        .filePath((String) cloudinaryData.get("url"))
                        .publicId((String) cloudinaryData.get("public_id"))
                        .uploadDate(LocalDateTime.now())
                        .originalFileName(originalFilename)
                        .description(description)
                        .build();

                documents.add(document);
            }

            acceptanceRequest.setDocuments(documents);

            topic.setStatus(TopicStatus.ACCEPTANCE_REQUESTED);
            topic = topicRepository.save(topic);
            acceptanceRequest = acceptanceRequestRepository.save(acceptanceRequest);
        }

        return modelMapper.map(acceptanceRequest, AcceptanceRequestDTO.class);
    }

    private List<Map<String, String>> parseDescriptions(String descriptionsJson) {
        if (descriptionsJson == null || descriptionsJson.isBlank()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(descriptionsJson,
                    new TypeReference<List<Map<String, String>>>() {});
        } catch (JsonProcessingException e) {
            log.error("Error parsing descriptions JSON: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public AcceptanceRequestDTO findById(Integer id) {
        log.info("Finding acceptance request with ID: {}", id);
        AcceptanceRequest acceptanceRequest = acceptanceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcceptanceRequest", "id", id));
        return modelMapper.map(acceptanceRequest, AcceptanceRequestDTO.class);
    }

    @Transactional
    @Override
    public AcceptanceRequestDTO approveAcceptanceRequest(Integer id, MultipartFile decisionFile) {
        log.info("Approving acceptance request with ID: {}", id);
        AcceptanceRequest acceptanceRequest = acceptanceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcceptanceRequest", "id", id));

        if (acceptanceRequest.getStatus() != AcceptanceStatus.PENDING) {
            throw new IllegalStateException("Cannot approve non-pending acceptance request");
        }

        acceptanceRequest.setStatus(AcceptanceStatus.APPROVED);

        String fileUrl = null;
        String publicId = null;
        if (decisionFile != null && !decisionFile.isEmpty()) {
            if (decisionFile.getSize() > MAX_FILE_SIZE.toBytes()) {
                throw new IllegalArgumentException("File vượt quá kích thước tối đa cho phép: " + MAX_FILE_SIZE + " bytes");
            }

            String fileName = decisionFile.getOriginalFilename();
            if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                try {
                    Map result = cloudinaryService.upload(decisionFile);
                    fileUrl = String.valueOf(result.get("url"));
                    publicId = String.valueOf(result.get("public_id"));
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload PDF file", e);
                }
            } else {
                throw new IllegalArgumentException("File phải có định dạng PDF (.pdf)");
            }
        } else {
            throw new IllegalArgumentException("File PDF không được để trống hoặc null");
        }

        Document document = Document.builder()
                .topic(acceptanceRequest.getTopic())
                .acceptanceRequest(acceptanceRequest)
                .documentType(DocumentType.DECISION.getLabel())
                .filePath(fileUrl)
                .publicId(publicId)
                .uploadDate(LocalDateTime.now())
                .originalFileName(decisionFile.getOriginalFilename())
                .description("Quyết định nghiệm thu")
                .build();

        if (acceptanceRequest.getDocuments() == null) {
            acceptanceRequest.setDocuments(new ArrayList<>());
        }
        acceptanceRequest.getDocuments().add(document);

        Topic topic = acceptanceRequest.getTopic();
        topic.setStatus(TopicStatus.ACCEPTED);
        topicRepository.save(topic);

        acceptanceRequest = acceptanceRequestRepository.save(acceptanceRequest);

        UserDTO user = userService.getUserByEmail(acceptanceRequest.getTopic().getPrincipalInvestigator());
        try {
            String recipientEmail = user.getEmail();
            String recipientName = user.getName();
            String topicName = topic.getVietnameseName();
            String topicCode = topic.getTopicCode();
            String councilName = acceptanceRequest.getCouncil().getName();
            String evaluationDate = acceptanceRequest.getNotes() != null ?
                    acceptanceRequest.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Chưa xác định";
            String notes = acceptanceRequest.getNotes() != null ? acceptanceRequest.getNotes() : "Không có nhận xét";
            String viewDetailsUrl = DOMAIN_URL + "topics/" + topic.getId();

            mailService.sendTopicAcceptedNotificationEmail(recipientEmail, recipientName, topicName, topicCode, councilName, evaluationDate, notes, viewDetailsUrl);
        } catch (MessagingException e) {
            log.error("Failed to send topic accepted notification email", e);
        }

        return modelMapper.map(acceptanceRequest, AcceptanceRequestDTO.class);
    }

    @Transactional
    @Override
    public AcceptanceRequestDTO rejectAcceptanceRequest(Integer id, String rejectionReason) {
        log.info("Rejecting acceptance request with ID: {}", id);
        AcceptanceRequest acceptanceRequest = acceptanceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AcceptanceRequest", "id", id));

        if (acceptanceRequest.getStatus() != AcceptanceStatus.PENDING) {
            throw new IllegalStateException("Cannot reject non-pending acceptance request");
        }

        acceptanceRequest.setStatus(AcceptanceStatus.REJECTED);
        acceptanceRequest.setNotes(rejectionReason);

        Topic topic = acceptanceRequest.getTopic();
        topic.setStatus(TopicStatus.NOT_ACCEPTED);
        topicRepository.save(topic);

        acceptanceRequest = acceptanceRequestRepository.save(acceptanceRequest);

        UserDTO user = userService.getUserByEmail(acceptanceRequest.getTopic().getPrincipalInvestigator());
        try {
            String recipientEmail = user.getEmail();
            String recipientName = user.getName();
            String topicName = topic.getVietnameseName();
            String topicCode = topic.getTopicCode();
            String councilName = acceptanceRequest.getCouncil().getName();
            String evaluationDate = acceptanceRequest.getNotes() != null ?
                    acceptanceRequest.getCreatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Chưa xác định";
            String notes = acceptanceRequest.getNotes() != null ? acceptanceRequest.getNotes() : "Không có nhận xét";
            String viewDetailsUrl = DOMAIN_URL + "topics/" + topic.getId();

            mailService.sendTopicRejectedNotificationEmail(recipientEmail, recipientName, topicName, topicCode, councilName, evaluationDate, notes, viewDetailsUrl);
        } catch (MessagingException e) {
            log.error("Failed to send topic accepted notification email", e);
        }
        return modelMapper.map(acceptanceRequest, AcceptanceRequestDTO.class);
    }
}
