package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.thanhtuanle.entity.AcceptanceRequest;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

public interface AcceptanceRequestRepository extends JpaRepository<AcceptanceRequest, Integer>, JpaSpecificationExecutor<AcceptanceRequest> {
    @Query("SELECT COUNT(ar) FROM AcceptanceRequest ar WHERE ar.topic.id = :topicId")
    Integer countByTopicId(@Param("topicId") String topicId);

    List<AcceptanceRequest> findByTopic(Topic topic);
}