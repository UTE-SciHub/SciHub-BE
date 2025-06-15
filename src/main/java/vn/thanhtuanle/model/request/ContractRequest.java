package vn.thanhtuanle.model.request;

import lombok.*;
import vn.thanhtuanle.common.enums.ContractStatus;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequest {
    private Integer id;
    private String code;
    private String name;
    private String contractDetails;
    private LocalDate signedDate;
    private String idTopic;
    private ContractStatus status;
}
