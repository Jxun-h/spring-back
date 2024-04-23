package its.backend.global.config.error.exception;

import its.backend.global.config.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseException extends RuntimeException {
    ErrorCode errorCode;
}
