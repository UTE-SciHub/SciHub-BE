package vn.thanhtuanle.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicStatisticsResponse {

    private long totalTopics;
    private long inProgressCount;
    private long completedCount;
    private long totalBudget;
    private List<StatusCount> statusDistribution;
    private List<DepartmentCount> departmentDistribution;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StatusCount {
        private String status;
        private long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepartmentCount {
        private String departmentName;
        private long count;
    }
}