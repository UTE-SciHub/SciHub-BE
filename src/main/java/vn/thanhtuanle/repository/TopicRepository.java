package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {

    boolean existsByTopicCode(String topicCode);

    @Query("SELECT t.status AS status, COUNT(t) AS count FROM Topic t GROUP BY t.status")
    List<Object[]> countTopicsByStatus();

    // Count topics by department
    @Query("SELECT d.name AS departmentName, COUNT(t) AS count FROM Topic t JOIN t.department d GROUP BY d.name")
    List<Object[]> countTopicsByDepartment();

    // Total topics
    long count();

    // Count topics in IN_PROGRESS status
    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status = 'IN_PROGRESS'")
    long countInProgressTopics();

    // Count topics in COMPLETED status
    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status = 'COMPLETED'")
    long countCompletedTopics();

    // Sum of total budget
    @Query("SELECT COALESCE(SUM(t.totalBudget), 0) FROM Topic t")
    long sumTotalBudget();
}
