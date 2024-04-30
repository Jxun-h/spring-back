package its.backend.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import its.backend.global.config.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static its.backend.global.response.BaseResponseStatus.SUCCESS;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "res"})
public class BaseResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String message;
    private final int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T res;

    // SUCCESS
    public BaseResponse(T res) {
        this.isSuccess = SUCCESS.isSuccess();
        this.message = SUCCESS.getMessage();
        this.code = SUCCESS.getCode();
        this.res = res;
    }

    public BaseResponse(ErrorCode errorCode) {
        this.isSuccess = false;
        this.message = errorCode.getErrorMsg();
        this.code = errorCode.getCode();
        this.res = getRes();
    }

    public BaseResponse(String message, int code) {
        this.isSuccess = false;
        this.message = message;
        this.code = code;
        this.res = getRes();
    }
}
