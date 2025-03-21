package vn.thanhtuanle.controller.registration;

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
import vn.thanhtuanle.model.dto.RegistrationPeriodDTO;
import vn.thanhtuanle.model.request.RegistrationPeriodRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.RegistrationPeriodService;

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
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @RequestParam(value = "q", required = false) String query) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<RegistrationPeriodDTO> pageResult = registrationPeriodService.getAll(pageable, query);

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

        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }

    @Operation(summary = "Registration Period API", description = "Create a registration period")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<?>> create(
            @Valid @RequestPart("data") RegistrationPeriodRequest req,
            @RequestPart("decisionFile") MultipartFile decisionFile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<RegistrationPeriodDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(registrationPeriodService.create(req, decisionFile))
                .build());
    }
}
