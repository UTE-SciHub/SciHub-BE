package vn.thanhtuanle.model.request;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class AssignCategoryRequest {
    @NotEmpty
    private List<String> topicIds;

    @NotNull
    private Integer categoryId;
}