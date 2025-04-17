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
@Table(name = "tbl_research_fields")
public class ResearchField extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Boolean delFlag;

    @OneToMany(mappedBy = "researchField")
    private List<Topic> topics;
}
