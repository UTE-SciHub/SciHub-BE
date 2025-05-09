package vn.thanhtuanle.controller.council;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.CouncilDTO;
import vn.thanhtuanle.model.request.CreateCouncilRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.CouncilService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/councils")
@Tag(name = "Council Controller")
@RequiredArgsConstructor
public class CouncilController {

    private final CouncilService councilService;

    @Operation(summary = "Council List API", description = "Get paginated list of councils with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "delFlag", required = false) Boolean delFlag,
            @RequestParam(value = "isAdmin", required = false) Boolean isAdmin) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<CouncilDTO> pageResult = councilService.findAll(pageable, query, type, status, delFlag, isAdmin);

        PageResponse<?> pageResponse = PageResponse.<List<CouncilDTO>>builder()
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

    @Operation(summary = "Create Council API", description = "Create a new council with members and topics")
    @PostMapping
    public ResponseEntity<BaseResponse<?>> createCouncil(@Valid @RequestBody CreateCouncilRequest request) {
        CouncilDTO createdCouncil = councilService.createCouncil(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message("Council created successfully")
                .data(createdCouncil)
                .build());
    }

    @Operation(summary = "Get Council by ID API", description = "Get council details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> getCouncilById(@PathVariable Long id) {
        CouncilDTO council = councilService.getCouncilById(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Council retrieved successfully")
                .data(council)
                .build());
    }

    @Operation(summary = "Update Council API", description = "Update an existing council with members and topics")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> updateCouncil (
            @PathVariable Long id,
            @Valid @RequestBody CreateCouncilRequest request) {
        CouncilDTO updatedCouncil = councilService.updateCouncil(id, request);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Council updated successfully")
                .data(updatedCouncil)
                .build());
    }

    @Operation(summary = "Soft Delete Council API", description = "Soft delete a council by setting delFlag to true")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<?>> softDeleteCouncil(@PathVariable Long id) {
        councilService.softDeleteCouncil(id);

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Council soft deleted successfully")
                .build());
    }

    @Operation(summary = "Export Councils to Excel API", description = "Export councils to an Excel file with filtering options")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "delFlag", required = false) Boolean delFlag,
            @RequestParam(value = "isAdmin", required = false) Boolean isAdmin,
            @RequestParam(value = "includeMembers", defaultValue = "false") Boolean includeMembers,
            @RequestParam(value = "includeTopics", defaultValue = "false") Boolean includeTopics,
            @RequestParam(value = "selectedIds", required = false) List<Long> selectedIds) {

        byte[] excelData = councilService.exportExcel(query, type, status, sort, order, startDate, endDate, delFlag, isAdmin, includeMembers, includeTopics, selectedIds);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "councils.xlsx");
        headers.setContentLength(excelData.length);

        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
    }
}
