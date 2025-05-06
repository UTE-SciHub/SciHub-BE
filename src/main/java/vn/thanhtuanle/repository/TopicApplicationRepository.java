package vn.thanhtuanle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.entity.Topic;
import vn.thanhtuanle.entity.TopicApplication;
import vn.thanhtuanle.entity.User;

import java.util.Optional;

public interface TopicApplicationRepository extends JpaRepository<TopicApplication, Long>, JpaSpecificationExecutor<TopicApplication> {

    Page<TopicApplication> findAllByStatusAndUser(ApplicationStatus status, User user, Pageable pageable);

    Page<TopicApplication> findAllByUser(User user, Pageable pageable);

    Optional<TopicApplication> findByTopicAndUser(Topic topic, User user);

    Page<TopicApplication> findAllByTopic(Topic topic, Pageable pageable);

    Page<TopicApplication> findAllByTopicAndUser(Topic topic, User user, Pageable pageable);

    boolean existsByTopicAndUser(Topic topic, User user);
}