package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.thanhtuanle.common.enums.TopicMemberRole;

@Entity
@Table(name = "tbl_topic_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private TopicMemberRole role;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;
}