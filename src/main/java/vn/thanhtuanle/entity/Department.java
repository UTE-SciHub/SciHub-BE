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
@Table(name = "tbl_departments")
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private String phoneNumber;
    private String email;
    private Boolean delFlag;
    private String logoPublicId;

    @OneToMany(mappedBy = "department")
    private List<Topic> topics;
}
