package vn.thanhtuanle.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Constant {

    USER_PASSWORD_DEFAULT("Zaq12wsxcde3"),
    SUCCESSFULLY("Successfully!");

    String value;
}

