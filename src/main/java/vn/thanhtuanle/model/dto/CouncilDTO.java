package vn.thanhtuanle.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import vn.thanhtuanle.common.enums.CouncilType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouncilDTO extends BaseDTO {
    private Long id;
    private String name;
    private String decisionNumber;
    private LocalDate establishmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    private CouncilType type;
    private Boolean delFlag;

    @JsonManagedReference
    private List<CouncilMemberDTO> councilMembers;

    @JsonManagedReference
    private List<TopicCouncilDTO> topicCouncils;
}
