package vn.thanhtuanle.model.response;

import lombok.*;
import vn.thanhtuanle.common.enums.CouncilType;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouncilSimpleResponse {
    private Long id;
    private String name;
    private String decisionNumber;
    private LocalDate establishmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;
    private CouncilType type;
    private Boolean delFlag;
}
