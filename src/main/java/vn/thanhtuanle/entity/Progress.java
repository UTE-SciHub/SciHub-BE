package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_progresses")
public class Progress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestone_id", nullable = false)
    private Milestone milestone;

    @Column(name = "progress_percent")
    private Integer progressPercent;

    @Column(name = "report", columnDefinition = "TEXT")
    private String report;

    private String documentUrl;
}
