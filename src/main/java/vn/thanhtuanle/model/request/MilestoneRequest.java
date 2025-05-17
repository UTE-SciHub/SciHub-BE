package vn.thanhtuanle.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.thanhtuanle.common.enums.MilestoneStatus;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MilestoneRequest {
    private String topicId;
    private String description;
    private LocalDate expectedCompletionDate;
    private MilestoneStatus status;
}
