package vn.thanhtuanle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.thanhtuanle.common.enums.TopicMemberRole;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.entity.TopicMember;
import vn.thanhtuanle.model.dto.TopicDTO;
import vn.thanhtuanle.model.request.AssignToDepartmentRequest;
import vn.thanhtuanle.model.request.TopicCreateRequest;
import vn.thanhtuanle.model.response.TopicStatisticsResponse;

import java.time.LocalDate;
import java.util.List;

public interface TopicService {

    TopicDTO findById(String id);

    TopicDTO createTopic(TopicCreateRequest topicDTO) throws JsonProcessingException;

    TopicDTO updateTopic(String id, TopicCreateRequest topicDTO) throws JsonProcessingException;

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

    void addMemberToTopic(String topicId, String userId, TopicMemberRole role);

    @Transactional
    void addMembersToTopic(String topicId, List<String> userIds, TopicMemberRole role);

    List<TopicMember> getMembersOfTopic(String topicId);

    List<TopicDTO> getUserTopics();

    TopicDTO submitTopic(String topicId, String registrationPeriodId);

    void deleteTopicById(String topicId);

    @Transactional
    TopicDTO assignToDepartment(String topicId, AssignToDepartmentRequest request);

    Page<TopicDTO> getTopicsByDepartment(String departmentEmail, String query, Pageable pageable);

    @Transactional
    void approveTopic(String topicId, String notes);

    @Transactional
    void rejectTopic(String topicId, String notes);

    @Transactional
    void reviewTopic(String topicId, TopicStatus status, String notes);
}
