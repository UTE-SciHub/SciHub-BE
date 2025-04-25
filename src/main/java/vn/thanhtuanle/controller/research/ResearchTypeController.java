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
import vn.thanhtuanle.model.dto.ResearchTypeDTO;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.ResearchTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/research-types")
@Tag(name = "Research Type Controller")
@RequiredArgsConstructor
public class ResearchTypeController {
    private final ResearchTypeService researchTypeService;

    @Operation(summary = "Research Type List API", description = "Get paginated list of research Types with sorting and filtering")
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

        Page<ResearchTypeDTO> pageResult = researchTypeService.findAll(pageable, query, delFlag);

        PageResponse<?> pageResponse = PageResponse.<List<ResearchTypeDTO>>builder()
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
    public ResponseEntity<BaseResponse<ResearchTypeDTO>> create(@Valid @RequestBody ResearchTypeDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<ResearchTypeDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(researchTypeService.createResearchType(req))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ResearchTypeDTO>> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ResearchTypeDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(researchTypeService.getResearchTypeById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ResearchTypeDTO>> update(@PathVariable Integer id, @Valid @RequestBody ResearchTypeDTO req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ResearchTypeDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(researchTypeService.updateResearchType(id, req))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Integer id) {
        researchTypeService.deleteResearchType(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<ResearchTypeDTO>> updateStatus(@PathVariable Integer id, @RequestBody Boolean isActive) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ResearchTypeDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(researchTypeService.updateResearchTypeStatus(id, isActive))
                .build());
    }
}
