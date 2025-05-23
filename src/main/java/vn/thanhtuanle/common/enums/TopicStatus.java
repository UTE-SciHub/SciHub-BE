package vn.thanhtuanle.common.enums;

public enum TopicStatus {
    // Giai đoạn khởi tạo
    DRAFT,             // Bản nháp, người dùng đang soạn thảo đề tài
    RETURNED,          // Đã trả lại cho người tạo để chỉnh sửa (khi chưa đạt yêu cầu kiểm tra ban đầu)
    WITHDRAWN,         // Người tạo tự nguyện rút đề tài trước khi được xét duyệt chính thức

    // Giai đoạn nộp và chờ xử lý
    SUBMITTED,         // Đã gửi đăng ký đề tài
    UNDER_REVIEW,      // Đang được hội đồng/ban xét duyệt đánh giá
    NEED_REVISION,     // Cần sửa đổi theo góp ý của hội đồng/ban đánh giá
    REVIEWED,          // Đã được xem xét, chờ phân công hoặc phê duyệt tiếp theo
    WAITING_FOR_ASSIGNMENT, // Chờ phân công hội đồng xét duyệt hoặc người phụ trách
    ASSIGNED,          // Đã được phân công cho hội đồng hoặc người đánh giá

    // Giai đoạn phê duyệt
    WAITING_FOR_APPROVAL, // Đang chờ phê duyệt chính thức sau khi được đánh giá
    APPROVED,          // Đã được phê duyệt thực hiện
    REJECTED,          // Đã bị từ chối bởi hội đồng/phòng ban

    // Giai đoạn quản lý và thực hiện
    IN_CATALOG,        // Đã được đưa vào danh mục đề tài được duyệt
    IN_PROGRESS,       // Đề tài đang trong quá trình thực hiện
    ON_HOLD,           // Đề tài tạm dừng do các lý do nội bộ (thiếu kinh phí, nhân sự, v.v.)
    SUSPENDED,         // Đề tài bị đình chỉ (vi phạm quy định, có sự cố nghiêm trọng)

    // Giai đoạn kết thúc
    COMPLETED,         // Đề tài đã hoàn thành và được nghiệm thu
    FAILED,            // Đề tài thất bại, không hoàn thành theo kế hoạch
    EXPIRED,           // Đề tài đã quá hạn thời gian thực hiện mà chưa hoàn thành
    CANCELLED,         // Đề tài bị huỷ bởi đơn vị quản lý hoặc người tạo

    // Sau kết thúc
    ARCHIVED,          // Đề tài đã được lưu trữ, không còn hiển thị ở danh sách chính
    DELETED            // Đề tài đã bị xóa vĩnh viễn khỏi hệ thống
}


