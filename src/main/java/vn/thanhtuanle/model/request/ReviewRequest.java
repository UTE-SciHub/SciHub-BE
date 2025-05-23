package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    @NotNull(message = "Council ID is required")
    private Long councilId;

    @NotNull(message = "Milestone ID is required")
    private Integer milestoneId;

    @NotBlank(message = "Comments are required")
    private String comments;
}