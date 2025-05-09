package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.entity.Council;

public interface CouncilRepository extends JpaRepository<Council, Long>, JpaSpecificationExecutor<Council> {

    boolean existsByName(String name);

    boolean existsByDecisionNumber(String decisionNumber);

    boolean existsByIdAndDecisionNumber(Long id, String decisionNumber);

    boolean existsByIdAndName(Long id, String name);
}
