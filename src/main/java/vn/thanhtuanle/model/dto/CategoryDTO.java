package vn.thanhtuanle.model.dto;

import lombok.*;
import vn.thanhtuanle.entity.Topic;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO extends BaseDTO {

    private Integer id;
    private String name;
    private String description;
    private Integer level;
    private Boolean delFlag;
//    private List<TopicDTO> topics;
}
