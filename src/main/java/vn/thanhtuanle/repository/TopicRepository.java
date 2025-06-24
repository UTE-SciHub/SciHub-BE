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

    @Query("SELECT new map(YEAR(t.createdAt) as year, MONTH(t.createdAt) as month, COUNT(t) as count) " +
            "FROM Topic t WHERE YEAR(t.createdAt) = :year " +
            "GROUP BY YEAR(t.createdAt), MONTH(t.createdAt) " +
            "ORDER BY month")
    List<Map<String, Object>> countTopicRegistrationsByMonthForYear(@Param("year") int year);

    @Query("SELECT new map(tm.role as role, COUNT(DISTINCT tm.user) as count) " +
            "FROM TopicMember tm JOIN tm.topic t " +
            "WHERE YEAR(t.createdAt) = :year " +
            "GROUP BY tm.role")
    List<Map<String, Object>> countTopicParticipantsByRoleForYear(@Param("year") int year);

    @Query("SELECT new map(t.status as status, COUNT(t) as count) " +
            "FROM Topic t " +
            "WHERE t.status IN ('ACCEPTED', 'APPROVED', 'REJECTED', 'NEED_REVISION') " +
            "AND YEAR(t.updatedAt) = :year " +
            "GROUP BY t.status")
    List<Map<String, Object>> countTopicsByReviewStatusForYear(@Param("year") int year);

    @Query("SELECT AVG(DATEDIFF(t.updatedAt, t.createdAt)) " +
            "FROM Topic t " +
            "WHERE t.status IN ('ACCEPTED', 'APPROVED', 'REJECTED', 'NEED_REVISION') " +
            "AND YEAR(t.updatedAt) = :year")
    Double getAverageReviewTimeForYear(@Param("year") int year);

    @Query("SELECT new map(" +
            "CASE " +
            "  WHEN topic_avg.avg_progress >= 100 THEN 'AHEAD' " +
            "  WHEN topic_avg.avg_progress >= 50 THEN 'ON_TRACK' " +
            "  ELSE 'BEHIND' " +
            "END as progressStatus, " +
            "COUNT(topic_avg.topic_id) as count) " +
            "FROM (SELECT t.id as topic_id, AVG(p.progressPercent) as avg_progress " +
            "      FROM Topic t " +
            "      JOIN t.milestones m " +
            "      JOIN m.progresses p " +
            "      WHERE t.status IN ('APPROVED', 'IN_PROGRESS') " +
            "      AND YEAR(t.startDate) = :year " +
            "      GROUP BY t.id) topic_avg " +
            "GROUP BY CASE " +
            "  WHEN topic_avg.avg_progress >= 100 THEN 'AHEAD' " +
            "  WHEN topic_avg.avg_progress >= 50 THEN 'ON_TRACK' " +
            "  ELSE 'BEHIND' " +
            "END")
    List<Map<String, Object>> getTopicProgressStatusForYear(@Param("year") int year);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status IN :statuses AND YEAR(t.startDate) = :year")
    long countByStatusInAndYearOfStartDate(@Param("statuses") List<TopicStatus> statuses, @Param("year") int year);

    @Query("SELECT new map(t.id as topicId, t.vietnameseName as topicName, " +
            "t.startDate as startDate, " +
            "m.id as milestoneId, m.description as milestoneName, " +
            "m.expectedCompletionDate as dueDate, " +
            "COALESCE(MAX(p.progressPercent), 0) as progressPercent) " +
            "FROM Topic t " +
            "JOIN t.milestones m " +
            "LEFT JOIN m.progresses p " +
            "WHERE t.status IN ('APPROVED', 'IN_PROGRESS') " +
            "AND YEAR(t.startDate) = :year " +
            "GROUP BY t.id, t.vietnameseName, t.startDate, m.id, m.description, m.expectedCompletionDate " +
            "ORDER BY t.startDate, m.expectedCompletionDate")
    List<Map<String, Object>> getTopicMilestonesForGanttChart(@Param("year") int year);
}
