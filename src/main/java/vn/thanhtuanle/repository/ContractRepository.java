package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.entity.Contract;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer>, JpaSpecificationExecutor<Contract> {

    List<Contract> findAllByTopic(Topic topic);
}
