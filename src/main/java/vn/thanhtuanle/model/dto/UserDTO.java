package vn.thanhtuanle.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import vn.thanhtuanle.common.enums.Gender;
import vn.thanhtuanle.common.enums.UserStatus;
import vn.thanhtuanle.entity.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO extends BaseDTO {
    private String id;
    private String email;
    private String name;
    private String phoneNumber;
    private String imageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLogin;
    private UserStatus status;
    private Gender gender;
    private LocalDate dob;
    private Set<Role> roles;
}
