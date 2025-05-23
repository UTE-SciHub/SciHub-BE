package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AssignToDepartmentRequest {
    @NotBlank(message = "Mã đơn vị không được để trống")
    private String departmentId;
    private String notes;
}
