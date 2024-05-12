package its.backend.web.Controller.user;

import its.backend.global.response.BaseResponse;
import its.backend.web.Service.UserService;
import its.backend.web.dto.SignInReq;
import its.backend.web.dto.SignInRes;
import its.backend.web.dto.SignUpReq;
import its.backend.web.dto.SignUpRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/app")
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/users/auth/signUp")
    public ResponseEntity<BaseResponse<SignUpRes>> signUp(@RequestBody SignUpReq signUpReq) {
        SignUpRes signUpRes = userService.signUp(signUpReq);
        return ResponseEntity.ok(new BaseResponse<>(signUpRes));
    }

    @PostMapping("/users/auth/signIn")
    public ResponseEntity<BaseResponse<SignInRes>> signIn(@RequestBody SignInReq req) {
        SignInRes signInRes = userService.loginUser(req);

        return ResponseEntity.ok(new BaseResponse<>(signInRes));
    }

    @PostMapping("users/auth/signOut")
    public String signOut() {
        return "login";
    }
}
