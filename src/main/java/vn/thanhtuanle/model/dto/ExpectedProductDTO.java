package vn.thanhtuanle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpectedProductDTO {
    private String id;
    private String productName;
    private String criteria;
    private String description;
}
