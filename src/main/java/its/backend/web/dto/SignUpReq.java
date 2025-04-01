package its.backend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpReq {

    // 사용자 정보
    private String userid;
    private String password;
    private String username;
    private String usernick;
    private String birth;
    private String phone;
    private String email;
    private String role;

    // 사용자 이용 동의 정보
    private int serviceCheck = 0;
    private int personalCheck = 0;
    private int smsCheck = 0;
    private int emailCheck = 0;
    private int callCheck = 0;
}
