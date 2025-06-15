package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.thanhtuanle.common.enums.ContractStatus;

import java.time.LocalDate;

@Entity
@Table(name = "tbl_contracts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "contract_details", columnDefinition = "TEXT")
    private String contractDetails;

    @Column(name = "signed_date")
    private LocalDate signedDate;

    @Column(name = "contract_path")
    private String contractPath;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    private Boolean delFlag = false;
}
