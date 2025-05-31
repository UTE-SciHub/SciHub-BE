package vn.thanhtuanle.common.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.thanhtuanle.entity.Council;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouncilExportData {
    private Council council;
    private String memberDetails;
    private String topicDetails;
}
