package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.TopicMemberRole;
import vn.thanhtuanle.entity.*;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.EvaluationDetailDTO;
import vn.thanhtuanle.model.dto.TopicApplicationDTO;
import vn.thanhtuanle.model.request.EvaluationDetailRequest;
import vn.thanhtuanle.repository.*;
import vn.thanhtuanle.service.EvaluationService;
import vn.thanhtuanle.service.UserService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationDetailRepository evaluationDetailRepository;
    private final TopicApplicationRepository topicApplicationRepository;
    private final CouncilMemberRepository councilMemberRepository;
    private final CouncilRepository councilRepository;
    private final TopicRepository topicRepository;
    private final TopicMembersRepository topicMembersRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private static final double PASSING_SCORE = 55.0;

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

        updateTotalScore(application, evaluationRequest.getCouncilId());

        EvaluationDetail savedEvaluationDetail = evaluationDetailRepository.save(evaluationDetail);

        return modelMapper.map(savedEvaluationDetail, EvaluationDetailDTO.class);
    }

    @Transactional
    @Override
    public List<TopicApplicationDTO> determinePrincipalInvestigator(Long councilId, String topicId) {
        // Bước 1: Kiểm tra và lấy thông tin hội đồng và đề tài
        councilRepository.findById(councilId)
                .orElseThrow(() -> new ResourceNotFoundException("Council not found", "id", councilId));
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found", "id", topicId));

        // Bước 2: Lấy tất cả đơn ứng tuyển của đề tài
        List<TopicApplication> applications = topicApplicationRepository.findAllByTopic(topic);
        if (applications.isEmpty()) {
            throw new ResourceNotFoundException("No applications found for topic", "topicId", topicId);
        }

        // Bước 3: Tính tổng điểm cho từng đơn dựa trên các đánh giá
        applications.forEach(app -> {
            List<EvaluationDetail> evaluations = app.getEvaluationDetails();
            if (evaluations == null || evaluations.isEmpty()) {
                app.setTotalScore(0.0);
                app.setPassed(false);
            } else {
                double totalScore = evaluations.stream()
                        .mapToDouble(eval -> {
                            int score = 0;
                            score += eval.getResearchOverviewScore() != null ? eval.getResearchOverviewScore() : 0;
                            score += eval.getUrgencyScore() != null ? eval.getUrgencyScore() : 0;
                            score += eval.getObjectiveScore() != null ? eval.getObjectiveScore() : 0;
                            score += eval.getApproachMethodScore() != null ? eval.getApproachMethodScore() : 0;
                            score += eval.getContentAndTimelineScore() != null ? eval.getContentAndTimelineScore() : 0;
                            score += eval.getProductScore() != null ? eval.getProductScore() : 0;
                            score += eval.getEffectivenessScore() != null ? eval.getEffectivenessScore() : 0;
                            score += eval.getExperienceScore() != null ? eval.getExperienceScore() : 0;
                            score += eval.getInstitutionCapabilityScore() != null ? eval.getInstitutionCapabilityScore() : 0;
                            score += eval.getBudgetScore() != null ? eval.getBudgetScore() : 0;
                            return score;
                        })
                        .sum();
                app.setTotalScore(totalScore);
                app.setPassed(totalScore >= PASSING_SCORE);
            }
            topicApplicationRepository.save(app);
        });

        // Bước 4: Chọn đơn có tổng điểm cao nhất làm chủ nhiệm
        TopicApplication principalApplication = applications.stream()
                .max(Comparator.comparingDouble(app -> app.getTotalScore() != null ? app.getTotalScore() : 0.0))
                .orElseThrow(() -> new ResourceNotFoundException("No valid applications for ranking", "topicId", topicId));

        // Gán người ứng tuyển của đơn cao điểm nhất làm chủ nhiệm
        topic.setPrincipalInvestigator(principalApplication.getUser().getEmail());
        topicRepository.save(topic);

        TopicMember topicMember = TopicMember.builder()
                .topic(topic)
                .user(principalApplication.getUser())
                .role(TopicMemberRole.INVESTIGATOR)
                .build();

        topicMembersRepository.save(topicMember);

        principalApplication.setStatus(ApplicationStatus.APPROVED);
        principalApplication.setPassed(true);
        topicApplicationRepository.save(principalApplication);

        applications.stream()
                .filter(app -> !app.getId().equals(principalApplication.getId()))
                .forEach(app -> {
                    app.setStatus(ApplicationStatus.REJECTED);
                    app.setPassed(false);
                    topicApplicationRepository.save(app);
                });

        // Bước 5: Sắp xếp danh sách theo tổng điểm giảm dần và chuyển đổi thành DTO
        List<TopicApplication> sortedApplications = applications.stream()
                .sorted((a1, a2) -> Double.compare(a2.getTotalScore() != null ? a2.getTotalScore() : 0.0,
                        a1.getTotalScore() != null ? a1.getTotalScore() : 0.0))
                .toList();

        return sortedApplications.stream()
                .map(app -> modelMapper.map(app, TopicApplicationDTO.class))
                .collect(Collectors.toList());
    }

    private void updateTotalScore(TopicApplication app, Long councilId) {
        List<EvaluationDetail> evaluations = evaluationDetailRepository.findByApplicationIdAndCouncilId(app.getId(), councilId);
        if (evaluations.isEmpty()) {
            app.setTotalScore(0.0);
            return;
        }

        double total = evaluations.stream()
                .mapToDouble(eval ->
                        (eval.getApproachMethodScore() != null ? eval.getApproachMethodScore() : 0) +
                                (eval.getBudgetScore() != null ? eval.getBudgetScore() : 0) +
                                (eval.getContentAndTimelineScore() != null ? eval.getContentAndTimelineScore() : 0) +
                                (eval.getEffectivenessScore() != null ? eval.getEffectivenessScore() : 0) +
                                (eval.getExperienceScore() != null ? eval.getExperienceScore() : 0) +
                                (eval.getInstitutionCapabilityScore() != null ? eval.getInstitutionCapabilityScore() : 0) +
                                (eval.getObjectiveScore() != null ? eval.getObjectiveScore() : 0) +
                                (eval.getProductScore() != null ? eval.getProductScore() : 0) +
                                (eval.getResearchOverviewScore() != null ? eval.getResearchOverviewScore() : 0) +
                                (eval.getUrgencyScore() != null ? eval.getUrgencyScore() : 0)
                )
                .sum();
        app.setTotalScore(total);
        topicApplicationRepository.save(app);
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
