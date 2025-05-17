package vn.thanhtuanle.controller.topic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.model.dto.TopicDTO;
import vn.thanhtuanle.model.request.*;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.TopicService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/topics")
@Tag(name = "Topic Controller")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @Operation(summary = "Topic List API", description = "Get paginated list of topic with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "status", required = false) TopicStatus status,
            @RequestParam(value = "departmentId", required = false) Integer departmentId,
            @RequestParam(value = "researchTypeId", required = false) Integer researchTypeId,
            @RequestParam(value = "researchFieldId", required = false) Integer researchFieldId,
            @RequestParam(value = "categoryId", required = false) Integer categoryId,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate,
            @RequestParam(value = "minBudget", required = false) Long minBudget,
            @RequestParam(value = "investigator", required = false) String investigator,
            @RequestParam(value = "periodId", required = false) String periodId
    ) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<TopicDTO> pageResult = topicService.getAll(pageable, query, status, departmentId, researchTypeId, researchFieldId, categoryId, startDate, endDate, minBudget, investigator, periodId);

        PageResponse<?> pageResponse = PageResponse.<List<TopicDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .currentPage(page)
                .totalPages(pageResult.getTotalPages())
                .totalItems(pageResult.getTotalElements())
                .itemsPerPage(size)
                .query(query)
                .data(pageResult.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }

    @Operation(summary = "Get Topic by ID API", description = "Get topic by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> getTopicById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.findById(id))
                .build());
    }

    @Operation(summary = "Create Topic API", description = "Create a new topic")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<?>> createTopic(
            @Valid @RequestPart(value = "data") TopicCreateRequest topicCreateRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "descriptions", required = false) String descriptionsJson
    ) throws JsonProcessingException {
        log.info("Received topic JSON: {}", topicCreateRequest);

        List<AttachedDocumentCreation> attachedDocuments = new ArrayList<>();
        if (descriptionsJson != null && !descriptionsJson.isEmpty()) {
            // Parse the descriptions JSON into a list of AttachedDocumentCreation objects
            ObjectMapper objectMapper = new ObjectMapper();
            List<AttachedDocumentCreation> descriptionDtos;
            try {
                descriptionDtos = objectMapper.readValue(
                        descriptionsJson,
                        new TypeReference<List<AttachedDocumentCreation>>() {}
                );
            } catch (JsonProcessingException e) {
                log.error("Failed to parse descriptions JSON: {}", descriptionsJson, e);
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid descriptions JSON format")
                        .data(null)
                        .build());
            }

            // Validate the number of descriptions matches the number of files
            if (files == null || files.isEmpty()) {
                if (!descriptionDtos.isEmpty()) {
                    log.error("Descriptions provided but no files uploaded");
                    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Files are required for attached documents in topic creation")
                            .data(null)
                            .build());
                }
            } else {
                if (descriptionDtos.size() != files.size()) {
                    log.error("Mismatch between number of descriptions ({}) and uploaded files ({})", descriptionDtos.size(), files.size());
                    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Number of descriptions does not match number of uploaded files")
                            .data(null)
                            .build());
                }

                for (int i = 0; i < descriptionDtos.size(); i++) {
                    AttachedDocumentCreation dto = descriptionDtos.get(i);
                    if (dto.getId() != null) {
                        log.warn("Unexpected id provided for new attached document: {}", dto.getId());
                        continue;
                    }
                    dto.setFile(files.get(i));
                    attachedDocuments.add(dto);
                }
            }
        }

        if (!attachedDocuments.isEmpty()) {
            topicCreateRequest.setAttachedDocuments(attachedDocuments);
        }

        log.info("Creating new topic with name: {}", topicCreateRequest.getVietnameseName());

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.createTopic(topicCreateRequest))
                .build());
    }

    @Operation(summary = "Update Topic API", description = "Update an existing topic")
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<?>> updateTopic(
            @PathVariable String id,
            @Valid @RequestPart(value = "data") TopicCreateRequest topicCreateRequest,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "descriptions", required = false) String descriptionsJson
    ) throws JsonProcessingException {
        log.info("Updating topic with ID: {}", id);

        List<AttachedDocumentCreation> attachedDocuments = new ArrayList<>();
        if (descriptionsJson != null && !descriptionsJson.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<AttachedDocumentCreation> descriptionDtos = objectMapper.readValue(
                    descriptionsJson,
                    new TypeReference<List<AttachedDocumentCreation>>() {}
            );

            // Count new documents (those without an id)
            long newDocCount = descriptionDtos.stream()
                    .filter(dto -> dto.getId() == null)
                    .count();

            // Validate the number of new files matches the number of new documents
            if (files != null && !files.isEmpty()) {
                if (newDocCount != files.size()) {
                    log.error("Mismatch between number of new documents ({}) and uploaded files ({})", newDocCount, files.size());
                    return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                            .status(HttpStatus.BAD_REQUEST.value())
                            .message("Number of new documents does not match number of uploaded files")
                            .data(null)
                            .build());
                }

                // Assign files to new documents (those without an id)
                int fileIndex = 0;
                for (AttachedDocumentCreation dto : descriptionDtos) {
                    if (dto.getId() == null) {
                        // This is a new document, assign a file
                        dto.setFile(files.get(fileIndex));
                        fileIndex++;
                    }
                    attachedDocuments.add(dto);
                }
            } else if (newDocCount > 0) {
                // If there are new documents but no files, this is an error
                log.error("New documents provided but no files uploaded");
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message("Files are required for new attached documents in topic update")
                        .data(null)
                        .build());
            } else {
                // No new documents, just add the existing ones
                attachedDocuments.addAll(descriptionDtos);
            }
        }

        if (!attachedDocuments.isEmpty()) {
            topicCreateRequest.setAttachedDocuments(attachedDocuments);
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.updateTopic(id, topicCreateRequest))
                .build());
    }

    @Operation(summary = "Exist Topic by topic code", description = "Check if topic exists by topic code")
    @GetMapping("/exists-by-topic-code")
    public ResponseEntity<BaseResponse<?>> existsByTopicCode(@RequestParam String topicCode) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.existsByTopicCode(topicCode))
                .build());
    }

    @GetMapping("/statistics")
    @Operation(summary = "Get Topic Statistics API", description = "Get topic statistics")
    public ResponseEntity<BaseResponse<?>> getTopicStatistics() {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.getTopicStatistics())
                .build());
    }

    @Operation(summary = "Get Topic by User ID API", description = "Get topics by user ID")
    @GetMapping("/my-topics")
    public ResponseEntity<BaseResponse<?>> getTopicsByUserId() {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.getUserTopics())
                .build());
    }

    @Operation(summary = "Submit Topic to Registration Period API", description = "Submit a topic to a registration period")
    @PostMapping("/{topicId}/submit")
    public ResponseEntity<BaseResponse<?>> submitTopic(
            @PathVariable String topicId,
            @RequestBody SubmitTopicRequest period) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.submitTopic(topicId, period.getRegistrationPeriodId()))
                .build());
    }

    @Operation(summary = "Delete Topic by ID API", description = "Delete a topic by ID")
    @DeleteMapping("/{topicId}")
    public ResponseEntity<BaseResponse<?>> deleteTopicById(@PathVariable String topicId) {
        topicService.deleteTopicById(topicId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(null)
                .build());
    }

    @Operation(summary = "Assign Topic to User API", description = "Assign a topic to an unit")
    @PostMapping("/{topicId}/assign")
    public ResponseEntity<BaseResponse<?>> assignTopicToUser(
            @PathVariable String topicId,
            @Valid @RequestBody AssignToDepartmentRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.assignToDepartment(topicId, req))
                .build());
    }

    @Operation(summary = "Unassign Topic from Department", description = "Remove a topic's department assignment")
    @PostMapping("/{topicId}/unassign")
    public ResponseEntity<BaseResponse<?>> unassignDepartment(@PathVariable String topicId) {
        topicService.unassignDepartment(topicId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(null)
                .build());
    }

    @Operation(summary = "Get Topic by department API", description = "Get topics by department ID")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<BaseResponse<?>> getTopicsByDepartmentId(
            @PathVariable String departmentId,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<TopicDTO> pageResult = topicService.getTopicsByDepartment(departmentId, query, pageable);

        PageResponse<?> pageResponse = PageResponse.<List<TopicDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .currentPage(page)
                .totalPages(pageResult.getTotalPages())
                .totalItems(pageResult.getTotalElements())
                .itemsPerPage(size)
                .data(pageResult.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }

    @Operation(summary = "Review Topic API", description = "Review a topic by ID")
    @PostMapping(value = "/{topicId}/review", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<?>> reviewTopic(
            @PathVariable String topicId,
            @RequestPart("pdfFile") MultipartFile pdfFile,
            @Valid @RequestPart("approved") ApprovedRequest approved) {
        log.info("Reviewing topic with ID: {}, approved: {}", topicId, approved);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.reviewTopic(topicId, approved.isApproved(), pdfFile))
                .build());
    }

    @Operation(summary = "Approve Topic API", description = "Approve a topic by ID")
    @PostMapping("/{topicId}/approve")
    public ResponseEntity<BaseResponse<?>> approveTopic(
            @PathVariable String topicId,
            @RequestBody Map<String, String> request) {
        topicService.approveTopic(topicId, request.get("notes"));
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(null)
                .build());
    }

    @Operation(summary = "Reject Topic API", description = "Reject a topic by ID")
    @PostMapping("/{topicId}/reject")
    public ResponseEntity<BaseResponse<?>> rejectTopic(
            @PathVariable String topicId,
            @RequestBody Map<String, String> request) {
        topicService.rejectTopic(topicId, request.get("notes"));
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(null)
                .build());
    }

    @PostMapping("/assign-category")
    @Operation(summary = "Assign Category to Topics", description = "Assign a category to multiple topics")
    public ResponseEntity<BaseResponse<?>> assignCategory(@RequestBody AssignCategoryRequest request) {
        topicService.assignCategory(request.getTopicIds(), request.getCategoryId());
        return ResponseEntity.ok(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Category assigned successfully")
                .data(null)
                .build());
    }

    @Operation(summary = "Approve Topics by Council", description = "Approve multiple topics with budget through a council")
    @PostMapping("/council-approval")
    public ResponseEntity<BaseResponse<?>> approveTopicsByCouncil(@Valid @RequestBody CouncilApprovalRequest request) {
        topicService.approveTopicsByCouncil(request);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }
}
