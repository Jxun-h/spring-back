package its.backend.web.Service;

import its.backend.global.config.error.exception.BaseException;
import its.backend.web.dto.SignUpReq;
import its.backend.web.dto.SignUpRes;
import its.backend.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = BaseException.class)
    public SignUpRes signUp(SignUpReq signUpReq) throws BaseException {
        // TODO : 작성 필요
        return null;
    }
}
