package vn.thanhtuanle.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.entity.CouncilMember;
import vn.thanhtuanle.entity.EvaluationDetail;
import vn.thanhtuanle.entity.TopicApplication;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.model.dto.EvaluationDetailDTO;
import vn.thanhtuanle.model.request.EvaluationDetailRequest;
import vn.thanhtuanle.repository.CouncilMemberRepository;
import vn.thanhtuanle.repository.EvaluationDetailRepository;
import vn.thanhtuanle.repository.TopicApplicationRepository;
import vn.thanhtuanle.repository.UserRepository;
import vn.thanhtuanle.service.EvaluationService;

import java.nio.file.attribute.UserPrincipal;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationDetailRepository evaluationDetailRepository;
    private final TopicApplicationRepository topicApplicationRepository;
    private final CouncilMemberRepository councilMemberRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public EvaluationDetailDTO submitEvaluation(String applicationId, EvaluationDetailRequest req) {
        TopicApplication application = topicApplicationRepository.findById(Long.parseLong(applicationId))
                .orElseThrow(() -> new IllegalArgumentException("Topic application not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal currentUser = (UserPrincipal) authentication.getPrincipal();
        CouncilMember councilMember = councilMemberRepository.findByUserIdAndCouncilId(currentUser.getName(), req.getCouncilId());

        EvaluationDetail evaluationDetail = modelMapper.map(req, EvaluationDetail.class);
        evaluationDetail.setEvaluation(application);

        EvaluationDetail saved = evaluationDetailRepository.save(evaluationDetail);
        return modelMapper.map(saved, EvaluationDetailDTO.class);
    }
}
