package its.backend.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInRes {
    private String userId;
    private String userName;
    private String userNick;
    private final String accessToken;
}
