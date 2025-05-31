package vn.thanhtuanle.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.entity.RegistrationPeriod;
import vn.thanhtuanle.repository.RegistrationPeriodRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class RegistrationPeriodScheduler {

    private final RegistrationPeriodRepository registrationPeriodRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void updateRegistrationPeriodStatus() {
        log.info("Running scheduled task to update registration period status...");

        LocalDate currentDate = LocalDate.now();
        List<RegistrationPeriod> openPeriods = registrationPeriodRepository.findByStatus(RegistrationPeriodsStatus.OPEN);

        if (openPeriods.isEmpty()) {
            log.info("No open registration periods found. Skip update.");
            return;
        }

        List<RegistrationPeriod> periodsToClose = openPeriods.stream()
                .filter(period -> period.getEndDate().isBefore(currentDate))
                .peek(period -> period.setStatus(RegistrationPeriodsStatus.CLOSED))
                .toList();

        if (periodsToClose.isEmpty()) {
            log.info("No registration periods need to be closed.");
            return;
        }

        registrationPeriodRepository.saveAll(periodsToClose);
        log.info("Updated {} registration periods to CLOSED.", periodsToClose.size());
    }
}
