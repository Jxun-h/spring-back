package its.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {

    // 사용자 정보
    private String userId;
    private String password;
    private String userName;
    private String userNick;
    private String birth;
    private String phone;
    private String email;

    // 사용자 이용 동의 정보
    private int serviceCheck;
    private int personalCheck;
    private int smsCheck;
    private int emailCheck;
    private int callCheck;
}
