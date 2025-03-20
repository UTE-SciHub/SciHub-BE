package vn.thanhtuanle.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import vn.thanhtuanle.common.enums.RegistrationPeriodsStatus;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_registration_periods")
public class RegistrationPeriod extends BaseEntity {

    @Id
    private String id; // STT_UTE_YYYYMMDD_DK

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 100)
    private String decisionNumber;

    @Column(columnDefinition = "TEXT")
    private String decisionFile;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private RegistrationPeriodsStatus status;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;
}
