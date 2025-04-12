package vn.thanhtuanle.controller.registration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
import vn.thanhtuanle.model.request.UpdateRegistrationRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.RegistrationPeriodService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/registration-period")
@Tag(name = "Registration Controller")
@RequiredArgsConstructor
public class RegistrationPeriodController {
    private final RegistrationPeriodService registrationPeriodService;

    @Operation(summary = "Registration Period List API", description = "Get paginated list of registration periods with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "status", required = false) RegistrationPeriodsStatus status,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        int adjustedPage = Math.max(0, page - 1);
        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<RegistrationPeriodDTO> pageResult = registrationPeriodService.getAll(pageable, query, status, startDate, endDate);

        PageResponse<?> pageResponse = PageResponse.<List<RegistrationPeriodDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .currentPage(page)
                .totalPages(pageResult.getTotalPages())
                .totalItems(pageResult.getTotalElements())
                .itemsPerPage(size)
                .query(query)
                .data(pageResult.getContent())
                .build();

        return ResponseEntity.ok(pageResponse);
    }

    @Operation(summary = "Registration Period API", description = "Create a registration period")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<?>> create(
            @Valid @RequestPart("data") RegistrationPeriodRequest req,
            @RequestPart("decisionFile") MultipartFile decisionFile) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<RegistrationPeriodDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(registrationPeriodService.create(req, decisionFile))
                .build());
    }

    @Operation(summary = "Close Multiple Registration Periods", description = "Update status of multiple registration periods to CLOSED")
    @PatchMapping("/multiple-close")
    public ResponseEntity<BaseResponse<?>> closeMultiple(@RequestBody List<String> ids) {
        registrationPeriodService.closeMultiple(ids);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRegistrationById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(registrationPeriodService.getById(id))
                .build());
    }

    @Operation(summary = "Registration Period API", description = "Update a registration period")
    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<?>> update (
            @PathVariable String id,
            @Valid @RequestPart("data") UpdateRegistrationRequest req,
            @RequestPart(value = "decisionFile", required = false) MultipartFile decisionFile) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<RegistrationPeriodDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(registrationPeriodService.update(id, req, decisionFile))
                .build());
    }

    @Operation(summary = "Export Excel API", description = "Export registrations list to excel file")
    @GetMapping("/export-excel")
    public ResponseEntity<?> exportExcel(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "status", required = false) RegistrationPeriodsStatus status,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        byte[] excelFile = registrationPeriodService.exportExcel(query, status, sort, order, startDate, endDate);

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "registrations_" + timestamp + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }
}
