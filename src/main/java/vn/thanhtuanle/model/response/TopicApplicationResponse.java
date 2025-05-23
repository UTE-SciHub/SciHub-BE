package vn.thanhtuanle.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.thanhtuanle.common.enums.ApplicationStatus;
import vn.thanhtuanle.common.enums.TopicStatus;
import vn.thanhtuanle.model.dto.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicApplicationResponse {
    private String id;
    private String vietnameseName;
    private String englishName;
    private String principalInvestigator;
    private TopicStatus status;
    private LocalDate startDate;
    private int durationInMonths;
    private DepartmentDTO department;
    private ResearchFieldDTO field;
    private ResearchTypeDTO researchType;
    private CategoryDTO category;
    private RegistrationPeriodDTO registrationPeriod;
    private ApplicationStatus applicationStatus;
    private Long applicationId;
    private boolean hasApplied;
}