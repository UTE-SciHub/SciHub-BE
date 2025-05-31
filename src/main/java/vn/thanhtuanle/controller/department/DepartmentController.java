package vn.thanhtuanle.controller.department;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.model.dto.DepartmentDTO;
import vn.thanhtuanle.model.request.DepartmentRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.DepartmentService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Department Controller")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Department List API", description = "Get paginated list of departments with sorting and filtering")
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

        Page<DepartmentDTO> pageResult = departmentService.findAll(pageable, query, delFlag);

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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<DepartmentDTO>> createDepartment(
            @Valid @RequestPart("data") DepartmentRequest req,
            @RequestParam(value = "logoFile", required = false) MultipartFile logoFile) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<DepartmentDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(departmentService.createDepartment(req, logoFile))
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

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<DepartmentDTO>> updateDepartment(
            @PathVariable Integer id,
            @RequestPart("data") DepartmentDTO req,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<DepartmentDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(departmentService.updateDepartment(id, req, logoFile))
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

    @Operation(summary = "Export Excel API", description = "Export departments list to excel file")
    @GetMapping("/export-excel")
    public ResponseEntity<?> exportExcel(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "delFlag", required = false) Boolean delFlag) throws IOException {

        byte[] excelFile = departmentService.exportExcel(query, delFlag);

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "departments_" + timestamp + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }
}
