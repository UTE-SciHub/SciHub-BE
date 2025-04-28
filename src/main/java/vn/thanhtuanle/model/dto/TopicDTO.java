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
public class TopicDTO {

    private String id;

    @NotBlank(message = "Vietnamese name is required")
    private String vietnameseName;

    private String englishName;

    private String principalInvestigator;

    private String objectives;

    private String mainContent;

    private String practicalApplications;

    private String expectedProducts;

    private String novelty;

    private String expectedRisks;

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

    private String registrationPeriod;

    private boolean commitment;

    private String additionalNotes;

    private DepartmentDTO department;

    private ResearchFieldDTO field;

    private ResearchTypeDTO researchType;

    private CategoryDTO category;

    private List<AttachedDocumentDTO> attachedDocuments;
}
