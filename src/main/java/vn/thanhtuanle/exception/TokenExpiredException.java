package vn.thanhtuanle.exception;

import vn.thanhtuanle.common.enums.ErrorCode;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }
}
