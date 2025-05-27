package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import vn.thanhtuanle.model.dto.EvaluationDetailDTO;
import vn.thanhtuanle.model.dto.TopicApplicationDTO;
import vn.thanhtuanle.model.request.EvaluationDetailRequest;

import java.util.List;

public interface EvaluationService {
    @Transactional
    EvaluationDetailDTO submitEvaluation(Long applicationId, EvaluationDetailRequest evaluationRequest);

    @Transactional
    List<TopicApplicationDTO> determinePrincipalInvestigator(Long councilId, String topicId);

    EvaluationDetailDTO getEvaluationDetailByApplicationIdAndEvaluatorId(Long applicationId);
}
