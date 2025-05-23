package vn.thanhtuanle.controller.topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.ProgressDTO;
import vn.thanhtuanle.model.request.ProgressRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.service.ProgressService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/progress")
@Tag(name = "Progress Controller")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @Operation(summary = "Get all progress records for a milestone", description = "Get all progress records for a milestone")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> findAll(@RequestParam Integer milestoneId) {
        return ResponseEntity.ok(BaseResponse.<List<ProgressDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(progressService.findAll(milestoneId))
                .build());
    }

    @Operation(summary = "Get progress by ID", description = "Get progress details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(BaseResponse.<ProgressDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(progressService.findById(id))
                .build());
    }

    @Operation(summary = "Create a new progress record", description = "Create a new progress record for a milestone")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<ProgressDTO>> create(
            @Valid @RequestPart("data") ProgressRequest req,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<ProgressDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(progressService.create(req, file))
                .build());
    }

    @Operation(summary = "Update progress", description = "Update an existing progress record")
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<ProgressDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestPart("data") ProgressRequest req,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        return ResponseEntity.ok(BaseResponse.<ProgressDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(progressService.update(id, req, file))
                .build());
    }

    @Operation(summary = "Delete progress", description = "Delete a progress record")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Integer id) {
        progressService.delete(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }
}