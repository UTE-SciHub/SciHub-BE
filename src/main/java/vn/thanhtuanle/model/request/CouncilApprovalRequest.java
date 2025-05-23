package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouncilApprovalRequest {
    @NotNull(message = "Council ID is required")
    private Long councilId;

    @NotBlank(message = "Decision number is required")
    private String decisionNumber;

    @NotEmpty(message = "Topics list cannot be empty")
    private List<TopicApproval> topics;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicApproval {
        @NotBlank(message = "Topic ID is required")
        private String topicId;

        private long approvedBudget;
    }
}