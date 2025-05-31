package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenRequest {

    @NotEmpty(message = "Refresh token không được để trống")
    String refreshToken;
}
