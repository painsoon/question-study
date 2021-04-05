package com.sheca.unitrust.common.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Description 随机数工具类
 * @Author psn
 * @Date 2020/3/20 17:07
 */
public class RandomUtil {
    /** 用于随机选的数字 */
    public static final String BASE_NUMBER = "0123456789";
    /** 用于随机选的字符 */
    public static final String BASE_LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    public static final String BASE_UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** 用于随机选的大小字符和数字 */
    public static final String BASE_CHAR_NUMBER = BASE_LOWER_CHAR + BASE_NUMBER + BASE_UPPER_CHAR;

    /**
     * 获取随机数生成器对象
     * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
     * @return
     */
    private static ThreadLocalRandom getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 获得随机数[0, 2^32)
     *
     * @return 随机数
     */
    public static int randomInt() {
        return getRandom().nextInt();
    }

    /**
     * 获得指定范围内的随机数 [0,limit)
     *
     * @param limit 限制随机数的范围，不包括这个数
     * @return 随机数
     */
    public static int randomInt(int limit) {
        return getRandom().nextInt(limit);
    }

    /**
     * 获得一个只包含数字的字符串
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomNumbers(int length) {
        return randomString(BASE_NUMBER, length);
    }

    /**
     * 获得一个随机的字符串（含数字和大小写字符）
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        return randomString(BASE_CHAR_NUMBER, length);
    }

    /**
     * 获得一个随机的字符串
     *
     * @param baseString 随机字符选取的样本
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String randomString(String baseString, int length) {
        final StringBuilder sb = new StringBuilder();

        if (length < 1) {
            length = 1;
        }
        int baseLength = baseString.length();
        for (int i = 0; i < length; i++) {
            int number = getRandom().nextInt(baseLength);
            sb.append(baseString.charAt(number));
        }
        return sb.toString();
    }

}
