package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.Milestone;
import vn.thanhtuanle.entity.Progress;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {
    List<Progress> findByMilestone(Milestone milestone);
}