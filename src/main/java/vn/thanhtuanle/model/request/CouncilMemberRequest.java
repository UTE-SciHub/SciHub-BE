package vn.thanhtuanle.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.thanhtuanle.common.enums.CouncilMemberRole;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CouncilMemberRequest {
    private String userId;
    private CouncilMemberRole role;
}
