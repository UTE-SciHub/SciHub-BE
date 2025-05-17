package vn.thanhtuanle.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MilestoneDTO extends BaseDTO {
    private Integer id;
    private TopicDTO topic;
    private String description;
    private LocalDate expectedCompletionDate;
    private String status;
    private Boolean delFlag;
    private List<ProgressDTO> progressReports;
}
