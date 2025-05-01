package vn.thanhtuanle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedProductDTO {
    private ScientificProductDTO scientific;
    private TrainingProductDTO training;
    private CommercialProductDTO commercial;
}
