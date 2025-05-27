package vn.thanhtuanle.controller.topic;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.model.dto.TopicApplicationDTO;
import vn.thanhtuanle.model.request.EvaluationDetailRequest;
import vn.thanhtuanle.model.request.TopicApplicationRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.model.response.TopicApplicationResponse;
import vn.thanhtuanle.service.EvaluationService;
import vn.thanhtuanle.service.TopicApplicationService;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/topic-applications")
@Tag(name = "Topic Application Controller")
@RequiredArgsConstructor
public class TopicApplicationController {

    private final TopicApplicationService topicApplicationService;
    private final EvaluationService evaluationService;

    @Operation(summary = "Get All Topic Applications", description = "Retrieve all topic applications with optional filters")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "status", required = false) TopicStatus status,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "periodId", required = false) String periodId
    ) {
        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<TopicApplicationResponse> pageResult = topicApplicationService.getAllApplications(query, periodId, status, pageable);

        PageResponse<?> pageResponse = PageResponse.<List<TopicApplicationResponse>>builder()
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

    @Operation(summary = "Get Applications by Topic", description = "Retrieve all applications for a specific topic")
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<BaseResponse<?>> getApplicationsByTopic (
            @PathVariable String topicId
    ) {
        BaseResponse<?> response = BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicApplicationService.getApplicationsByTopic(topicId))
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Applications by Topic and User", description = "Retrieve all applications for a specific topic and user")
    @GetMapping("/topic/{topicId}/user/{userId}")
    public ResponseEntity<BaseResponse<?>> getApplicationsByTopicAndUser(
            @PathVariable String topicId,
            @PathVariable String userId,
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {
        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<TopicApplicationDTO> pageResult = topicApplicationService.getApplicationsByTopicAndUser(topicId, userId, pageable);

        PageResponse<?> pageResponse = PageResponse.<List<TopicApplicationDTO>>builder()
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

    @Operation(summary = "Get Topic Application by ID", description = "Retrieve a topic application by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicApplicationService.getApplicationById(id))
                .build());
    }

    @Operation(summary = "Register Topic Application", description = "Submit a new topic application")
    @PostMapping
    public ResponseEntity<BaseResponse<?>> register(@Valid @RequestBody TopicApplicationRequest request) {
        log.info("Registering topic application for topic ID: {}", request.getTopicId());
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicApplicationService.registerTopicApplication(request))
                .build());
    }

    @Operation(summary = "Update Topic Application Status", description = "Update the status of a topic application")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponse<?>> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        topicApplicationService.updateApplicationStatus(id, ApplicationStatus.valueOf(request.get("status")), request.get("notes"));
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(null)
                .build());
    }

    @Operation(summary = "Delete Topic Application", description = "Delete a topic application by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Long id) {
        topicApplicationService.deleteApplication(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(null)
                .build());
    }

    @Operation(summary = "Evaluate Topic Application", description = "Evaluate a topic application")
    @PostMapping("/{id}/evaluate")
    public ResponseEntity<BaseResponse<?>> evaluate(
            @PathVariable Long id,
            @RequestBody EvaluationDetailRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(evaluationService.submitEvaluation(id, request))
                .build());
    }

    @GetMapping("/{applicationId}/evaluation-detail")
    @Operation(summary = "Get Evaluation Details", description = "Retrieve evaluation details for a specific topic application")
    public ResponseEntity<BaseResponse<?>> getEvaluationDetails(@PathVariable Long applicationId) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(evaluationService.getEvaluationDetailByApplicationIdAndEvaluatorId(applicationId))
                .build());
    }

    @Operation(summary = "Determine Principal Investigator for a Topic", description = "Determine the principal investigator for a topic within a council and return ranked applications")
    @PostMapping("/council/{councilId}/topic/{topicId}/summary")
    public ResponseEntity<BaseResponse<?>> determinePrincipalInvestigator(
            @PathVariable Long councilId,
            @PathVariable String topicId) {
        List<TopicApplicationDTO> rankedApplications = evaluationService.determinePrincipalInvestigator(councilId, topicId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(rankedApplications)
                .build());
    }
}