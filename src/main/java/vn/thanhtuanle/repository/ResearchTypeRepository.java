package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.entity.ResearchType;

public interface ResearchTypeRepository extends JpaRepository<ResearchType, Integer>, JpaSpecificationExecutor<ResearchType> {
}
