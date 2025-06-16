package vn.thanhtuanle.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum DocumentType {
    REVIEW_RESULT("Biên bản đánh giá"),
    TOPIC_REGISTRATION("Phiếu đăng ký đề tài"),
    ACCEPTANCE_MINUTES("Biên bản nghiệm thu"),
    DECISION("Quyết định"),
    COMPLETION_REPORT("Báo cáo tổng kết"),
    BM_DECISION("Quyết định BM.24-QT.01-KHCN"),
    RESEARCH_PRODUCT("Sản phẩm nghiên cứu"),
    SUPPORTING_DOCUMENT("Tài liệu bổ sung"),
    APPLICATION_CERTIFICATE("Chứng nhận áp dụng thực tế"),
    ADDITIONAL_DOCUMENT("Tài liệu khác"),
    OTHER("Khác");

    String label;
}

