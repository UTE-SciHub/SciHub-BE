package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.model.response.CouncilSimpleResponse;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO extends BaseDTO {
    private Integer id;
    private CouncilSimpleResponse council;
    private String comments;
    private Boolean delFlag;
}
