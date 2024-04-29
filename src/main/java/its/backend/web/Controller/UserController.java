package its.backend.web.Controller;

import its.backend.global.config.error.exception.BaseException;
import its.backend.global.response.BaseResponse;
import its.backend.web.Service.UserService;
import its.backend.web.dto.SignUpReq;
import its.backend.web.dto.SignUpRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static its.backend.global.config.error.ErrorCode.BAD_REQUEST;
import static its.backend.global.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/web")
public class UserController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<BaseResponse> userTest() {
        try {
            return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BaseResponse<>(BAD_REQUEST));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<BaseResponse<SignUpRes>> signUp(@RequestBody SignUpReq signUpReq) {
        SignUpRes signUpRes = userService.signUp(signUpReq);
        return ResponseEntity.ok(new BaseResponse<>(signUpRes));
    }
}
