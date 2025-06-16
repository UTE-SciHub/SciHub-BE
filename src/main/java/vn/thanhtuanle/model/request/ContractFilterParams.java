package vn.thanhtuanle.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import vn.thanhtuanle.common.enums.ContractStatus;
import vn.thanhtuanle.common.enums.TopicStatus;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractFilterParams {
    private Integer p = 0;
    private Integer s = 10;
    private String sort = "createdDate";
    private String order = "desc";
    private String q;
    private ContractStatus status;
    private TopicStatus topicStatus;
    private Integer researchTypeId;
    private Integer researchFieldId;
    private Integer registrationPeriodId;
    private String departmentId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate signedDateFrom;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate signedDateTo;

    private Boolean delFlag;
}