package vn.thanhtuanle.controller.user;

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
import vn.thanhtuanle.model.dto.ImportUserDTO;
import vn.thanhtuanle.model.request.UserRequest;
import vn.thanhtuanle.model.request.MultipleCreateUserRequest;
import vn.thanhtuanle.model.request.TokenRequest;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.PageResponse;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.service.UserService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Users List API", description = "Get paginated list of user with sorting and filtering")
    @GetMapping
    public ResponseEntity<BaseResponse<?>> getAll(
            @RequestParam(value = "p", defaultValue = "1") int page,
            @RequestParam(value = "s", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "tab", required = false) UserStatus status) {

        int adjustedPage = page - 1;
        if (adjustedPage < 0) adjustedPage = 0;

        Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(adjustedPage, size, direction, sort);

        Page<UserDTO> pageResult = userService.getAll(pageable, query, status);

        PageResponse<?> pageResponse = PageResponse.<List<UserDTO>>builder()
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

    @Operation(summary = "User API", description = "Create Users API")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> create(@Valid @RequestPart("data") UserRequest req,
                                    @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.<UserDTO>builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(userService.create(req, avatar))
                .build());
    }

    @Operation(summary = "User API", description = "Update user API")
    @PostMapping("/me")
    public ResponseEntity<?> me(@RequestBody TokenRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<UserDTO>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(userService.getCurrentUser(req.getToken()))
                .build());
    }

    @Operation(summary = "Multiple Create User API", description = "Create multiple users with list of id")
    @PostMapping("/multiple-create")
    public ResponseEntity<?> multipleCreate(@RequestBody MultipleCreateUserRequest req) {
        Map<String, Object> result = userService.multipleCreate(req);

        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.builder()
                .status(HttpStatus.CREATED.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(result)
                .build());
    }

    @Operation(summary = "Validate Users Import", description = "Upload a CSV/Excel file to validate user data.")
    @PostMapping("/validate-import")
    public ResponseEntity<Map<String, Object>> validateImport(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty() || (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx"))) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid file format. Only CSV & Excel are supported."));
        }

        return ResponseEntity.ok(userService.importUsers(file));
    }

    @Operation(summary = "Confirm Users Import", description = "Confirm and save validated users to the database.")
    @PostMapping("/confirm-import")
    public ResponseEntity<Map<String, Object>> confirmImport(@RequestBody List<ImportUserDTO> validUsers) {
        return ResponseEntity.ok(userService.saveUsers(validUsers));
    }

    @Operation(summary = "Export Excel API", description = "Export users list to excel file")
    @GetMapping("/export-excel")
    public ResponseEntity<?> exportExcel(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "status", required = false) UserStatus status,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order) {

        byte[] excelFile = userService.exportExcel(query, status, sort, order);

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = "users_" + timestamp + ".xlsx";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelFile);
    }

    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reset Password API", description = "Reset password for user")
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable("id") String id) {
        userService.resetPassword(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .build());
    }

    @Operation(summary = "Change status API", description = "Block/Unlock user")
    @PatchMapping("/{id}/status-change")
    public ResponseEntity<?> changeStatus(@PathVariable("id") String id,
                                          @RequestParam(value = "status") UserStatus status) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue()).data(userService.changeStatus(id, status))
                .build());
    }

    @Operation(summary = "Get all user not student API", description = "Get all user not student with optional search")
    @GetMapping("/not-student")
    public ResponseEntity<?> getAllUserNotStudent(@RequestParam(value = "q", required = false) String query) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<List<UserDTO>>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(userService.findAllUserNotStudent(query))
                .build());
    }

    @Operation(summary = "Get User by Email", description = "Retrieve a user by their email address")
    @GetMapping("/email")
    public ResponseEntity<BaseResponse<?>> getUserByEmail(@RequestParam("email") String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(user)
                .build());
    }
}
