package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.thanhtuanle.common.enums.ApplicationStatus;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_topic_applications")
public class TopicApplication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String plan;
    private String motivation;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    private Double totalScore;
    private Boolean passed;

    private String notes;

    @OneToMany(mappedBy = "evaluation")
    private List<EvaluationDetail> evaluationDetails;
}
