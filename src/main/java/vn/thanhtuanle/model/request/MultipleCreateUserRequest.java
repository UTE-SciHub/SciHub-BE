package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import vn.thanhtuanle.common.enums.RoleType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MultipleCreateUserRequest {
    @NotBlank(message = "Danh sách id không được để trống")
    private List<String> ids;

    @NotBlank(message = "Role không được để trống")
    private RoleType role;

    @NotBlank(message = "Tiền tố không được để trống")
    private String prefix;

    @NotBlank(message = "Hậu tố không được để trống")
    private String suffix;
}
