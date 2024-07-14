package its.backend.web.Service;

import its.backend.global.config.error.exception.BaseException;
import its.backend.global.security.JwtTokenProvider;
import its.backend.web.dto.*;
import its.backend.web.entity.Role;
import its.backend.web.entity.User;
import its.backend.web.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static its.backend.global.config.error.ErrorCode.*;
import static its.backend.global.util.RegexUtil.*;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberDetailService memberDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(rollbackFor = BaseException.class)
    public SignUpRes signUp(SignUpReq signUpReq) throws BaseException {
        // 1. Request 값 검사
        // 빈 값 여부, 정규식 일치 검사
        if (checkIstEmptySignUpByUser(signUpReq)) {
            throw new BaseException(BAD_REQUEST);
        }

        if (!isRegexUserId(signUpReq.getUserid())) {
            throw new BaseException(INVALID_USER_ID_FORMAT);
        }

        if (!isRegexPassword(signUpReq.getPassword())) {
            throw new BaseException(INVALID_NOT_MATCHED_ID_OR_PASSWORD);
        }

        if (!isRegexBirth(signUpReq.getBirth())) {
            throw new BaseException(INVALID_BIRTH_FORMAT);
        }

        if (!isRegexPhone(signUpReq.getPhone())) {
            throw new BaseException(INVALID_PHONE_FORMAT);
        }

        // 2. 아이디 중복 검사 및 비밀번호 암호화
        // 아이디 중복 검사
        if (userRepository.findByUserId(signUpReq.getUserid()).isPresent()) {
            throw new BaseException(INVALID_USER_ID_DUPLICATE);
        }

        // 패스워드 암호화 -> 사용자 요청값 중 비밀번호 최신화
        try {

            String encrptPassword = passwordEncoder.encode(signUpReq.getPassword());
            signUpReq.setPassword(encrptPassword);

        } catch (Exception e) {
            throw new BaseException(INTERNAL_SERVER_ERROR);
        }

        try {
            User newUser = User.builder()
                    .userId(signUpReq.getUserid())
                    .password(signUpReq.getPassword())
                    .userName(signUpReq.getUsername())
                    .userNick(signUpReq.getUsernick())
                    .birth(signUpReq.getBirth())
                    .email(signUpReq.getEmail())
                    .phone(signUpReq.getPhone())
                    .role(Role.valueOf(signUpReq.getRole() == null ? "USER" : "ADMIN"))
                    .firstLogin(1)
                    .serviceCheck(signUpReq.getServiceCheck())
                    .personalCheck(signUpReq.getPersonalCheck())
                    .smsCheck(signUpReq.getSmsCheck())
                    .email_check(signUpReq.getEmailCheck())
                    .callCheck(signUpReq.getCallCheck())
                    .build();

            // User Data Insert
            newUser = userRepository.save(newUser);

            // User Data Return
            SignUpRes checkUser = SignUpRes.builder()
                    .userId(newUser.getUserId())
                    .userName(newUser.getUsername())
                    .userNick(newUser.getUserNick())
                    .birth(newUser.getBirth())
                    .email(newUser.getEmail())
                    .phone(signUpReq.getPhone())
                    .role(newUser.getRole().getKey())
                    .signUpDate(convertTimestampToString(newUser.getCreatedAt()))
                    .smsCheck(newUser.getSmsCheck())
                    .emailCheck(newUser.getEmail_check())
                    .callCheck(newUser.getCallCheck())
                    .build();
            return checkUser;
        } catch (Exception e) {
            System.out.println(e);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public SignInRes loginUser(SignInReq req) {
        // 1. 빈 값 확인
        if (req.getUserid().isEmpty() || req.getPassword().isEmpty()) {
            throw new BaseException(INVALID_EMPTY_VALUE);
        }

        // 2-1. UserDetail Data Load
        UserDetails userDetails = memberDetailService.loadUserByUsername(req.getUserid());

        // 2-2. UserDetail Password Validation
        if (!checkPassword(req.getPassword(), userDetails.getPassword())) {
            throw new BaseException(INVALID_NOT_MATCHED_ID_OR_PASSWORD);
        }

        // 3-1. JWT Token (Access, Refresh)
        User user = (User) userDetails;

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        TokenDto token = jwtTokenProvider.generateToken(authentication, user.getUserSeq());

        // 4-1. Login Response
        return SignInRes.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .userNick(user.getUserNick())
                .accessToken(token)
                .build();
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public boolean checkIstEmptySignUpByUser(SignUpReq signUpReq) {
        return
                signUpReq.getUserid().length() == 0 || signUpReq.getPassword().length() == 0 ||
                        signUpReq.getUsername().length() == 0 || signUpReq.getBirth().length() == 0 ||
                        signUpReq.getEmail().length() == 0 || signUpReq.getPhone().length() == 0;

    }

    public String convertTimestampToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateFormat.format(localDateTime);
    }
}
