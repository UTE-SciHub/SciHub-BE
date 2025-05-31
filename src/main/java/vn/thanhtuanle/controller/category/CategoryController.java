package vn.thanhtuanle.controller.category;

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
import vn.thanhtuanle.model.dto.CategoryDTO;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@Tag(name = "Category Controller")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Category List API", description = "Get paginated list of categories with sorting and filtering")
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

        Page<CategoryDTO> pageResult = categoryService.findAll(pageable, query, delFlag);

        PageResponse<?> pageResponse = PageResponse.<List<CategoryDTO>>builder()
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
    public ResponseEntity<BaseResponse<CategoryDTO>> create(@Valid @RequestBody CategoryDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<CategoryDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(categoryService.createCategory(req))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryDTO>> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<CategoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(categoryService.getCategoryById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryDTO>> update(@PathVariable Integer id, @Valid @RequestBody CategoryDTO req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<CategoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(categoryService.updateCategory(id, req))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> delete(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<CategoryDTO>> updateStatus(@PathVariable Integer id, @RequestBody Boolean isActive) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<CategoryDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(categoryService.updateCategoryStatus(id, isActive))
                .build());
    }
}
