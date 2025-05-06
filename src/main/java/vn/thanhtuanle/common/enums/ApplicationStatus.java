package vn.thanhtuanle.common.enums;

public enum ApplicationStatus {
    PENDING,
    APPROVED,
    REJECTED,
    IN_PROGRESS;

    public static ApplicationStatus fromString(String status) {
        try {
            return ApplicationStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
