package vn.thanhtuanle.exception;

import lombok.*;
import vn.thanhtuanle.common.enums.ErrorCode;

@Getter
@Setter
public class AppException extends RuntimeException {

    private ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
