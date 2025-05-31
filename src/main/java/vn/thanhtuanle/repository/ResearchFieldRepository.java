package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.entity.ResearchField;

public interface ResearchFieldRepository extends JpaRepository<ResearchField, Integer>, JpaSpecificationExecutor<ResearchField> {
}
