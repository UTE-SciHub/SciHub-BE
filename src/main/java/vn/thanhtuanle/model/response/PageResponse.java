package vn.thanhtuanle.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> extends BaseResponse<T> {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
    private String query;
}