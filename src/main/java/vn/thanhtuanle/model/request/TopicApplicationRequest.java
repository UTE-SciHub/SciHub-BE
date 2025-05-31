package vn.thanhtuanle.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicApplicationRequest {
    private String topicId;
    private String plan;
    private String motivation;
}
