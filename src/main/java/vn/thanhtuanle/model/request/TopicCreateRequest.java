package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.entity.Category;
import vn.thanhtuanle.model.dto.AttachedDocumentDTO;
import vn.thanhtuanle.model.dto.BaseDTO;
import vn.thanhtuanle.model.dto.BudgetBreakdownDTO;
import vn.thanhtuanle.model.dto.ExpectedProductDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicCreateRequest extends BaseDTO {

    private String id;

    @NotBlank(message = "Vietnamese name is required")
    private String vietnameseName;

    private String englishName;

    private String principalInvestigator;

    private String objectives;

    private String mainContent;

    private String practicalApplications;

    @NotEmpty(message = "Expected products cannot be empty")
    private List<ExpectedProductDTO> expectedProducts = new ArrayList<>();

    private String novelty;

    private String expectedRisks;

    @NotEmpty(message = "Keywords cannot be empty")
    private List<String> keywords = new ArrayList<>();

    private List<String> transferForm = new ArrayList<>();

    private TopicStatus status;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private int durationInMonths;

    private int endYear;

    @NotBlank(message = "Topic code is required")
    private String topicCode;

    private long totalBudget;

    private String fundingSource;

    private long approvedBudget;

    private long remainingBudget;

    @NotEmpty(message = "Budget breakdown cannot be empty")
    private List<BudgetBreakdownDTO> budgetBreakdown = new ArrayList<>();

    private String council;

    private String registrationPeriod;

    private boolean commitment;

    private String additionalNotes;

    private String department;

    private String field;

    private String researchType;

    private String category;

    private List<AttachedDocumentDTO> attachedDocuments;
}