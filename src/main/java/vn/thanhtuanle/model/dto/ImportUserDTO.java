package vn.thanhtuanle.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.thanhtuanle.common.enums.ErrorType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImportUserDTO {
    @NotBlank(message = "ID không được để trống")
    private String id;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email chưa đúng định dạng")
    private String email;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @JsonProperty("phoneNumber")
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{10,15}", message = "Số điện thoại không đúng định dạng")
    private String phone;

    private String errorField;
    private ErrorType type;
}

