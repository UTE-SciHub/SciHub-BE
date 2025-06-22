package vn.thanhtuanle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.repository.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final TopicRepository topicRepository;
    private final CouncilRepository councilRepository;
    private final UserRepository userRepository;
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final ContractRepository contractRepository;

    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> data = new HashMap<>();

        // Topics statistics
        long totalTopics = topicRepository.count();
        long submittedTopics = topicRepository.countByStatus(TopicStatus.SUBMITTED);
        long approvedTopics = topicRepository.countByStatus(TopicStatus.APPROVED);
        long rejectedTopics = topicRepository.countByStatus(TopicStatus.REJECTED);
        data.put("topics", Map.of(
                "total", totalTopics,
                "submitted", submittedTopics,
                "approved", approvedTopics,
                "rejected", rejectedTopics
        ));

        // Councils statistics
        long totalCouncils = councilRepository.countTotalCouncils();
        long activeCouncils = councilRepository.countActiveCouncils();
        long upcomingCouncils = councilRepository.countUpcomingCouncils();
        long closedCouncils = councilRepository.countClosedCouncils();
        data.put("councils", Map.of(
                "total", totalCouncils,
                "active", activeCouncils,
                "upcoming", upcomingCouncils,
                "closed", closedCouncils
        ));

        List<Map<String, Object>> activeCouncilsList = councilRepository.findActiveCouncils();
        data.put("activeCouncils", activeCouncilsList);

        // Users statistics
        long totalUsers = userRepository.count();
        long activeUsers = userRepository.countByStatus(UserStatus.ACTIVE);
        long inactiveUsers = userRepository.countByStatus(UserStatus.INACTIVE);
        data.put("users", Map.of(
                "total", totalUsers,
                "active", activeUsers,
                "inactive", inactiveUsers
        ));

        // Registration periods statistics
        long totalRegistrationPeriods = registrationPeriodRepository.count();
        long openRegistrationPeriods = registrationPeriodRepository.countByStatus(RegistrationPeriodsStatus.OPEN);
        long closedRegistrationPeriods = registrationPeriodRepository.countByStatus(RegistrationPeriodsStatus.CLOSED);
        data.put("registrationPeriods", Map.of(
                "total", totalRegistrationPeriods,
                "open", openRegistrationPeriods,
                "closed", closedRegistrationPeriods
        ));

        // Budget information
//        long approvedBudget = contractRepository.sumApprovedBudget();
//        long remainingBudget = contractRepository.sumRemainingBudget();
//        data.put("budgetInfo", Map.of(
//                "approved", approvedBudget,
//                "remaining", remainingBudget
//        ));

        // Category distribution
        List<Map<String, Object>> categoryDistribution = topicRepository.findCategoryDistribution();
        data.put("categoryDistribution", categoryDistribution);

        // Research field distribution
        List<Map<String, Object>> researchFieldDistribution = topicRepository.findResearchFieldDistribution();
        data.put("researchFieldDistribution", researchFieldDistribution);

        return data;
    }
}