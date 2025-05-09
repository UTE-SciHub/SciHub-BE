package vn.thanhtuanle.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private CouncilDTO council;
    private String notes;
}
