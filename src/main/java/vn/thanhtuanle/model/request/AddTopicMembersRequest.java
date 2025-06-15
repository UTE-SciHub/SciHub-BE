package vn.thanhtuanle.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.thanhtuanle.common.enums.TopicMemberRole;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTopicMembersRequest {
    @Valid
    @NotEmpty(message = "Members list cannot be empty")
    private List<MemberEntry> members;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberEntry {
        @NotNull(message = "User ID cannot be null")
        private String userId;

        @NotNull(message = "Role cannot be null")
        private TopicMemberRole role;
    }
}