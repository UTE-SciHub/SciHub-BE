package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationPeriodDTO extends BaseDTO {
    private String id;
    private String title;
    private String decisionNumber;
    private String decisionFile;
    private LocalDate startDate;
    private LocalDate endDate;
    private RegistrationPeriodsStatus status;
    private String description;
    private String imageUrl;
}
