package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.model.response.UserMemberResponse;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicApplicationDTO extends BaseDTO {
    private Long id;

    private TopicDTO topic;

    private UserMemberResponse user;

    private String plan;
    private String motivation;

    private ApplicationStatus status;

    private Double totalScore;
    private Boolean passed;

    private String notes;

    private Boolean hasEvaluated;

    private List<EvaluationDetailDTO> evaluationDetails;
}
