package its.backend.global.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 베이스 에러 코드
    SUCCESS(HttpStatus.OK, 200, "요청에 성공했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "요청이 잘못되었습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "해당 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "대상을 찾을 수 없습니다."),

    // 커스텀 에러 코드
    // * * * 회원가입 * * *
    // ID 정규표현식
    INVALID_USER_ID_FORMAT(HttpStatus.BAD_REQUEST, 9001, "아이디 형식이 잘못되었습니다."),

    // 중복
    INVALID_USER_ID_DUPLICATE(HttpStatus.BAD_REQUEST, 9002, "중복된 아이디 입니다."),

    // ID 빈 값
    INVALID_USER_ID_EMPTY(HttpStatus.BAD_REQUEST, 9003, "아이디를 입력해주세요."),

    // 빈 값 존재
    INVALID_EMPTY_VALUE(HttpStatus.BAD_REQUEST, 9004, "빈 값이 존재합니다. 다시 확인해주세요."),

    // * * * 로그인 * * *
    // 유저 ID OR 패스워드 다름
    INVALID_NOT_MATCHED_ID_OR_PASSWORD(HttpStatus.BAD_REQUEST, 9005, "패스워드가 다릅니다."),

    // * * * 서버 에러 * * *
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9999, "내부서버 에러");

    private final HttpStatus httpStatus;
    private final int code;
    private final String errorMsg;
}
