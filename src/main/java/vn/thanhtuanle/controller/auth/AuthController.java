package vn.thanhtuanle.controller.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.thanhtuanle.common.enums.Constant;
import vn.thanhtuanle.model.request.LoginRequest;
import vn.thanhtuanle.model.request.RefreshTokenRequest;
import vn.thanhtuanle.model.response.AuthResponse;
import vn.thanhtuanle.model.response.BaseResponse;
import vn.thanhtuanle.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.LOGIN_SUCCESSFULLY.getValue())
                .data(authService.login(req))
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<BaseResponse<AuthResponse>> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.<AuthResponse>builder()
                .status(HttpStatus.OK.value())
                .message(Constant.SUCCESS.getValue())
                .data(authService.refreshToken(refreshToken.getRefreshToken()))
                .build());
    }
}
