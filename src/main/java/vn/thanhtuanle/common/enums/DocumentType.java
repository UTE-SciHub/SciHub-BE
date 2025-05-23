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
    OTHER("Khác");

    String label;
}

