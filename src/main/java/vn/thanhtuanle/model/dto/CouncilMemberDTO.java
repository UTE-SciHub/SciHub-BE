package vn.thanhtuanle.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import vn.thanhtuanle.common.enums.CouncilMemberRole;
import vn.thanhtuanle.model.response.UserMemberResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouncilMemberDTO extends BaseDTO {

    private Long id;

    @JsonBackReference
    private CouncilDTO council;

    private UserMemberResponse user;

    private CouncilMemberRole role;
}
