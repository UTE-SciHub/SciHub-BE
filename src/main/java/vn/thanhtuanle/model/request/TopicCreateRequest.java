package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import vn.thanhtuanle.common.enums.TopicStatus;
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
public class TopicCreateRequest {

    private String id;

    @NotBlank(message = "Vietnamese name is required")
    private String vietnameseName;

    private String englishName;

    private String principalInvestigator;

    private String objectives;

    private String mainContent;

    private String practicalApplications;

    @NotNull(message = "Expected products cannot be null")
    private ExpectedProductDTO expectedProducts;

    private String urgency;

    private String expectedRisks;

    @NotEmpty(message = "Keywords cannot be empty")
    private List<String> keywords = new ArrayList<>();

    private List<String> transferForm = new ArrayList<>();

    private TopicStatus status;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private int durationInMonths;

    private int endYear;

    private long totalBudget;

    private String fundingSource;

    private long remainingBudget;

    @NotEmpty(message = "Budget breakdown cannot be empty")
    private List<BudgetBreakdownDTO> budgetBreakdown = new ArrayList<>();

    private String council;

    private String registrationPeriod;

    private boolean commitment;

    private String additionalNotes;

    private String field;

    private String researchType;

    private String category;

    private List<AttachedDocumentCreation> attachedDocuments;
}