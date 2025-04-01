package its.backend.global.config.error.exception;

import its.backend.global.config.error.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private String detailMessage;

    public BaseException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }
}
