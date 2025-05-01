package vn.thanhtuanle.model.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMemberResponse {
    private String email;
    private String name;
    private String phoneNumber;
    private String imageUrl;
}
