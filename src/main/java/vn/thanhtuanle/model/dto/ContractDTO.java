package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.common.enums.ContractStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDTO extends BaseDTO {

    private Integer id;

    private String code;

    private String name;

    private ContractStatus status;

    private String contractDetails;

    private LocalDate signedDate;

    private String contractPath;
}
