package vn.thanhtuanle.controller.topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.MilestoneDTO;
import vn.thanhtuanle.model.request.MilestoneRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.service.MilestoneService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/milestones")
@Tag(name = "Milestone Controller")
@RequiredArgsConstructor
public class MilestoneController {

    private final MilestoneService milestoneService;

    @Operation(summary = "Get all milestones", description = "Get all milestones")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> findAll(@RequestParam String topicId) {
        return ResponseEntity.ok(BaseResponse.<List<MilestoneDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(milestoneService.findAll(topicId))
                .build());
    }

    @Operation(summary = "Create a new milestone", description = "Create a new milestone")
    @PostMapping
    public ResponseEntity<BaseResponse<MilestoneDTO>> create(@Valid @RequestBody MilestoneRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<MilestoneDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(milestoneService.create(req))
                .build());
    }

    @Operation(summary = "Update a milestone", description = "Update an existing milestone")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<MilestoneDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody MilestoneRequest req) {
        return ResponseEntity.ok(BaseResponse.<MilestoneDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(milestoneService.update(id, req))
                .build());
    }
}
