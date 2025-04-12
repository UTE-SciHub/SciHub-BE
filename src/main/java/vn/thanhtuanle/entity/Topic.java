package vn.thanhtuanle.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@Table(name = "tbl_topics")
public class Topic extends BaseEntity {

    private String name;

    private String description;

    private String imageUrl;

    private String slug;

    private String status;

    private String type;
}
