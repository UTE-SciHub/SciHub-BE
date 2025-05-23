package vn.thanhtuanle.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgressDTO extends BaseDTO {
    private Integer id;
    private MilestoneDTO milestone;
    private Integer progressPercent;
    private String report;
    private String documentUrl;
}