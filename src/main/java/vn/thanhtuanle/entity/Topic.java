package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.thanhtuanle.common.enums.TopicStatus;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_topics")
public class Topic extends BaseEntity {

    @Id
    @Column(updatable = false, nullable = false)
    private String id;
    private String vietnameseName;
    private String englishName;
    private String objective;
    private String mainContent;
    private TopicStatus status;
    private LocalDate startDate;
    private int durationInMonths;
    private int endYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_field_id")
    private ResearchField researchField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_type_id")
    private ResearchType researchType;
}
