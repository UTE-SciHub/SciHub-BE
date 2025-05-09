package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.entity.CouncilMember;
import vn.thanhtuanle.entity.EvaluationDetail;
import vn.thanhtuanle.entity.TopicApplication;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.EvaluationDetailDTO;
import vn.thanhtuanle.model.request.EvaluationDetailRequest;
import vn.thanhtuanle.repository.CouncilMemberRepository;
import vn.thanhtuanle.repository.EvaluationDetailRepository;
import vn.thanhtuanle.repository.TopicApplicationRepository;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.EvaluationService;
import vn.thanhtuanle.service.UserService;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationDetailRepository evaluationDetailRepository;
    private final TopicApplicationRepository topicApplicationRepository;
    private final CouncilMemberRepository councilMemberRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    private static final Map<String, CriteriaDetail> CRITERIA_DETAILS = new HashMap<>() {{
        put("researchOverviewScore", new CriteriaDetail("researchOverviewScore", "Tổng quan tình hình nghiên cứu thuộc lĩnh vực đề tài", 3, 10));
        put("urgencyScore", new CriteriaDetail("urgencyScore", "Tính cấp thiết của đề tài", 6, 10));
        put("objectiveScore", new CriteriaDetail("objectiveScore", "Mục tiêu của đề tài", 7, 10));
        put("approachMethodScore", new CriteriaDetail("approachMethodScore", "Cách tiếp cận và phương pháp nghiên cứu", 3, 5));
        put("contentAndTimelineScore", new CriteriaDetail("contentAndTimelineScore", "Nội dung nghiên cứu và tiến độ thực hiện", 10, 20));
        put("productScore", new CriteriaDetail("productScore", "Sản phẩm của đề tài", 12, 18));
        put("effectivenessScore", new CriteriaDetail("effectivenessScore", "Hiệu quả, phương thức chuyển giao kết quả nghiên cứu và khả năng ứng dụng", 5, 10));
        put("experienceScore", new CriteriaDetail("experienceScore", "Kinh nghiệm nghiên cứu, những thành tích nổi bật và năng lực quản lý", 3, 5));
        put("institutionCapabilityScore", new CriteriaDetail("institutionCapabilityScore", "Tiềm lực của cơ quan chủ trì đề tài", 3, 5));
        put("budgetScore", new CriteriaDetail("budgetScore", "Tính hợp lý của dự toán kinh phí đề nghị", 3, 7));
    }};

    @Getter
    @Setter
    @AllArgsConstructor
    private static class CriteriaDetail {
        private final String id;
        private final String name;
        private final int minScore;
        private final int maxScore;
    }

    @Transactional
    @Override
    public EvaluationDetailDTO submitEvaluation(Long applicationId, EvaluationDetailRequest evaluationRequest) {
        TopicApplication application = topicApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic application not found", "id", applicationId));

        User currentUser = userService.getCurrentUserEntity();
        CouncilMember councilMember = councilMemberRepository.findByUserIdAndCouncilId(currentUser.getEmail(), evaluationRequest.getCouncilId());

        Map<String, Integer> scores = getStringIntegerMap(evaluationRequest);

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            EvaluationServiceImpl.CriteriaDetail criteria = CRITERIA_DETAILS.get(entry.getKey());
            if (criteria == null) continue;
            int score = entry.getValue() != null ? entry.getValue() : 0;
            if (score < criteria.getMinScore() || score > criteria.getMaxScore()) {
                throw new AppException(ErrorCode.INVALID_SCORE);
            }
        }

        EvaluationDetail evaluationDetail = EvaluationDetail.builder()
                .evaluation(application)
                .councilMember(councilMember)
                .researchOverviewScore(evaluationRequest.getResearchOverviewScore())
                .urgencyScore(evaluationRequest.getUrgencyScore())
                .objectiveScore(evaluationRequest.getObjectiveScore())
                .approachMethodScore(evaluationRequest.getApproachMethodScore())
                .contentAndTimelineScore(evaluationRequest.getContentAndTimelineScore())
                .productScore(evaluationRequest.getProductScore())
                .effectivenessScore(evaluationRequest.getEffectivenessScore())
                .experienceScore(evaluationRequest.getExperienceScore())
                .institutionCapabilityScore(evaluationRequest.getInstitutionCapabilityScore())
                .budgetScore(evaluationRequest.getBudgetScore())
                .additionalComments(evaluationRequest.getAdditionalComments())
                .build();

        EvaluationDetail savedEvaluationDetail = evaluationDetailRepository.save(evaluationDetail);

        return modelMapper.map(savedEvaluationDetail, EvaluationDetailDTO.class);
    }

    private static Map<String, Integer> getStringIntegerMap(EvaluationDetailRequest evaluationRequest) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("researchOverviewScore", evaluationRequest.getResearchOverviewScore());
        scores.put("urgencyScore", evaluationRequest.getUrgencyScore());
        scores.put("objectiveScore", evaluationRequest.getObjectiveScore());
        scores.put("approachMethodScore", evaluationRequest.getApproachMethodScore());
        scores.put("contentAndTimelineScore", evaluationRequest.getContentAndTimelineScore());
        scores.put("productScore", evaluationRequest.getProductScore());
        scores.put("effectivenessScore", evaluationRequest.getEffectivenessScore());
        scores.put("experienceScore", evaluationRequest.getExperienceScore());
        scores.put("institutionCapabilityScore", evaluationRequest.getInstitutionCapabilityScore());
        scores.put("budgetScore", evaluationRequest.getBudgetScore());
        return scores;
    }
}
