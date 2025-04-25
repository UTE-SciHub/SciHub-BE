package vn.thanhtuanle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.model.request.TopicCreateRequest;

public interface TopicService {
    Page<TopicCreateRequest> getAll(Pageable pageable, String query, TopicStatus status, Integer departmentId);

    TopicCreateRequest findById(String id);

    TopicCreateRequest createTopic(TopicCreateRequest topicDTO) throws JsonProcessingException;

    TopicCreateRequest updateTopic(String id, TopicCreateRequest topicDTO);

    void changeStatus(String id, TopicStatus status);
}
