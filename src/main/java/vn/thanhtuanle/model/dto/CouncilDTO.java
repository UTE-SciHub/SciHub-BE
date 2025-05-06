package vn.thanhtuanle.model.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouncilDTO extends BaseDTO {
    private Long id;

    private String name;

    private String decisionNumber;

    private LocalDate establishmentDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private String notes;
}
