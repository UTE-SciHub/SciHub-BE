package vn.thanhtuanle.model.dto;

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

    private CouncilDTO council;

    private UserMemberResponse user;

    private CouncilMemberRole role;
}
