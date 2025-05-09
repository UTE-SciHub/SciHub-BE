package vn.thanhtuanle.service;

import jakarta.transaction.Transactional;
import vn.thanhtuanle.model.dto.EvaluationDetailDTO;
import vn.thanhtuanle.model.request.EvaluationDetailRequest;

public interface EvaluationService {
    @Transactional
    EvaluationDetailDTO submitEvaluation(Long applicationId, EvaluationDetailRequest evaluationRequest);
}
