package vn.thanhtuanle.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {

    USER_NOT_FOUND(1404, "User not found", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(1401, "Invalid credentials", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1402, "Invalid token", HttpStatus.UNAUTHORIZED),
    ;

    Integer code;
    String message;
    HttpStatusCode statusCode;
}
