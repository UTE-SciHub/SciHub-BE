package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentRequest {

    @NotEmpty(message = "Tên không được để trống")
    private String name;
    private String description;
    private String imageUrl;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    @NotEmpty(message = "Email không được để trống")
    private String email;
}
