package vn.thanhtuanle.common.enums;

public enum TopicStatus {
    DRAFT,                 // Bản nháp - chưa gửi
    SUBMITTED,             // Đã gửi đăng ký
    PENDING_REVIEW,        // Chờ hội đồng xét duyệt
    REQUESTED_REVISION,    // Yêu cầu chỉnh sửa (do hội đồng yêu cầu)
    RESUBMITTED,           // Đã chỉnh sửa và gửi lại
    APPROVED,              // Đã phê duyệt
    REJECTED,              // Bị từ chối
    WAITING_FOR_FUNDS,     // Đang chờ cấp kinh phí
    FUNDED,                // Đã cấp kinh phí
    IN_PROGRESS,           // Đang thực hiện
    EXTENDED,              // Được gia hạn thời gian thực hiện
    FINAL_REPORT_SUBMITTED,// Đã nộp báo cáo tổng kết
    EVALUATING,            // Đang đánh giá kết quả
    COMPLETED,             // Hoàn thành
    ARCHIVED,              // Lưu trữ
    CANCELLED,              // Bị huỷ
    PENDING,               // Đang chờ xử lý
}

