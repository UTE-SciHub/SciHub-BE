package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.entity.Topic;

import java.util.List;
import java.util.Map;

public interface TopicRepository extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {

    boolean existsByTopicCode(String topicCode);

    @Query("SELECT t.status AS status, COUNT(t) AS count FROM Topic t GROUP BY t.status")
    List<Object[]> countTopicsByStatus();

    @Query("SELECT d.name AS departmentName, COUNT(t) AS count " +
            "FROM Department d LEFT JOIN Topic t ON t.department = d " +
            "GROUP BY d.name")
    List<Object[]> countTopicsByDepartment();

    long count();

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status = 'IN_PROGRESS'")
    long countInProgressTopics();

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status = 'COMPLETED' OR t.status = 'ACCEPTED'")
    long countCompletedTopics();

    @Query("SELECT COALESCE(SUM(t.totalBudget), 0) FROM Topic t")
    long sumTotalBudget();

    @Query("SELECT t FROM Topic t JOIN t.members tm WHERE tm.user.id = :userId")
    List<Topic> findByUserId(@Param("userId") String userId);

    List<Topic> findByPrincipalInvestigator(String principalInvestigator);

    long countByStatus(TopicStatus status);

    @Query("SELECT new map(t.id as id, t.vietnameseName as vietnameseName, t.topicCode as topicCode, " +
            "size(t.applications) as applicationCount) " +
            "FROM Topic t WHERE t.status = 'SUBMITTED' OR t.status = 'UNDER_REVIEW' " +
            "ORDER BY t.createdAt DESC")
    List<Map<String, Object>> findPendingTopics();

    @Query("SELECT new map(c.name as name, COUNT(t) as count) " +
            "FROM Topic t JOIN t.category c GROUP BY c.name")
    List<Map<String, Object>> findCategoryDistribution();

    @Query("SELECT new map(rf.name as name, COUNT(t) as count) " +
            "FROM Topic t JOIN t.researchField rf GROUP BY rf.name")
    List<Map<String, Object>> findResearchFieldDistribution();

    @Query("SELECT new map(t.id as topicId, t.vietnameseName as topicName, " +
            "t.principalInvestigator as principalInvestigator, t.status as status) " +
            "FROM Topic t WHERE t.status = 'WAITING_FOR_ACCEPTANCE' OR t.status = 'ACCEPTANCE_REQUESTED'")
    List<Map<String, Object>> findPendingEvaluations();


}
