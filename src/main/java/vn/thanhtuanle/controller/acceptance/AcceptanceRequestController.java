package vn.thanhtuanle.controller.acceptance;

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
import vn.thanhtuanle.common.enums.AcceptanceStatus;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.AcceptanceRequestDTO;
import vn.thanhtuanle.model.request.AcceptanceRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.AcceptanceRequestService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/acceptance")
@Tag(name = "Acceptance Controller")
@RequiredArgsConstructor
@Slf4j
public class AcceptanceRequestController {

    private final AcceptanceRequestService acceptanceRequestService;

    @Operation(summary = "Get Acceptance Requests", description = "Get paginated list of acceptance requests with filtering options")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAllAcceptanceRequests(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "submissionDate") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "councilId", required = false) String councilId,
            @RequestParam(value = "status", required = false) AcceptanceStatus status
    ) {
        log.info("Fetching acceptance requests with page: {}, size: {}, councilId: {}, status: {}", page, size, councilId, status);

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<AcceptanceRequestDTO> pageResult = acceptanceRequestService.findAll(pageable, councilId, status);

        PageResponse<?> pageResponse = PageResponse.<List<AcceptanceRequestDTO>>builder()
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

    @Operation(summary = "Create Acceptance Request", description = "Create a new acceptance request with optional files")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<AcceptanceRequestDTO>> createAcceptanceRequest(
            @Valid @RequestPart(value = "data") AcceptanceRequest req,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            @RequestPart(value = "descriptions", required = false) String descriptionsJson
    ) throws IOException {
        log.info("Creating acceptance request for topic ID: {}", req.getTopicId());

        AcceptanceRequestDTO createdRequest = acceptanceRequestService.createAcceptanceRequest(req, files, descriptionsJson);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<AcceptanceRequestDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message(Constant.CREATED_SUCCESSFULLY.getValue())
                        .data(createdRequest)
                        .build());
    }

    @Operation(summary = "Get Acceptance Request by ID", description = "Retrieve an acceptance request by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<AcceptanceRequestDTO>> getAcceptanceRequestById(
            @PathVariable Integer id) {
        log.info("Fetching acceptance request with ID: {}", id);

        AcceptanceRequestDTO dto = acceptanceRequestService.findById(id);

        return ResponseEntity.ok(BaseResponse.<AcceptanceRequestDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(dto)
                .build());
    }

    @Operation(summary = "Approve Acceptance Request", description = "Approve an acceptance request with decision file")
    @PutMapping("/{id}/approve")
    public ResponseEntity<BaseResponse<AcceptanceRequestDTO>> approveAcceptanceRequest(
            @PathVariable Integer id,
            @RequestParam("decisionFile") MultipartFile decisionFile
    ) {
        log.info("Approving acceptance request with ID: {}", id);
        AcceptanceRequestDTO dto = acceptanceRequestService.approveAcceptanceRequest(id, decisionFile);

        return ResponseEntity.ok(BaseResponse.<AcceptanceRequestDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Acceptance request approved successfully")
                .data(dto)
                .build());
    }

    @Operation(summary = "Reject Acceptance Request", description = "Reject an acceptance request with reason")
    @PutMapping("/{id}/reject")
    public ResponseEntity<BaseResponse<AcceptanceRequestDTO>> rejectAcceptanceRequest(
            @PathVariable Integer id,
            @RequestParam String rejectionReason
    ) {
        log.info("Rejecting acceptance request with ID: {}", id);
        AcceptanceRequestDTO dto = acceptanceRequestService.rejectAcceptanceRequest(id, rejectionReason);

        return ResponseEntity.ok(BaseResponse.<AcceptanceRequestDTO>builder()
                .status(HttpStatus.OK.value())
                .message("Acceptance request rejected successfully")
                .data(dto)
                .build());
    }
}
