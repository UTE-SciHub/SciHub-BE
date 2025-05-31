package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.common.enums.TopicMemberRole;
import vn.thanhtuanle.model.response.UserMemberResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicMemberDTO {
    private Long id;
    private UserMemberResponse user;
    private TopicMemberRole role;
}
