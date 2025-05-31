package vn.thanhtuanle.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.thanhtuanle.common.enums.CouncilType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateCouncilRequest {

    @NotBlank(message = "Tên hội đồng không được để trống")
    private String name;

    @NotBlank(message = "Số quyết định không được để trống")
    private String decisionNumber;
    private LocalDate establishmentDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String notes;

    @NotNull(message = "Loại hội đồng không được để trống")
    private CouncilType type;

    @NotEmpty(message = "Danh sách thành viên không được để trống")
    private List<CouncilMemberRequest> members;

    @NotEmpty(message = "Danh sách đề tài không được để trống")
    private List<String> topics;
}
