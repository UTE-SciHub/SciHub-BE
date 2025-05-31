package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.thanhtuanle.entity.Milestone;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

public interface MilestoneRepository extends JpaRepository<Milestone, Integer> {

    @Query("""
        SELECT m FROM Milestone m 
        JOIN FETCH m.topic t 
        WHERE t = :topic 
        AND m.delFlag = false
    """)
    List<Milestone> findByTopicAndDelFlagFalse(@Param("topic") Topic topic);

}
