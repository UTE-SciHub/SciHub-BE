package vn.thanhtuanle.model.request;

import lombok.Data;

@Data
public class EvaluationDetailRequest {
    private Long councilId;
    private Integer researchOverviewScore;
    private Integer urgencyScore;
    private Integer objectiveScore;
    private Integer approachMethodScore;
    private Integer contentAndTimelineScore;
    private Integer productScore;
    private Integer effectivenessScore;
    private Integer experienceScore;
    private Integer institutionCapabilityScore;
    private Integer budgetScore;
    private String additionalComments;
    private Integer totalScore;
    private boolean passedAssessment;
    private Integer approveCount;
    private Integer rejectCount;
    private Integer totalPresent;
    private Integer totalAbsent;
}

