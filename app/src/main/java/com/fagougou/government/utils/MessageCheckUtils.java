package com.fagougou.government.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MessageCheckUtils {
    /**
     * 中国电信号码格式验证 手机段： 133,149,153,173,177,180,181,189,191,199,1349,1410,1700,1701,1702
     **/
    private static final String CHINA_TELECOM_PATTERN = "(?:^(?:\\+86)?1(?:33|49|53|7[37]|8[019]|9[19])\\d{8}$)|(?:^(?:\\+86)?1349\\d{7}$)|(?:^(?:\\+86)?1410\\d{7}$)|(?:^(?:\\+86)?170[0-2]\\d{7}$)";

    /**
     * 中国联通号码格式验证 手机段：130,131,132,145,146,155,156,166,171,175,176,185,186,1704,1707,1708,1709
     **/
    private static final String CHINA_UNICOM_PATTERN = "(?:^(?:\\+86)?1(?:3[0-2]|4[56]|5[56]|66|7[156]|8[56])\\d{8}$)|(?:^(?:\\+86)?170[47-9]\\d{7}$)";

    /**
     * 中国移动号码格式验证
     * 手机段：134,135,136,137,138,139,147,148,150,151,152,157,158,159,178,182,183,184,187,188,195,198,1440,1703,1705,1706
     **/
    private static final String CHINA_MOBILE_PATTERN = "(?:^(?:\\+86)?1(?:3[4-9]|4[78]|5[0-27-9]|78|8[2-478]|98|95)\\d{8}$)|(?:^(?:\\+86)?1440\\d{7}$)|(?:^(?:\\+86)?170[356]\\d{7}$)";

    /**
     * 中国大陆手机号码校验
     *
     * @param phone
     *
     * @return
     */
    public static boolean checkPhone(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            return checkChinaMobile(phone) || checkChinaUnicom(phone) || checkChinaTelecom(phone);
        }
        return false;
    }
    /**
     * 中国移动手机号码校验
     *
     * @param phone
     *
     * @return
     */
    public static boolean checkChinaMobile(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            Pattern regexp = Pattern.compile(CHINA_MOBILE_PATTERN);
            return regexp.matcher(phone).matches();
        }
        return false;
    }

    /**
     * 中国联通手机号码校验
     *
     * @param phone
     *
     * @return
     */
    public static boolean checkChinaUnicom(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            Pattern regexp = Pattern.compile(CHINA_UNICOM_PATTERN);
            return regexp.matcher(phone).matches();
        }
        return false;
    }

    /**
     * 中国电信手机号码校验
     *
     * @param phone
     *
     * @return
     */
    public static boolean checkChinaTelecom(String phone) {
        if (!TextUtils.isEmpty(phone)) {
            Pattern regexp = Pattern.compile(CHINA_TELECOM_PATTERN);
            return regexp.matcher(phone).matches();
        }
        return false;
    }

    /**
     * 验证输入的名字是否为“中文”或者是否包含“·”
     */
    public static boolean isLegalName(String name){
        if (name.contains("·") || name.contains("•")){
            return name.matches("^[\u4e00-\u9fa5]+[·•][\u4e00-\u9fa5]+$");
        }else {
            return name.matches("^[\u4e00-\u9fa5]+$");
        }
    }


    // 身份证校验码
    private static final int[] COEFFICIENT_ARRAY = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

    // 身份证号的尾数规则
    private static final String[] IDENTITY_MANTISSA = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

    private static final String IDENTITY_PATTERN = "^[0-9]{17}[0-9Xx]$";

    public static boolean isLegalPattern(String identity) {
        if (identity == null) {
            return false;
        }

        if (identity.length() != 18) {
            return false;
        }

        if (!identity.matches(IDENTITY_PATTERN)) {
            return false;
        }

        char[] chars = identity.toCharArray();
        long sum = IntStream.range(0, 17).map(index -> {
            char ch = chars[index];
            int digit = Character.digit(ch, 10);
            int coefficient = COEFFICIENT_ARRAY[index];
            return digit * coefficient;
        }).summaryStatistics().getSum();

        // 计算出的尾数索引
        int mantissaIndex = (int) (sum % 11);
        String mantissa = IDENTITY_MANTISSA[mantissaIndex];

        String lastChar = identity.substring(17);
        return lastChar.equalsIgnoreCase(mantissa);
    }


}
