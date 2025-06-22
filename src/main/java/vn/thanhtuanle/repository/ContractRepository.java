package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import vn.thanhtuanle.entity.Contract;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer>, JpaSpecificationExecutor<Contract> {

    List<Contract> findAllByTopic(Topic topic);

//    @Query("SELECT SUM(c.approvedBudget) FROM Contract c")
//    long sumApprovedBudget();
//
//    @Query("SELECT SUM(c.remainingBudget) FROM Contract c")
//    long sumRemainingBudget();
}
