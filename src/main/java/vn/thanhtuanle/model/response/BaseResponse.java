package vn.thanhtuanle.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.thanhtuanle.model.ValidationError;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    @Builder.Default
    private int code = 1000;
    private int status;
    private String message;
    private T data;
    private Date timestamp;
    private String path;
    private List<ValidationError> errors;
}
