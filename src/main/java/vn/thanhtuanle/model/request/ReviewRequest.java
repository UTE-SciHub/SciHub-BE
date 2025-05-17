package vn.thanhtuanle.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ReviewRequest {
    private Long councilId;
    private Integer milestoneId;
    private String comments;
}
