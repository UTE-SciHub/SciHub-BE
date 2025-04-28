package vn.thanhtuanle.controller.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import vn.thanhtuanle.model.request.AttachedDocumentCreation;
import vn.thanhtuanle.model.request.TopicCreateRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.TopicService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
            @RequestParam(value = "investigator", required = false) String investigator) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<TopicDTO> pageResult = topicService.getAll(pageable, query, status, departmentId, researchTypeId, researchFieldId, categoryId, startDate, endDate, minBudget, investigator);

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
     public ResponseEntity<BaseResponse<?>> createTopic (
             @Valid @RequestPart(value = "data") TopicCreateRequest topicCreateRequest,
             @RequestPart(value = "files") List<MultipartFile> files,
             @RequestPart(value = "descriptions") List<String> descriptions
     ) throws JsonProcessingException {
         log.info("Received topic JSON: {}", topicCreateRequest);

         List<AttachedDocumentCreation> attachedDocuments = new ArrayList<>();
         if (files != null && descriptions != null && files.size() == descriptions.size()) {
             for (int i = 0; i < files.size(); i++) {
                 AttachedDocumentCreation dto = new AttachedDocumentCreation();
                 dto.setFile(files.get(i));
                 dto.setDescription(descriptions.get(i));
                 attachedDocuments.add(dto);
             }
         }

         if (!attachedDocuments.isEmpty()) {
             topicCreateRequest.setAttachedDocuments(attachedDocuments);

             if (topicCreateRequest.getAttachedDocuments().size() != attachedDocuments.size()) {
                 log.error("Mismatch between attached documents and uploaded files");
                 return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                         .status(HttpStatus.BAD_REQUEST.value())
                         .message("Number of files does not match number of attached documents")
                         .data(null)
                         .build());
             }
         }

         log.info("Creating new topic with name: {}", topicCreateRequest.getVietnameseName());

         return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.builder()
                 .status(HttpStatus.CREATED.value())
                 .message(Constant.SUCCESS.getValue())
                 .data(topicService.createTopic(topicCreateRequest))
                 .build());
     }

    @Operation(summary = "Update Topic API", description = "Update an existing topic")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> updateTopic(@PathVariable String id, @RequestBody TopicCreateRequest topicDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(topicService.updateTopic(id, topicDTO))
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
}
