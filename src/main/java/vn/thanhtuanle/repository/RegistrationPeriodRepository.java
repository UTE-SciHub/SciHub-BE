package vn.thanhtuanle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import vn.thanhtuanle.entity.RegistrationPeriod;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod, String>, JpaSpecificationExecutor<RegistrationPeriod> {
    RegistrationPeriod findTopByOrderByCreatedAtDesc();
}
