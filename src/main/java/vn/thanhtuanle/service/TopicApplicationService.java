package vn.thanhtuanle.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.model.dto.TopicApplicationDTO;
import vn.thanhtuanle.model.request.TopicApplicationRequest;
import vn.thanhtuanle.model.response.TopicApplicationResponse;

public interface TopicApplicationService {
    Page<TopicApplicationResponse> getAllApplications(String query, String periodId, TopicStatus status, Pageable pageable);

    TopicApplicationDTO getApplicationById(Long id);

    TopicApplicationDTO registerTopicApplication(TopicApplicationRequest request);

    void updateApplicationStatus(Long id, ApplicationStatus status, String notes);

    void deleteApplication(Long id);

    Page<TopicApplicationDTO> getApplicationsByTopic(String topicId, Pageable pageable);

    Page<TopicApplicationDTO> getApplicationsByTopicAndUser(String topicId, String userId, Pageable pageable);
}