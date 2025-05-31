package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.thanhtuanle.common.enums.Gender;
import vn.thanhtuanle.common.enums.RoleType;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotEmpty(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotEmpty(message = "Số điện thoại không được để trống")
    private String phoneNumber;

    private Gender gender;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dob;

    private RoleType role;
}
