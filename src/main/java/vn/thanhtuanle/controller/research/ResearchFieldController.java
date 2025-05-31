package vn.thanhtuanle.controller.research;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.ResearchFieldDTO;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.ResearchFieldService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/research-fields")
@Tag(name = "Research Field Controller")
@RequiredArgsConstructor
public class ResearchFieldController {
    private final ResearchFieldService researchFieldService;

    @Operation(summary = "Research Field List API", description = "Get paginated list of research fields with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "delFlag", required = false) Boolean delFlag) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<ResearchFieldDTO> pageResult = researchFieldService.findAll(pageable, query, delFlag);

        PageResponse<?> pageResponse = PageResponse.<List<ResearchFieldDTO>>builder()
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

    @PostMapping
    public ResponseEntity<BaseResponse<ResearchFieldDTO>> create(@Valid @RequestBody ResearchFieldDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<ResearchFieldDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(researchFieldService.createResearchField(req))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResearchFieldDTO>> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ResearchFieldDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(researchFieldService.getResearchFieldById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ResearchFieldDTO>> update(@PathVariable Integer id, @Valid @RequestBody ResearchFieldDTO req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ResearchFieldDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(researchFieldService.updateResearchField(id, req))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Integer id) {
        researchFieldService.deleteResearchField(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ResearchFieldDTO>> updateStatus(@PathVariable Integer id, @RequestBody Boolean isActive) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ResearchFieldDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(researchFieldService.updateResearchFieldStatus(id, isActive))
                .build());
    }
}
