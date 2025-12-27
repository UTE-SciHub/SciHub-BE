package vn.thanhtuanle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.thanhtuanle.service.DashboardService;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public Map<String, Object> getDashboardStatistics() {
        return dashboardService.getDashboardStatistics();
    }

    @GetMapping("/research-process")
    public Map<String, Object> getResearchProcessStatistics(@RequestParam(defaultValue = "0") int year) {
        if (year == 0) {
            year = LocalDate.now().getYear();
        }
        return dashboardService.getResearchProcessStatistics(year);
    }

    @GetMapping("/topic-reviews")
    public Map<String, Object> getTopicReviewStatistics(@RequestParam(defaultValue = "0") int year) {
        if (year == 0) {
            year = LocalDate.now().getYear();
        }
        return dashboardService.getTopicReviewStatistics(year);
    }

    @GetMapping("/topic-progress")
    public Map<String, Object> getTopicProgressStatistics(@RequestParam(defaultValue = "0") int year) {
        if (year == 0) {
            year = LocalDate.now().getYear();
        }
        return dashboardService.getTopicProgressStatistics(year);
    }
}