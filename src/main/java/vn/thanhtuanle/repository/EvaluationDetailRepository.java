package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.thanhtuanle.entity.EvaluationDetail;

public interface EvaluationDetailRepository extends JpaRepository<EvaluationDetail, Long> {
}