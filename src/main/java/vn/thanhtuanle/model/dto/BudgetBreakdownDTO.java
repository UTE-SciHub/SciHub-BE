package vn.thanhtuanle.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetBreakdownDTO {
    private String id;
    private String category;
    private long amount;
    private String description;
}
