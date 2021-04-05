package com.sheca.unitrust.common.util;

import com.sheca.unitrust.framework.enums.ErrorCodeEnum;
import com.sheca.unitrust.framework.exception.ApiException;
import com.sheca.unitrust.framework.util.ApiAssert;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 日期工具类
 * @author liujida
 * create: 2019-05-15
 */
@Slf4j
@UtilityClass
public class DateUtil {

    public static final String CERT_FORMAT = "yyyyMMddHHmmss";

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_FORMAT_BEGIN = "yyyy-MM-dd 00:00:00";

    public static final String DEFAULT_FORMAT_END = "yyyy-MM-dd 23:59:59";

    public static final String OTHER_FORMAT = "yyyy-MM-dd";

    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";

    /**
     * 将日期格式化成对应的format
     *
     * @return str
     */
    public static String formatDate(LocalDateTime time, String format) {
        if (time == null) {
            return null;
        }

        if (StringUtils.isBlank(format)) {
            format = DEFAULT_FORMAT;
        }

        return DateTimeFormatter.ofPattern(format)
                .format(time);
    }

    public static boolean isBefore(Date now, Date compare) {
        if (Objects.isNull(now) || Objects.isNull(compare)) {
            return false;
        }

        LocalDateTime nowTime = toLocalDateTime(now);
        LocalDateTime compareTime = toLocalDateTime(compare);

        return nowTime.isBefore(compareTime);
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * 获取本月的开始时间
     *
     * @return
     */
    public static String getBeginDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        return formatDate(toLocalDateTime(getDayStartTime(calendar.getTime())), DEFAULT_FORMAT);
    }

    /**
     * 获取本月的结束时间
     *
     * @return
     */
    public static String getEndDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(getNowYear(), getNowMonth() - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(getNowYear(), getNowMonth() - 1, day);
        return formatDate(toLocalDateTime(getDayEndTime(calendar.getTime())), DEFAULT_FORMAT);
    }

    /**
     * 获取下一个月的第一天
     *
     * @return
     */
    public static String getPerFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return formatDate(toLocalDateTime(calendar.getTime()), DEFAULT_FORMAT);
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param date
     * @return
     */
    public static Timestamp getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取某个日期的结束时间
     *
     * @param date
     * @return
     */
    public static Timestamp getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date) {
            calendar.setTime(date);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * 获取今年是哪一年
     *
     * @return
     */
    public static int getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(1);
    }

    /**
     * 获取本月是哪一月
     *
     * @return
     */
    public static int getNowMonth() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return gc.get(2) + 1;
    }

    /**
     * 获取近一周的日期
     *
     * @return
     */
    public static Map getPreWeek() {
        // 日期格式转换
        SimpleDateFormat format = new SimpleDateFormat(OTHER_FORMAT);
        // 当前日期
        Calendar instance = Calendar.getInstance();
        // 调整到上周
        instance.add(Calendar.WEDNESDAY, -1);
        Map<Integer, String> map = new LinkedHashMap<>();
        for (int i = 1; i <= 7; i++) {
            map.put(i, format.format(instance.getTime()));
            instance.add(Calendar.DAY_OF_WEEK, 1);
        }
        return map;
    }

    /**
     * 获取近6个月的日期
     *
     * @return
     */
    public static Map getPreMonth() {
        // 日期格式转换
        SimpleDateFormat format = new SimpleDateFormat(YEAR_MONTH_FORMAT);
        // 当前日期
        Calendar instance = Calendar.getInstance();
        // 调整到上月
        instance.add(Calendar.MONTH, -5);
        Map<Integer, String> map = new LinkedHashMap<>();
        for (int i = 1; i <= 6; i++) {
            map.put(i, format.format(instance.getTime()));
            instance.add(Calendar.MONTH, 1);
        }
        return map;
    }

    /**
     * 将String转为LOCALDATETIME
     */
    public static LocalDateTime getStringToDate(String time, String format) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取两个日期之间的所有日期（yyyy-MM-dd）
     *
     * @param beginDate 开始日期
     * @param endDate 结束日期
     * @return 两个日期之间的所有日期（yyyy-MM-dd）
     */
    public static List<String> getBetweenDates(String beginDate, String endDate) {
        // 校验日期格式
        Date begin, end;
        DateFormat format = new SimpleDateFormat(OTHER_FORMAT);
        try {
            begin = format.parse(beginDate);
            end = format.parse(endDate);
        } catch (ParseException e) {
            log.info("DateUtil getBetweenDates parse error：beginDate is {}, endDate is {}", beginDate, endDate);
            throw new ApiException(ErrorCodeEnum.DATE_FORMAT_ERROR);
        }
        // 校验开始时间和结束时间的大小关系
        ApiAssert.isFalse(ErrorCodeEnum.BEGINTIME_GT_ENDTIME, begin.getTime() > end.getTime());
        // 循环取出所有日期
        List<String> result = new ArrayList<>();
        Calendar tempStart = Calendar.getInstance();
        tempStart.setTime(begin);
        while (begin.getTime() <= end.getTime()) {
            result.add(format.format(tempStart.getTime()));
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            begin = tempStart.getTime();
        }
        return result;
    }

}
