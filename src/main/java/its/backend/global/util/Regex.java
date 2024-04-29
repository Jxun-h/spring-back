package its.backend.global.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {

    // Validate User ID
    public static boolean isRegexUserId(String arg) {
        String regex = "^[a-zA-z]{1}[a-zA-Z0-9_]{3,19}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(arg);
        return matcher.find();
    }

    // Validate User Password
    public static boolean isRegexPassword(String arg) {
        // 최소 8글자, 최대 16글자, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개
        String regex1 = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$";

        // 최대 30글자 : 영문 + 숫자 10자 이상
        String regex2 = "^(?=.*[a-zA-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,30}$";

        Pattern pattern1 = Pattern.compile(regex1, Pattern.CASE_INSENSITIVE);
        Pattern pattern2 = Pattern.compile(regex2, Pattern.CASE_INSENSITIVE);

        Matcher matcher1 = pattern1.matcher(arg);
        Matcher matcher2 = pattern2.matcher(arg);

        return matcher1.find() || matcher2.find();
    }

    // Validate User Birth
    public static boolean isRegexBirth(String arg) {
        String regex = "^\\d{4}\\.\\d{2}\\.\\d{2}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(arg);

        return matcher.find();
    }

    // Validate User Phone
    public static boolean isRegexPhone(String arg) {
        String regex = "^\\d{3}\\d{4}\\d{4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(arg);

        return matcher.find();
    }
}
