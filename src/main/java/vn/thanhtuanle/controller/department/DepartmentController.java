package vn.thanhtuanle.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.model.request.DepartmentRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Department Controller")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Registration Period List API", description = "Get paginated list of registration periods with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "q", required = false) String query) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<DepartmentDTO> pageResult = departmentService.findAll(pageable, query);

        PageResponse<?> pageResponse = PageResponse.<List<DepartmentDTO>>builder()
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
    public ResponseEntity<BaseResponse<DepartmentDTO>> createDepartment(@RequestBody DepartmentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<DepartmentDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(departmentService.createDepartment(req))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DepartmentDTO>> getDepartmentById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<DepartmentDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(departmentService.getDepartmentById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<DepartmentDTO>> updateDepartment(@PathVariable Integer id, @RequestBody DepartmentDTO req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<DepartmentDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(departmentService.updateDepartment(id, req))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteDepartment(@PathVariable Integer id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }
}
