package vn.thanhtuanle.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Constant {

    USER_PASSWORD_DEFAULT("Zaq12wsxcde3"),
    SUCCESS("Thành công!"),
    CREATED_SUCCESSFULLY("Tạo mới thành công!"),
    LOGIN_SUCCESSFULLY("Đăng nhập thành công!"),;

    String value;
}

