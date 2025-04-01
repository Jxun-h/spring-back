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
        try {
            // 1. Request 값 검사
            // 빈 값 여부, 정규식 일치 검사
            if (checkIstEmptySignUpByUser(signUpReq)) {
                throw new BaseException(BAD_REQUEST, "필수 입력 필드가 비어있습니다. ID, 비밀번호, 이름, 생년월일, 이메일, 전화번호를 모두 입력해주세요.");
            }

            if (!isRegexUserId(signUpReq.getUserid())) {
                throw new BaseException(INVALID_USER_ID_FORMAT, 
                    "ID는 영문자로 시작하고 영문자, 숫자, 밑줄(_)만 사용하여 4~20자 이내로 입력해주세요. (현재 입력: " + signUpReq.getUserid() + ")");
            }

            if (!isRegexPassword(signUpReq.getPassword())) {
                throw new BaseException(INVALID_NOT_MATCHED_ID_OR_PASSWORD, 
                    "비밀번호는 다음 중 하나의 조건을 만족해야 합니다: 1) 8~16자 중 대문자, 소문자, 숫자, 특수문자를 각각 1개 이상 포함, 2) 10~30자 중 영문, 숫자, 특수문자 포함");
            }

            if (!isRegexBirth(signUpReq.getBirth())) {
                throw new BaseException(INVALID_BIRTH_FORMAT, 
                    "생년월일은 YYYY-MM-DD 또는 YYYY.MM.DD 형식으로 입력해주세요. (현재 입력: " + signUpReq.getBirth() + ")");
            }

            if (!isRegexPhone(signUpReq.getPhone())) {
                throw new BaseException(INVALID_PHONE_FORMAT, 
                    "전화번호는 숫자만 11자리 또는 XXX-XXXX-XXXX 형식으로 입력해주세요. (현재 입력: " + signUpReq.getPhone() + ")");
            }

            // 2. 아이디 중복 검사 및 비밀번호 암호화
            // 아이디 중복 검사
            if (userRepository.findByUserId(signUpReq.getUserid()).isPresent()) {
                throw new BaseException(INVALID_USER_ID_DUPLICATE, 
                    "이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요. (중복 아이디: " + signUpReq.getUserid() + ")");
            }

            // 패스워드 암호화 -> 사용자 요청값 중 비밀번호 최신화
            String encrptPassword = passwordEncoder.encode(signUpReq.getPassword());
            signUpReq.setPassword(encrptPassword);

            // 3. 사용자 생성
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
                    .emailCheck(signUpReq.getEmailCheck())
                    .callCheck(signUpReq.getCallCheck())
                    .menu_register(0)
                    .build();

            // 4. User Data Insert
            newUser = userRepository.save(newUser);

            // 5. User Data Return
            return SignUpRes.builder()
                    .userId(newUser.getUserId())
                    .userName(newUser.getUsername())
                    .userNick(newUser.getUserNick())
                    .birth(newUser.getBirth())
                    .email(newUser.getEmail())
                    .phone(signUpReq.getPhone())
                    .role(newUser.getRole().getKey())
                    .signUpDate(convertTimestampToString(newUser.getCreatedAt()))
                    .smsCheck(newUser.getSmsCheck())
                    .emailCheck(newUser.getEmailCheck())
                    .callCheck(newUser.getCallCheck())
                    .build();
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("회원가입 중 오류 발생: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("원인: " + e.getCause().getMessage());
            }
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
                signUpReq.getUserid() == null || signUpReq.getUserid().length() == 0 ||
                signUpReq.getPassword() == null || signUpReq.getPassword().length() == 0 ||
                signUpReq.getUsername() == null || signUpReq.getUsername().length() == 0 ||
                signUpReq.getBirth() == null || signUpReq.getBirth().length() == 0 ||
                signUpReq.getEmail() == null || signUpReq.getEmail().length() == 0 ||
                signUpReq.getPhone() == null || signUpReq.getPhone().length() == 0;
    }

    public String convertTimestampToString(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        return dateFormat.format(localDateTime);
    }
}
