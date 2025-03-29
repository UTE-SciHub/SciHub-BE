package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.entity.RegistrationPeriod;

import java.util.List;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, String>, JpaSpecificationExecutor<RegistrationPeriod> {
    RegistrationPeriod findTopByOrderByCreatedAtDesc();

    List<RegistrationPeriod> findByStatus(RegistrationPeriodsStatus status);
}
