package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.entity.CouncilMember;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationDetailDTO extends BaseDTO {
    private Long id;
//    private TopicApplicationDTO evaluation;
    private CouncilMemberDTO councilMember;
    private Integer researchOverviewScore;  // Tổng quan tình hình nghiên cứu thuộc lĩnh vực đề tài
    private Integer urgencyScore;  // Tính cấp thiết của đề tài
    private Integer objectiveScore;  // Mục tiêu của đề tài
    private Integer approachMethodScore;  // Cách tiếp cận và phương pháp nghiên cứu
    private Integer contentAndTimelineScore;  // Nội dung nghiên cứu và tiến độ thực hiện
    private Integer productScore;  // Sản phẩm của đề tài
    private Integer effectivenessScore;  // Hiệu quả, phương thức chuyển giao kết quả nghiên cứu và khả năng ứng dụng
    private Integer experienceScore;  // Kinh nghiệm nghiên cứu, những thành tích nổi bật và năng lực quản lý
    private Integer institutionCapabilityScore;  // Tiềm lực của cơ quan chủ trì đề tài
    private Integer budgetScore;  // Tính hợp lý của dự toán kinh phí đề nghị
    private String additionalComments;
}
