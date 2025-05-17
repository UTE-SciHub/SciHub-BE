package vn.thanhtuanle.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import vn.thanhtuanle.common.enums.TopicStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDTO extends BaseDTO {

    private String id;

    @NotBlank(message = "Vietnamese name is required")
    private String vietnameseName;

    private String englishName;

    private String principalInvestigator;

    private String objectives;

    private String mainContent;

    private String practicalApplications;

    private String expectedProducts;

    private String urgency;

    private String expectedRisks;

    private String approvalDecisionCode;

    @NotEmpty(message = "Keywords cannot be empty")
    private List<String> keywords = new ArrayList<>();

    private List<String> transferForm = new ArrayList<>();

    private TopicStatus status;

    private LocalDate startDate;

    private int durationInMonths;

    private int endYear;

    private String topicCode;

    private long totalBudget;

    private String fundingSource;

    private long approvedBudget;

    private long remainingBudget;

    private String budgetBreakdown;

    private String council;

    private String additionalNotes;

    private DepartmentDTO department;

    private ResearchFieldDTO field;

    private ResearchTypeDTO researchType;

    private CategoryDTO category;

    private RegistrationPeriodDTO registrationPeriod;

    private List<AttachedDocumentDTO> attachedDocuments;

    private List<TopicMemberDTO> members;

    private String rejectionReason;
}
