package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_reviews")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "council_id")
    private Council council;

    @JoinColumn(name = "milestone_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Milestone milestone;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    private Boolean delFlag;
}
