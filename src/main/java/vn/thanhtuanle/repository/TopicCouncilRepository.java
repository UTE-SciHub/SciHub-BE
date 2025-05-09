package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.Council;
import vn.thanhtuanle.entity.TopicCouncil;

import java.util.List;

public interface TopicCouncilRepository extends JpaRepository<TopicCouncil, Long> {

    List<TopicCouncil> findByCouncil(Council council);
}
