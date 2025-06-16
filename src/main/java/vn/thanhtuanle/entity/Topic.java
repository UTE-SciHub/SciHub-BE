package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.thanhtuanle.common.enums.TopicStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_topics")
public class Topic extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "vietnamese_name")
    private String vietnameseName;

    @Column(name = "english_name")
    private String englishName;

    @Column(name = "principal_investigator")
    private String principalInvestigator;

    @Lob
    @Column(name = "objectives", columnDefinition = "TEXT")
    private String objectives;

    @Lob
    @Column(name = "main_content", columnDefinition = "TEXT")
    private String mainContent;

    @Lob
    @Column(name = "practical_applications", columnDefinition = "TEXT")
    private String practicalApplications;

    @Lob
    @Column(name = "expected_products", columnDefinition = "JSON")
    private String expectedProducts;

    @Lob
    @Column(name = "urgency", columnDefinition = "TEXT")
    private String urgency;

    @Lob
    @Column(name = "expected_risks", columnDefinition = "TEXT")
    private String expectedRisks;

    @ElementCollection
    @CollectionTable(name = "topic_keywords", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "topic_transfer_forms", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "transfer_form")
    private List<String> transferForm = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "topic_files", joinColumns = @JoinColumn(name = "topic_id"))
    @Column(name = "file_path")
    private List<AttachedDocument> attachedDocuments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TopicStatus status;

    private LocalDate startDate;

    @Column(name = "duration_in_months")
    private int durationInMonths;

    @Column(name = "end_year")
    private int endYear;

    @Column(name = "topic_code", unique = true)
    private String topicCode;

    @Column(name = "total_budget")
    private long totalBudget;

    @Column(name = "funding_source")
    private String fundingSource;

    @Column(name = "approved_budget")
    private long approvedBudget;

    @Column(name = "remaining_budget")
    private long remainingBudget;

    @Lob
    @Column(name = "budget_breakdown", columnDefinition = "JSON")
    private String budgetBreakdown;

    @Column(name = "council")
    private String council;

    @Column(name = "approval_decision_code")
    private String approvalDecisionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_period_id", nullable = false)
    private RegistrationPeriod registrationPeriod;

    @Column(name = "commitment")
    private boolean commitment;

    @Lob
    @Column(name = "additional_notes", columnDefinition = "TEXT")
    private String additionalNotes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_field_id")
    private ResearchField researchField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_type_id")
    private ResearchType researchType;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicMember> members = new ArrayList<>();

    @Column(name = "rejection_reason")
    private String rejectionReason;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicCouncil> topicCouncils = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TopicApplication> applications = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Milestone> milestones = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AcceptanceRequest> acceptanceRequests = new ArrayList<>();

    @Embeddable
    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachedDocument {
        @Column(name = "file_path")
        private String filePath;

        @Column(name = "description")
        private String description;

        @Column(name = "public_id")
        private String publicId;

        @Column(name = "original_file_name")
        private String originalFileName;
    }
}