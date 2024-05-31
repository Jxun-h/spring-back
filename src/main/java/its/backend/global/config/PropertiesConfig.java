package its.backend.global.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "demo.prop")
@Getter @Setter
@ToString
public class PropertiesConfig {
    /** 시스템명 */
    private String systemName;
    /** 디폴트 locale */
    private String defaultLocale;
    /** 최대 패스워드 오류횟수 */
    private int systemPwdErrMaxCnt;
    /** 패스워드변경 주기 */
    private int pwdChangeCycle;
    /** 세션명 */
    private String sessionName;

    /** 템플릿 경로 */
    private String templatesPath;
    /** 메일 경로 */
    private String mailPath;

    /** 운영자 임시비밀번호 */
    private String tempPwd;
    /** 기본 접속가능 아이피 */
    private String defaultAccessibleIp;

    /** 사용자타입(수퍼유저) */
    private String typeSuper;

    /** 사용자상태(정상) */
    private String userStateNormal;
    /** 사용자상태(탈퇴) */
    private String userStateLeave;
    /** 사용자상태(중지) */
    private String userStateStop;
    /** 사용자 등록대상 */
    private String userStateRegReady;
    /** 사용자상태(비밀번호변경대상) */
    private String userStateNeedChangePwd;

    /** 2중 보안 로그인 적용여부 */
    private boolean secondAuth;
    /** 접속가능 아이피 적용여부 */
    private boolean accessibleIPConfirm;
    /** 중복로그인 적용여부 */
    private boolean duplLogin;
    /**페이지 등록여부 체크*/
    private boolean pageCheck;
    /** 기본 검색 사이즈 */
    private int pageSize;

    /**암복화화 키*/
    private String encryptKey;

    /**파일업로드 기본 경로*/
    private String basePath;
    /**공지사항 업로드 경로*/
    private String dirAnnouncement;
}
