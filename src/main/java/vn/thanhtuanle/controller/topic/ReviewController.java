package vn.thanhtuanle.controller.topic;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.ReviewDTO;
import vn.thanhtuanle.model.request.ReviewRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/reviews")
@Tag(name = "Review Controller")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Get reviews by milestone", description = "Get all review records for a specific milestone")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> findAll(@RequestParam Integer milestoneId) {
        log.info("API request to get reviews for milestone ID: {}", milestoneId);

        return ResponseEntity.ok(BaseResponse.<List<ReviewDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(reviewService.findAll(milestoneId))
                .build());
    }

    @Operation(summary = "Get review by ID", description = "Get review details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> findById(@PathVariable Integer id) {
        log.info("API request to get review by id: {}", id);

        return ResponseEntity.ok(BaseResponse.<ReviewDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(reviewService.findById(id))
                .build());
    }

    @Operation(summary = "Create a new review", description = "Create a new review for a council and milestone")
    @PostMapping
    public ResponseEntity<BaseResponse<ReviewDTO>> create(@Valid @RequestBody ReviewRequest req) throws MessagingException {
        log.info("API request to create a new review");

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<ReviewDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(reviewService.create(req))
                .build());
    }

    @Operation(summary = "Update review", description = "Update an existing review record")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ReviewDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ReviewRequest req) {
        log.info("API request to update review with id: {}", id);

        return ResponseEntity.ok(BaseResponse.<ReviewDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(reviewService.update(id, req))
                .build());
    }

    @Operation(summary = "Delete review", description = "Soft delete a review record")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Integer id) {
        log.info("API request to delete review with id: {}", id);

        reviewService.delete(id);
        return ResponseEntity.ok(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }
}