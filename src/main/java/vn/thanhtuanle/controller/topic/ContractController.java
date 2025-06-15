package vn.thanhtuanle.controller.topic;

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
import vn.thanhtuanle.common.enums.ContractStatus;
import vn.thanhtuanle.model.dto.ContractDTO;
import vn.thanhtuanle.model.dto.ResearchTypeDTO;
import vn.thanhtuanle.model.request.ContractFilterParams;
import vn.thanhtuanle.model.request.ContractRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.service.ContractService;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/contracts")
@Tag(name = "Contract Controller")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @Operation(summary = "Contracts List API", description = "Get paginated list of contracts with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(ContractFilterParams params) {
        if (params.getP() != null) {
            params.setP(params.getP() - 1);
            if (params.getP() < 0) params.setP(0);
        }

        Page<ContractDTO> pageResult = contractService.findAll(params);

        PageResponse<?> pageResponse = PageResponse.<List<ContractDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .currentPage((params.getP() != null ? params.getP() : 0) + 1)
                .totalPages(pageResult.getTotalPages())
                .totalItems(pageResult.getTotalElements())
                .itemsPerPage(params.getS() != null ? params.getS() : 10)
                .query(params.getQ())
                .data(pageResult.getContent())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<ContractDTO>> create(
            @Valid @RequestPart ContractRequest req,
            @RequestPart(value = "contractFile", required = false) MultipartFile contractFile
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<ContractDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(contractService.createContract(req, contractFile))
                .build());
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<BaseResponse<ContractDTO>> update(
            @Valid @RequestPart ContractRequest req,
            @RequestPart(value = "contractFile", required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ContractDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(contractService.update(req, file))
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ContractDTO>> getById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<ContractDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(contractService.getContractById(id))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> delete(@PathVariable Integer id) {
        contractService.deleteContract(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(BaseResponse.<Void>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }
}
