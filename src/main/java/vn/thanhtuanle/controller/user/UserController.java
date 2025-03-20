package vn.thanhtuanle.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.dto.UserDTO;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.model.response.UserResponse;
import vn.thanhtuanle.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "User API", description = "Users API")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserDTO req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<UserResponse>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.CREATED_SUCCESSFULLY.getValue())
                .data(userService.create(req))
                .build());
    }
}
