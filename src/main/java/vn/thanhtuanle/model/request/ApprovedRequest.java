package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ApprovedRequest {

    @NotNull(message = "Approved status is required")
    private boolean approved;
}
