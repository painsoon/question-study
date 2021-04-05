package com.sheca.brutus.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共工具类
 *
 * @author zhangjian
 * @date 2019/06/24 17:01
 */
public class CommonUtil {

    /**
     * 手机号脱敏
     *
     * @param mobile 手机号
     * @return 脱敏处理后的手机号
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 身份证脱敏
     *
     * @param idCard 身份证
     * @return 脱敏处理后的身份证
     */
    public static String idCardEncrypt(String idCard) {
        if (StringUtils.isEmpty(idCard) || (idCard.length() < 8)) {
            return idCard;
        }
        return idCard.replaceAll("(?<=\\w{6})\\w(?=\\w{4})", "*");
    }

    /**
     * 截取url中的ip和端口号
     *
     * @param url         请求url
     * @param projectName 是否需要截取项目名
     * @return 截取后的ip和端口号
     */
    public static String getIP(String url, boolean projectName) {
        //使用正则表达式过滤
        String re = "((http|ftp|https)://)(([a-zA-Z0-9._-]+)|([0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}))(([a-zA-Z]{2,6})|(:[0-9]{1,9})?)";
        String str;
        // 编译正则表达式
        Pattern pattern = Pattern.compile(re);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        // 若url==http://127.0.0.1:9040或www.baidu.com的，正则表达式表示匹配
        if (matcher.matches()) {
            str = url;
        } else {
            String[] split2 = url.split(re);
            if (split2.length > 1) {
                String substring = url.substring(0, url.length() - split2[1].length());
                str = projectName ? substring.concat(split2[1].substring(0, split2[1].indexOf("/", split2[1].indexOf("/") + 1))) : substring;
            } else {
                str = split2[0];
            }
        }
        return str;
    }

    /**
     * 校验字符串中是否同时包含大写、小写字母和数字
     *
     * @param str
     * @return
     */
    public static boolean containsUpperAndLowerAndDigit(String str) {
        boolean isDigit = false;// 是否包含数字
        boolean isLower = false;// 是否包含小写字母
        boolean isUpper = false;// 是否包含大写字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                isDigit = true;
            }
            if (Character.isLowerCase(str.charAt(i))) {
                isLower = true;
            }
            if (Character.isUpperCase(str.charAt(i))) {
                isUpper = true;
            }
        }
        return isDigit && isLower && isUpper ? true : false;
    }

    /**
     * 生成数字和字母组合的随机数
     *
     * @param length 生成随机数的长度
     * @return
     */
    public static String getCharAndNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (random.nextInt(2) % 2 == 0) {
                // 取得大写字母还是小写字母
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else { // 数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 生成指定位数的随机数（仅包含数字）
     *
     * @param length
     * @return
     */
    public static String getRandom(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

}
