package vn.thanhtuanle.model.request;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AcceptanceRequest {
    private String topicId;
    private LocalDate submissionDate;
    private String notes;
    private Boolean acknowledgment;
}
