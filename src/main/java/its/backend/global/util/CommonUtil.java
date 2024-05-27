package its.backend.global.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.util.UriUtils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class CommonUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * 정확한 숫자 연산과 표현을 위해 설정
     */
    public static final BigDecimal INT_MIN_BIGDECIMAL = new BigDecimal(Integer.MIN_VALUE);
    public static final BigDecimal INT_MAX_BIGDECIMAL = new BigDecimal(Integer.MAX_VALUE);
    public static final BigDecimal LONG_MIN_BIGDECIMAL = new BigDecimal(Long.MIN_VALUE);
    public static final BigDecimal LONG_MAX_BIGDECIMAL = new BigDecimal(Long.MAX_VALUE);

    /** 자료형 캐스팅 시작 */
    public static int toInt(Object object) {
        if (!NumberUtils.isParsable(String.valueOf(object))) {
            return 0;
        }
        return NumberUtils.toInt(String.valueOf(object));
    }

    public static float toFloat(Object object) {
        if (!NumberUtils.isCreatable(String.valueOf(object))) {
            return 0f;
        }
        return NumberUtils.toFloat(String.valueOf(object));
    }

    public static double toDouble(Object object) {
        if (!NumberUtils.isCreatable(String.valueOf(object))) {
            return 0d;
        }
        return NumberUtils.toDouble(String.valueOf(object));
    }

    public static long toLong(Object object) {
        if (!NumberUtils.isCreatable(String.valueOf(object))) {
            return 0l;
        }
        return NumberUtils.toLong(String.valueOf(object));
    }

    public static String toStr(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            return "";
        }
        return String.valueOf(object).trim();
    }

    public static BigDecimal toDecimal(Object object) { /** object be not null */
        BigDecimal decimal = null;

        if (object instanceof BigDecimal) {
            decimal = (BigDecimal) object;
        } else {
            decimal = new BigDecimal(object.toString());
        }

        if (log.isTraceEnabled()) {
            log.trace("toDecimal()" + String.valueOf(decimal));
        }
        return decimal;
    }
    /** 자료형 캐스팅 종료 */

    /**
     * 임시 비밀번호 생성
     *
     * @param pWSize 비밀번호 자릿수
     * @return
     */
    public static String generateTempPassword(int pWSize) {
        String charSetString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*";
        char[] charSet = charSetString.toCharArray();

        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());

        int index = 0;
        int length = charSet.length;

        for (int i = 0; i < pWSize; i++) {
            index = secureRandom.nextInt(length);
            stringBuffer.append(charSet[index]);
        }

        return stringBuffer.toString();
    }

    /** 번호 유효성 검증 시작 */
    public static boolean validateTel(String tel) {
        return tel.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$");
    }

    public static boolean validatePh(String ph) {
        return ph.matches("^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$");
    }

    public static boolean validateTelPh(String num) {
        return validateTel(num) || validatePh(num);
    }
    /** 번호 유효성 검증 종료 */

    /** 파일 경로 암/복호화 시작 */
    public static String encryptPath(String path) {
        return UriUtils.encode(path, StandardCharsets.UTF_8.toString());
    }
    public static String decryptPath(String path) {
        return UriUtils.decode(path, StandardCharsets.UTF_8.toString());
    }
    /** 파일 경로 암/복호화 종료 */

    public static String createUudi() {
        return UUID.randomUUID().toString();
    }

    public static String CreateSystemFileName(String originalFilename) {
        return UUID.randomUUID().toString() + "." + originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
    }
}