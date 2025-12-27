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
import java.util.stream.Collectors;

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

        // Category distribution
        List<Map<String, Object>> categoryDistribution = topicRepository.findCategoryDistribution();
        data.put("categoryDistribution", categoryDistribution);

        // Research field distribution
        List<Map<String, Object>> researchFieldDistribution = topicRepository.findResearchFieldDistribution();
        data.put("researchFieldDistribution", researchFieldDistribution);

        return data;
    }

    public Map<String, Object> getResearchProcessStatistics(int year) {
        Map<String, Object> data = new HashMap<>();

        List<Map<String, Object>> topicRegistrations = topicRepository.countTopicRegistrationsByMonthForYear(year);
        data.put("topicRegistrations", topicRegistrations);

        List<Map<String, Object>> participantsByRole = topicRepository.countTopicParticipantsByRoleForYear(year);
        data.put("participantsByRole", participantsByRole);

        return data;
    }

    public Map<String, Object> getTopicReviewStatistics(int year) {
        Map<String, Object> data = new HashMap<>();

        // Count of topics by review status
        List<Map<String, Object>> reviewStatuses = topicRepository.countTopicsByReviewStatusForYear(year);
        data.put("reviewStatuses", reviewStatuses);

        // Average time from registration to review
        Double averageReviewTime = topicRepository.getAverageReviewTimeForYear(year);
        data.put("averageReviewTimeInDays", averageReviewTime != null ? averageReviewTime : 0);

        return data;
    }

    public Map<String, Object> getTopicProgressStatistics(int year) {
        Map<String, Object> data = new HashMap<>();

        // Get total count of active topics for the year
        long totalActiveTopics = topicRepository.countByStatusInAndYearOfStartDate(
                List.of(TopicStatus.APPROVED, TopicStatus.IN_PROGRESS), year);

        // Get progress status distribution
        List<Map<String, Object>> progressStatus = topicRepository.getTopicProgressStatusForYear(year);

        // Create a complete progress status map with all categories
        Map<String, Long> progressStatusMap = new HashMap<>();
        progressStatusMap.put("AHEAD", 0L);
        progressStatusMap.put("ON_TRACK", 0L);
        progressStatusMap.put("BEHIND", 0L);

        // Fill in actual counts where available
        for (Map<String, Object> status : progressStatus) {
            String category = (String) status.get("progressStatus");
            Long count = ((Number) status.get("count")).longValue();
            progressStatusMap.put(category, count);
        }

        // Convert to list format expected by frontend
        List<Map<String, Object>> completeProgressStatus = progressStatusMap.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> statusMap = new HashMap<>();
                    statusMap.put("progressStatus", entry.getKey());
                    statusMap.put("count", entry.getValue());
                    return statusMap;
                })
                .collect(Collectors.toList());

        data.put("progressStatus", completeProgressStatus);
        data.put("totalActiveTopics", totalActiveTopics);

        // Get milestone data for Gantt chart
        List<Map<String, Object>> milestonesForGantt = topicRepository.getTopicMilestonesForGanttChart(year);
        data.put("milestonesForGantt", milestonesForGantt);
        data.put("totalMilestones", milestonesForGantt.size());

        return data;
    }
}