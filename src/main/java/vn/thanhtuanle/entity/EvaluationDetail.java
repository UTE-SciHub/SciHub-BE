package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_evaluation_details")
public class EvaluationDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private TopicApplication evaluation;

    @ManyToOne
    @JoinColumn(name = "council_member_id")
    private CouncilMember councilMember;  // Liên kết với thành viên hội đồng

    @Column(name = "research_overview_score")
    private Integer researchOverviewScore;  // Tổng quan tình hình nghiên cứu thuộc lĩnh vực đề tài

    @Column(name = "urgency_score")
    private Integer urgencyScore;  // Tính cấp thiết của đề tài

    @Column(name = "objective_score")
    private Integer objectiveScore;  // Mục tiêu của đề tài

    @Column(name = "approach_method_score")
    private Integer approachMethodScore;  // Cách tiếp cận và phương pháp nghiên cứu

    @Column(name = "content_and_timeline_score")
    private Integer contentAndTimelineScore;  // Nội dung nghiên cứu và tiến độ thực hiện

    @Column(name = "product_score")
    private Integer productScore;  // Sản phẩm của đề tài

    @Column(name = "effectiveness_score")
    private Integer effectivenessScore;  // Hiệu quả, phương thức chuyển giao kết quả nghiên cứu và khả năng ứng dụng

    @Column(name = "experience_score")
    private Integer experienceScore;  // Kinh nghiệm nghiên cứu, những thành tích nổi bật và năng lực quản lý

    @Column(name = "institution_capability_score")
    private Integer institutionCapabilityScore;  // Tiềm lực của cơ quan chủ trì đề tài

    @Column(name = "budget_score")
    private Integer budgetScore;  // Tính hợp lý của dự toán kinh phí đề nghị

    @Column(columnDefinition = "TEXT")
    private String additionalComments;
}
