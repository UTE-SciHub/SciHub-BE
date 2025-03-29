package vn.thanhtuanle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDTO extends BaseDTO {
    private Integer id;
    private String name;
    private String description;
    private String imageUrl;
    private String phoneNumber;
    private String email;
    private Boolean delFlag;
}
