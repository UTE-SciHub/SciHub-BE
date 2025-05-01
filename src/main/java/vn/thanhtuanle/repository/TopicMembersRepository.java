package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.TopicMember;

import java.util.List;

public interface TopicMembersRepository extends JpaRepository<TopicMember, Long> {

    List<TopicMember> findByTopicId(String topicId);

    List<TopicMember> findByUserId(String userId);

    boolean existsByTopicIdAndUserId(String topicId, String userId);

    TopicMember findByTopicIdAndUserId(String topicId, String userId);
}
