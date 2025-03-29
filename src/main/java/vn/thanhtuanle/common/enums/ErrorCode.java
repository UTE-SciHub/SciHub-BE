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

    USER_NOT_FOUND(1404, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    INVALID_CREDENTIALS(1401, "Thông tin đăng nhập không chính xác", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1402, "Token không hợp lệ", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1403, "Token đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_REQUEST(1400, "Yêu cầu không hợp lệ", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR(1500, "Lỗi máy chủ nội bộ", HttpStatus.INTERNAL_SERVER_ERROR),
    REGISTRATION_PERIOD_NOT_FOUND(1404, "Không tìm thấy đợt đăng ký", HttpStatus.NOT_FOUND),
    REGISTRATION_PERIOD_ALREADY_EXISTED(1405, "Đợt đăng ký đã tồn tại", HttpStatus.BAD_REQUEST),
    REGISTRATION_PERIOD_IS_CLOSED(1406, "Đợt đăng ký đã đóng", HttpStatus.BAD_REQUEST),
    DATABASE_ERROR(1501, "Lỗi cơ sở dữ liệu", HttpStatus.INTERNAL_SERVER_ERROR),
    MAPPING_ERROR(1502, "Lỗi ánh xạ", HttpStatus.INTERNAL_SERVER_ERROR),
    FORBIDDEN_ERROR(1403, "Không có quyền truy cập", HttpStatus.FORBIDDEN),
    FILE_NOT_PROVIDED(1407, "Không có tệp được cung cấp", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_ERROR(1503, "Lỗi tải tệp lên", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SIZE_EXCEEDED(1408, "Kích thước tệp vượt quá giới hạn", HttpStatus.BAD_REQUEST),
    ;

    Integer code;
    String message;
    HttpStatusCode statusCode;
}
