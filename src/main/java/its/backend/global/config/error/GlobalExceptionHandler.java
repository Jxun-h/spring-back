package its.backend.global.config.error;

import its.backend.global.config.error.exception.BaseException;
import its.backend.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse<String>> handleBaseException(BaseException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        String detailMessage = ex.getDetailMessage();
        
        if (detailMessage != null && !detailMessage.isEmpty()) {
            // 상세 메시지가 있으면 이를 응답에 포함
            String errorMessage = errorCode.getErrorMsg() + " - " + detailMessage;
            return ResponseEntity
                    .status(errorCode.getHttpStatus())
                    .body(new BaseResponse<>(errorMessage, errorCode.getCode()));
        } else {
            // 기본 에러 코드와 메시지만 포함
            return ResponseEntity
                    .status(errorCode.getHttpStatus())
                    .body(new BaseResponse<>(errorCode));
        }
    }
} 