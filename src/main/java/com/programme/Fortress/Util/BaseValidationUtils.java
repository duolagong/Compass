package com.programme.Fortress.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseValidationUtils {

    /**
     * 验证手机号
     * @param phone
     * @return
     */
    public static boolean isMobile(String phone) {
        Pattern p = Pattern.compile("^1[3|4|5|6|7|8|9][0-9]\\d{8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 验证数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String num) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(num).matches();
    }

}
