package its.backend.global.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;

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
        if(!NumberUtils.isParsable(String.valueOf(object))) {
            return 0;
        }
        return NumberUtils.toInt(String.valueOf(object));
    }

    public static float toFloat(Object object) {
        if(!NumberUtils.isCreatable(String.valueOf(object))) {
            return 0f;
        }
        return NumberUtils.toFloat(String.valueOf(object));
    }

    public static double toDouble(Object object) {
        if(!NumberUtils.isCreatable(String.valueOf(object))) {
            return 0d;
        }
        return NumberUtils.toDouble(String.valueOf(object));
    }

    public static long toLong(Object object) {
        if(!NumberUtils.isCreatable(String.valueOf(object))) {
            return 0l;
        }
        return NumberUtils.toLong(String.valueOf(object));
    }

    public static String toString(Object object) {
        if (ObjectUtils.isEmpty(object)) {
            return "";
        }
        return String.valueOf(object).trim();
    }
    /** 자료형 캐스팅 종료 */


    /**
     * 임시 비밀번호 생성
     * @param pwsize 비밀번호 자릿수
     * @return
     */
    public static String generateTempPassword(int pwsize) {
        String charSetString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*";
        char[] charSet = charSetString.toCharArray();

        StringBuffer stringBuffer = new StringBuffer();
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(new Date().getTime());

        int index = 0;
        int length = charSet.length;

        for (int i = 0; i < pwsize; i++) {
            index = secureRandom.nextInt(length);
            stringBuffer.append(charSet[index]);
        }

        return stringBuffer.toString();
    }
}
