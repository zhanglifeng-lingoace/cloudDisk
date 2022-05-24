package com.edu.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wugaoping on 2019/8/18.
 */
public class DateUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
    private final static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String DATE_FORMAT = "yyyy-MM-dd";
    private final static String HHMMSS_FORMAT = "HH:mm:ss";

    /**
     * 星期转中文
     */
    public static Map<Integer, String> weekDayZhMap = new HashMap<>();

    static {
        weekDayZhMap.put(1, "日");
        weekDayZhMap.put(2, "一");
        weekDayZhMap.put(3, "二");
        weekDayZhMap.put(4, "三");
        weekDayZhMap.put(5, "四");
        weekDayZhMap.put(6, "五");
        weekDayZhMap.put(7, "六");
    }

    /**
     * 星期转英文
     */
    public static Map<Integer, String> weekDayEnMap = new HashMap<>();

    static {
        weekDayEnMap.put(1, "Sun");
        weekDayEnMap.put(2, "Mon");
        weekDayEnMap.put(3, "Tues");
        weekDayEnMap.put(4, "Wed");
        weekDayEnMap.put(5, "Thur");
        weekDayEnMap.put(6, "Fri");
        weekDayEnMap.put(7, "Sat");
    }

    /**
     * 获取当前本地时间
     *
     * @return
     */
    public static Date localTime() {
        // 取得本地时间
        Calendar cal = Calendar.getInstance();

        // 取得时间偏移量
        //int zoneOffset =  cal.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差
        //int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 从本地时间里扣除这些差量，取得UTC时间
        //cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        // 数据库已经自动处理成了UTC时间，这里不需要重复处理了
        return cal.getTime();
    }

    /**
     * 将本地时间转换成UTC标准时间
     *
     * @return
     */
    public static Date localTime2Utc(Date utcDate) {
        // 设置本地时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(utcDate);

        // 取得时间偏移量
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 将UTC时间加上这些差量，取得本地时间
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        // 前端传入的接口参数转换成日期对象时已经处理了时区
        return cal.getTime();
    }

    /**
     * 将用户本地时间转换成本地时间
     *
     * @param localDate 本地时间
     * @param tz        用户本地时区
     * @return
     */
    public static Date userLocalTime2LocalTime(Date localDate, String tz) {
        // 设置UTC时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(localDate);

        try {
            if (!StringUtils.isEmpty(tz)) {
                String[] timezoneInfo = tz.split(" ");
                String timezone = timezoneInfo[timezoneInfo.length - 1];
                int hours = Integer.parseInt(timezone.substring(1, 3));
                int minutes = Integer.parseInt(timezone.substring(3, 5));

                // 取得时间偏移量
                int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
                // 取得夏令时差
                int dstOffset = cal.get(Calendar.DST_OFFSET);
                // 从本地时间里扣除这些差量，取得UTC时间
                cal.add(Calendar.MILLISECOND, (zoneOffset + dstOffset));

                if (timezone.startsWith("+")) {
                    cal.add(Calendar.HOUR_OF_DAY, -hours);
                    cal.add(Calendar.MINUTE, -minutes);
                } else {
                    cal.add(Calendar.HOUR_OF_DAY, +hours);
                    cal.add(Calendar.MINUTE, +minutes);
                }
            }
        } catch (Exception e) {
            //LOGGER.error(String.format("userLocalTime2LocalTime %s, %s exception", localDate.toString(), tz), e);
        }

        // 前端传入的接口参数转换成日期对象时已经处理了时区
        return cal.getTime();
    }

    /**
     * 将本地时间转换成用户本地时间
     *
     * @param localDate 本地时间
     * @param tz        用户本地时区
     * @return
     */
    public static Date localTime2UserLocalTime(Date localDate, String tz) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(localDate);

        try {
            if (!StringUtils.isEmpty(tz)) {
                String[] timezoneInfo = tz.split(" ");
                String timezone = timezoneInfo[timezoneInfo.length - 1];
                int hours = Integer.parseInt(timezone.substring(1, 3));
                int minutes = Integer.parseInt(timezone.substring(3, 5));

                // 取得时间偏移量
                int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
                // 取得夏令时差
                int dstOffset = cal.get(Calendar.DST_OFFSET);
                // 从本地时间里扣除这些差量，取得UTC时间
                cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

                if (timezone.startsWith("+")) {
                    cal.add(Calendar.HOUR_OF_DAY, hours);
                    cal.add(Calendar.MINUTE, minutes);
                } else {
                    cal.add(Calendar.HOUR_OF_DAY, -hours);
                    cal.add(Calendar.MINUTE, -minutes);
                }
            }
        } catch (Exception e) {
            //LOGGER.error(String.format("localTime2UserLocalTime %s, %s exception", localDate.toString(), tz), e);
        }

        LOGGER.info("localTime2UserLocalTime localDate {}, tz {}, userLocalDate {}", localDate, tz, cal.getTime());
        return cal.getTime();
    }

    /**
     * 将本地时间转换成用户本地中文时间通知消息字符串格式
     *
     * @param localDate 本地时间
     * @param tz        用户本地时区
     * @return
     */
    public static String getUserLocalNotifyZhTime(Date localDate, String tz) {
        // 获取用户本地时间
        Date userLocalDate = localTime2UserLocalTime(localDate, tz);
        Calendar cal = Calendar.getInstance();
        cal.setTime(userLocalDate);

        // 前端传入的接口参数转换成日期对象时已经处理了时区
        String dayStr = String.format("%04d.%02d.%02d（周%s）%02d:%02d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                weekDayZhMap.get(cal.get(Calendar.DAY_OF_WEEK)), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        return dayStr;
    }

    /**
     * 将本地时间转换成用户本地英文时间通知消息字符串格式
     *
     * @param localDate 本地时间
     * @param tz        用户本地时区
     * @return
     */
    public static String getUserLocalNotifyEnTime(Date localDate, String tz) {
        // 获取用户本地时间
        Date userLocalDate = localTime2UserLocalTime(localDate, tz);
        Calendar cal = Calendar.getInstance();
        cal.setTime(userLocalDate);

        // 前端传入的接口参数转换成日期对象时已经处理了时区
        String dayStr = String.format("%04d.%02d.%02d（%s）%02d:%02d",
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH),
                weekDayEnMap.get(cal.get(Calendar.DAY_OF_WEEK)), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        return dayStr;
    }

    /**
     * 判断在用户本地时间，这两个日期是否相隔一天
     * 将本地时间转换成用户本地时间通知消息字符串格式
     *
     * @param d1 本地时间当天时间
     * @param d2 本地时间第二天时间
     * @param tz 用户本地时区
     * @return
     */
    public static boolean isTomorrow(Date d1, Date d2, String tz) {
        Date uld1 = localTime2UserLocalTime(d1, tz);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(uld1);
        cal1.add(Calendar.DAY_OF_MONTH, 1);
        String sd1 = String.format("%04d%02d%02d", cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH) + 1, cal1.get(Calendar.DAY_OF_MONTH));

        Date uld2 = localTime2UserLocalTime(d2, tz);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(uld2);
        String sd2 = String.format("%04d%02d%02d", cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));

        return sd1.equals(sd2);
    }

    /**
     * 判断在用户本地时间，这两个日期是否同一天
     * 将本地时间转换成用户本地时间通知消息字符串格式
     *
     * @param d1 本地时间当天时间
     * @param d2 本地时间当天时间
     * @param tz 用户本地时区
     * @return
     */
    public static boolean isToday(Date d1, Date d2, String tz) {
        Date uld1 = localTime2UserLocalTime(d1, tz);
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(uld1);
        String sd1 = String.format("%04d%02d%02d", cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH) + 1, cal1.get(Calendar.DAY_OF_MONTH));

        Date uld2 = localTime2UserLocalTime(d2, tz);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(uld2);
        String sd2 = String.format("%04d%02d%02d", cal2.get(Calendar.YEAR), cal2.get(Calendar.MONTH) + 1, cal2.get(Calendar.DAY_OF_MONTH));

        return sd1.equals(sd2);
    }

    /**
     * @param date
     * @return
     * @description 时间格式转换
     * @Author zhanglifeng
     */
    public static String date2TimeStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
        return sdf.format(date);
    }

    /**
     * @param begin
     * @param end
     * @Description 日期相差天数
     * @Author zhanglifeng
     */
    public static long getDaySub(String begin, String end) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date beginDate = format.parse(begin);
        Date endDate = format.parse(end);
        return (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
    }

    /**
     * @param begin
     * @param end
     * @Description 日期相差小时数
     * @Author zhanglifeng
     */
    public static long getHourSub(String begin, String end) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        Date beginDate = format.parse(begin);
        Date endDate = format.parse(end);
        return (endDate.getTime() - beginDate.getTime()) / (60 * 60 * 1000);
    }

    /**
     * @param begin
     * @param end
     * @Description 日期相差分钟数
     * @Author zhanglifeng
     */
    public static long getMinuteSub(String begin, String end) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        Date beginDate = format.parse(begin);
        Date endDate = format.parse(end);
        return (endDate.getTime() - beginDate.getTime()) / (60 * 1000);
    }

    /**
     * @param begin
     * @param end
     * @Description 日期相差分钟数
     * @Author zhanglifeng
     */
    public static long getSecondSub(String begin, String end) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        Date beginDate = format.parse(begin);
        Date endDate = format.parse(end);
        return (endDate.getTime() - beginDate.getTime()) / 1000;
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAgeByBirth(Date birthDay) {
        int age = 0;
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        //当前年份
        int yearNow = cal.get(Calendar.YEAR);
        //当前月份
        int monthNow = cal.get(Calendar.MONTH);
        //当前日期
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //计算整岁数
        age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    //当前日期在生日之前，年龄减一
                    age--;
                }
            } else {
                //当前月份在生日之前，年龄减一
                age--;
            }
        }
        return age;
    }

    public static boolean compareAge(Date birthDay, int age) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDay);
        cal.add(Calendar.YEAR, age);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime().before(DateUtil.localTime());
    }

    /**
     * 判断时间是否在某个时间段内（支持跨天）
     *
     * @param date      校验时间
     * @param startTime 时间段开始时间 HH:mm:ss
     * @param endTime   时间段结束时间 HH:mm:ss
     * @return
     * @author zhuhongjiang
     */
    public static boolean isInTimeRange(Date date, String startTime, String endTime) {
        SimpleDateFormat format = new SimpleDateFormat(HHMMSS_FORMAT);
        String time = format.format(date);

        int set = Integer.valueOf(time.replaceAll(":", ""));
        int begin = Integer.valueOf(startTime.replaceAll(":", ""));
        int end = Integer.valueOf(endTime.replaceAll(":", ""));
        if (begin > end) {
            return set <= end || set >= begin;
        } else {
            return set >= begin && set <= end;
        }
    }

    /**
     * 格式化日期
     *
     * @param dateFormat yyyy-MM-08 16:30:00
     * @return
     * @author zhuhongjiang
     */
    public static Date getDateFormat(Date datetoFormat, String dateFormat) {

        String[] dateArr = dateFormat.split(" ")[0].split("-");
        String[] timeArr = dateFormat.split(" ")[1].split(":");
        Integer days = Integer.valueOf(dateArr[2]);
        Integer hours = Integer.valueOf(timeArr[0]);
        Integer minutes = Integer.valueOf(timeArr[1]);
        Integer seconds = Integer.valueOf(timeArr[2]);
        // 格式化时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetoFormat);
        calendar.add(Calendar.DATE, days);   // 考虑时区，此处add
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    /**
     * 获取本月第一天、最后一天（北京时间）
     *
     * @param type 0:本月， -1:上月
     * @return
     * @author zhuhongjiang
     */
    public static Date[] getDaysForMonth(Integer type) {
        Date startDate = null;
        Date endDate = null;
        try {
            Date nowDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(nowDate);
            calendar.add(Calendar.MONTH, type - 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 16);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            startDate = calendar.getTime();

            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            endDate = calendar.getTime();

        } catch (Exception e) {
            LOGGER.error("get day month exception", e);
        }
        return new Date[]{startDate, endDate};
    }

    /**
     * 获取一天的开始时间
     * 如：2020-12-18 00:00:00
     *
     * @return
     */
    public static Date getStartOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取一天的结束时间
     * 如：2020-12-18 23:59:59
     *
     * @return
     */
    public static Date getEndOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取一天的结束时间
     * 如：2020-12-18 23:59:59
     *
     * @return
     */
    public static Date strToDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getBeforeDateTime(Date time, Integer value) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.setTime(time);
        // 例如获取当前时间十分钟之前的时间: time: -10
        beforeTime.add(Calendar.MINUTE, value);
        return beforeTime.getTime();
    }

    /**
     * 当地时间转utc时间
     * @param localTime 当地时间
     * @param timeZone  [-12,+12]
     * @return
     */
    public static Date getSubHourTime(Date localTime, Integer timeZone) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.setTime(localTime);
        beforeTime.add(Calendar.HOUR, -timeZone);
        return beforeTime.getTime();
    }

    /**
     * 当地时间转utc时间
     * @param localTime 当地时间
     * @param timeZone  [-12,+12]
     * @return
     */
    public static Date getSubHourTime(String localTime, Integer timeZone) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar beforeTime = Calendar.getInstance();
        Date local = simpleDateFormat.parse(localTime);
        beforeTime.setTime(local);
        beforeTime.add(Calendar.HOUR, -timeZone);
        return beforeTime.getTime();
    }

    /**
     * 当前时间加几个小时后的时间
     * @param time 系统当前时间
     * @param duration
     * @return
     */
    public static Date getAfterHourTime(String time, Integer duration) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar beforeTime = Calendar.getInstance();
        Date local = simpleDateFormat.parse(time);
        beforeTime.setTime(local);
        beforeTime.add(Calendar.HOUR, duration);
        return beforeTime.getTime();
    }

    /**
     * 当前时间加几个小时后的时间
     * @param time 系统当前时间
     * @param duration
     * @return
     */
    public static Date getAfterDayTime(String time, Integer duration) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar afterTime = Calendar.getInstance();
        Date local = simpleDateFormat.parse(time);
        afterTime.setTime(local);
        afterTime.add(Calendar.DAY_OF_MONTH, duration);
        return afterTime.getTime();
    }

    /**
     * 当前时间加几天后的时间
     * @param time 系统当前时间
     * @param duration
     * @return
     */
    public static Date getAfterDayTime(Date time, Integer duration) throws Exception {
        Calendar afterTime = Calendar.getInstance();
        afterTime.setTime(time);
        afterTime.add(Calendar.DAY_OF_MONTH, duration);
        return afterTime.getTime();
    }

/*    public static void main(String[] args) {
        String timeTemp="2021-05-15 12:23:41";
        Integer localSendDays=18;
        try {
            Date afterDayTime = DateUtil.getAfterDayTime(timeTemp, localSendDays);
            System.out.println(DateUtil.date2TimeStr(afterDayTime));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /**
     * 当前时间加几个小时后的时间
     * @param duration
     * @return
     */
    public static Date getAfterHourTime(Integer duration) {
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.add(Calendar.HOUR, duration);
        return beforeTime.getTime();
    }
}
