package vn.thanhtuanle.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RegistrationPeriodsStatus {
    OPEN("OPEN"),
    CLOSE("CLOSE"),
    REVIEWING("REVIEWING "),
    CANCELLED("CANCELLED "),;

    String value;
}
