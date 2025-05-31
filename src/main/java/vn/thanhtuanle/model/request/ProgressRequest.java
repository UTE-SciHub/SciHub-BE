package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressRequest {
    @NotNull(message = "Milestone ID is required")
    private Integer milestoneId;

    @NotNull(message = "Progress percentage is required")
    @Min(value = 0, message = "Progress percentage must be at least 0")
    @Max(value = 100, message = "Progress percentage must be at most 100")
    private Integer progressPercent;

    private String report;

    private String documentUrl;
}