package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

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

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status = 'COMPLETED'")
    long countCompletedTopics();

    @Query("SELECT COALESCE(SUM(t.totalBudget), 0) FROM Topic t")
    long sumTotalBudget();

    @Query("SELECT t FROM Topic t JOIN t.members tm WHERE tm.user.id = :userId")
    List<Topic> findByUserId(@Param("userId") String userId);
}
