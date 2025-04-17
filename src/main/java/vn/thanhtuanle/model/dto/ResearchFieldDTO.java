package vn.thanhtuanle.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResearchFieldDTO extends BaseDTO {
    private Integer id;

    @NotEmpty(message = "Tên không được để trống")
    private String name;

    @NotEmpty(message = "Mô tả không được để trống")
    private String description;
    private Boolean delFlag;
}
