package vn.thanhtuanle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.model.dto.TopicDTO;
import vn.thanhtuanle.model.request.TopicCreateRequest;
import vn.thanhtuanle.model.response.TopicStatisticsResponse;

import java.time.LocalDate;

public interface TopicService {

    TopicDTO findById(String id);

    TopicDTO createTopic(TopicCreateRequest topicDTO) throws JsonProcessingException;

    TopicCreateRequest updateTopic(String id, TopicCreateRequest topicDTO);

    void changeStatus(String id, TopicStatus status);

    boolean existsByTopicCode(String topicCode);

    Page<TopicDTO> getAll(
            Pageable pageable,
            String query,
            TopicStatus status,
            Integer departmentId,
            Integer researchTypeId,
            Integer researchFieldId,
            Integer categoryId,
            LocalDate startDate,
            LocalDate endDate,
            Long minBudget,
            String investigator);

    TopicStatisticsResponse getTopicStatistics();
}
