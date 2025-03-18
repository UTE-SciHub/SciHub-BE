package vn.thanhtuanle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import vn.thanhtuanle.model.ValidationError;
import vn.thanhtuanle.model.response.BaseResponse;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<BaseResponse<Object>> handleValidationExceptions(Exception ex) {
        List<ValidationError> errors = null;
        String message;

        if (ex instanceof MethodArgumentNotValidException) {
            errors = ((MethodArgumentNotValidException) ex).getBindingResult()
                    .getAllErrors()
                    .stream()
                    .map(error -> new ValidationError(
                            ((FieldError) error).getField(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
            message = "Validation failed";
        } else if (ex instanceof HttpMessageNotReadableException) {
            message = "Malformed JSON request";
        } else {
            message = "Unexpected error";
        }

        BaseResponse<Object> responseObject = BaseResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .errors(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<BaseResponse<?>> handlingAppException(AppException exception, WebRequest request) {
        BaseResponse<?> error = BaseResponse.builder()
                .code(exception.getErrorCode().getCode())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .timestamp(new Date())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {

        BaseResponse<Object> responseObject = BaseResponse.<Object>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseObject);
    }
}
