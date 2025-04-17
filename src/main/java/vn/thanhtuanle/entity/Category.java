package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer level;
    private Boolean delFlag;

    @OneToMany(mappedBy = "category")
    private List<Topic> topics;
}
