package its.backend.global.response;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    // * * * SUCCESS CODE * * *
    SUCCESS(true, 1000, "요청 성공"),


    /**
     * Member
     * Code : 2xxx
     */
    // * * * FAILURE CODE * * *
    NOT_FOUND_MEMBER(false, 2001, "일치하는 사용자가 없습니다.");

    /**
     * ETC
     */
    private boolean isSuccess;
    private String message;
    private int code;

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
