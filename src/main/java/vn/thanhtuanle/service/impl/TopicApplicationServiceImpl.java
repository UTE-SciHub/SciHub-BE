package vn.thanhtuanle.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.common.enums.ErrorCode;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.entity.Topic;
import vn.thanhtuanle.entity.TopicApplication;
import vn.thanhtuanle.entity.User;
import vn.thanhtuanle.exception.AppException;
import vn.thanhtuanle.exception.ResourceNotFoundException;
import vn.thanhtuanle.model.dto.TopicApplicationDTO;
import vn.thanhtuanle.model.request.TopicApplicationRequest;
import vn.thanhtuanle.model.response.TopicApplicationResponse;
import vn.thanhtuanle.repository.TopicApplicationRepository;
import vn.thanhtuanle.repository.TopicRepository;
import vn.thanhtuanle.service.TopicApplicationService;
import vn.thanhtuanle.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TopicApplicationServiceImpl implements TopicApplicationService {

    private final TopicApplicationRepository topicApplicationRepository;
    private final TopicRepository topicRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Page<TopicApplicationResponse> getAllApplications(String query, String periodId, TopicStatus status, Pageable pageable) {
        User currentUser = userService.getCurrentUserEntity();
        Specification<Topic> spec = Specification.where(null);

        if (status != null) {
            spec = spec.and((root, query1, cb) ->
                    cb.equal(root.get("status"), status));
        }

        if (periodId != null && !periodId.trim().isEmpty()) {
            spec = spec.and((root, query1, cb) ->
                    cb.equal(root.get("registrationPeriod").get("id"), periodId));
        }

        if (query != null && !query.trim().isEmpty()) {
            String searchPattern = "%" + query.toLowerCase() + "%";
            spec = spec.and((root, query1, cb) -> cb.or(
                    cb.like(cb.lower(root.get("vietnameseName")), searchPattern),
                    cb.like(cb.lower(root.get("englishName")), searchPattern)
            ));
        }

        Page<Topic> topics = topicRepository.findAll(spec, pageable);

        return topics.map(topic -> {
            Optional<TopicApplication> topicApplication = topicApplicationRepository.findByTopicAndUser(topic, currentUser);
            TopicApplicationResponse response = modelMapper.map(topic, TopicApplicationResponse.class);

            if(topicApplication.isPresent()) {
                response.setApplicationId(topicApplication.get().getId());
                response.setHasApplied(true);
                response.setApplicationStatus(topicApplication.get().getStatus());
            } else {
                response.setHasApplied(false);
                response.setApplicationStatus(null);
            }

            return response;
        });
    }

    @Override
    public TopicApplicationDTO getApplicationById(Long id) {
        TopicApplication application = topicApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
        return modelMapper.map(application, TopicApplicationDTO.class);
    }

    @Override
    public Page<TopicApplicationDTO> getApplicationsByTopic(String topicId, Pageable pageable) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));
        return topicApplicationRepository.findAllByTopic(topic, pageable)
                .map(application -> modelMapper.map(application, TopicApplicationDTO.class));
    }

    @Override
    public Page<TopicApplicationDTO> getApplicationsByTopicAndUser(String topicId, String userId, Pageable pageable) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "id", topicId));
        User user = userService.getUserById(userId);
        return topicApplicationRepository.findAllByTopicAndUser(topic, user, pageable)
                .map(application -> modelMapper.map(application, TopicApplicationDTO.class));
    }

    @Override
    public TopicApplicationDTO registerTopicApplication(TopicApplicationRequest request) {
        Topic topic = topicRepository.findById(request.getTopicId())
                .orElseThrow(() -> new AppException(ErrorCode.TOPIC_NOT_FOUND));
        User user = userService.getCurrentUserEntity();

        TopicApplication application = TopicApplication.builder()
                .topic(topic)
                .user(user)
                .status(ApplicationStatus.PENDING)
                .plan(request.getPlan())
                .motivation(request.getMotivation())
                .build();

        application = topicApplicationRepository.save(application);
        return modelMapper.map(application, TopicApplicationDTO.class);
    }

    @Override
    public void updateApplicationStatus(Long id, ApplicationStatus status, String notes) {
        TopicApplication application = topicApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));

        application.setStatus(status);
        application.setNotes(notes);
        topicApplicationRepository.save(application);
    }

    @Override
    public void deleteApplication(Long id) {
        if (!topicApplicationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Application", "id", id);
        }
        topicApplicationRepository.deleteById(id);
    }
}