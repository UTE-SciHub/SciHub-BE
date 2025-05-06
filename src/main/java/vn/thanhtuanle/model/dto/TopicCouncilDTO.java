package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.common.enums.CouncilType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicCouncilDTO extends BaseDTO {
    private Long id;
    private TopicDTO topic;

    private CouncilDTO council;

    private CouncilType type;

    private String notes;
}
