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
    TOKEN_EXPIRED(1403, "Token expired", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST(1400, "Invalid request", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    REGISTRATION_PERIOD_NOT_FOUND(1404, "Registration period not found", HttpStatus.NOT_FOUND),
    REGISTRATION_PERIOD_ALREADY_EXISTED(1405, "Registration period is already existed", HttpStatus.BAD_REQUEST),
    REGISTRATION_PERIOD_IS_CLOSED(1406, "Registration period is closed", HttpStatus.BAD_REQUEST),
    DATABASE_ERROR(1501, "Database error", HttpStatus.INTERNAL_SERVER_ERROR),
    MAPPING_ERROR(1502, "Mapping error", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN_ERROR(1403, "Forbidden", HttpStatus.FORBIDDEN),
    FILE_NOT_PROVIDED(1407, "File not provided", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_ERROR(1503, "File upload error", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SIZE_EXCEEDED(1408, "File size exceeded", HttpStatus.BAD_REQUEST),
    ;

    Integer code;
    String message;
    HttpStatusCode statusCode;
}
