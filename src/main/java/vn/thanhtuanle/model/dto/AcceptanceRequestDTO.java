package vn.thanhtuanle.model.dto;

import vn.thanhtuanle.common.enums.AcceptanceStatus;
import vn.thanhtuanle.model.response.CouncilSimpleResponse;
import vn.thanhtuanle.model.response.TopicAcceptanceResponse;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptanceRequestDTO extends BaseDTO {
    private Integer id;
    private TopicAcceptanceResponse topic;
    private AcceptanceStatus status;
    private LocalDate submissionDate;
    private String notes;
    private Boolean acknowledgment;
    private Integer attemptNumber;
    private Boolean isFinal;
    private List<DocumentDTO> documents;
    private CouncilSimpleResponse council;
}
