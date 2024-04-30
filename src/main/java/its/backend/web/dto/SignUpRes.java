package its.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Data : 해당 어노테이션이 선언된 클래스에 대해 getter, setter, equals, hashCode, toString 함수를 자동으로 생성해준다.
 * @AllArgsConstructor : 모든 필드 값을 파라미터로 입력 받는 생성자를 생성해준다.
 * @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성해준다.
 * @Builder 패턴 클래스를 생성하며, 생성자 상단에 생성하면 생성자에 포함된 필드만 빌더에 포함시킨다.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRes {

    // 사용자 정보
    private String userId;
    private String userName;
    private String userNick;
    private String birth;
    private String phone;
    private String email;
    private String signUpDate;
    private String role;

    // 사용자 이용 동의 정보
    private int smsCheck;
    private int emailCheck;
    private int callCheck;
}
