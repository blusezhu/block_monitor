package com.itheima.utils;

import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DateUtil {

    public static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter df2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    public static DateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter df4 = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static DateTimeFormatter df6 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    public static DateFormat df5 = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 日期格式yyyy-MM-dd
     */
    public static String DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 日期格式 yyyy-MM
     */
    public static String DATE_PATTERN_YM = "yyyy-MM";

    /**
     * 获取当前时间 时间戳
     *
     * @return
     */
    public static Long getUnixTimestamp() {
        return getLongByTime(LocalDateTime.now()) / 1000;
    }

    /**
     * LocalDateTime 转long
     *
     * @param localDateTime
     * @return
     */
    public static long getLongByTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static long getSecondLongByTime(LocalDateTime localDateTime) {
        return getLongByTime(localDateTime) / 1000;
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getDaysAgo(Date time, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.DAY_OF_MONTH, amount);
        return c.getTime();
    }

    /**
     * long(毫秒时间戳) 转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime getTimestampOfDateTime(Long time) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    }

    /**
     * long(毫秒时间戳) 兼容毫秒级 转LocalDateTime
     *
     * @return
     */
    public static LocalDateTime timeTolocalTime(Long time) {
        if (time > 16033721190L) {
            return getTimestampOfDateTime(time);
        }
        return LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.ofHours(8));
    }


    /**
     * String 转 LocalDateTime
     *
     * @return
     */
    public static LocalDateTime getTimeByString(String time) {
        return LocalDateTime.parse(time, df);
    }

    public static LocalDateTime getTimeByStringDf6(String time) {
        return LocalDateTime.parse(time, df6);
    }

    /**
     * LocalDateTime 转 string
     *
     * @return
     */
    public static String getStringByTime(LocalDateTime time) {
        return df.format(time);
    }

    //当前的时间字符串
    public static String getTimeStringNow() {
        return df.format(LocalDateTime.now());
    }

    /**
     * 判断时间字符串格式
     *
     * @param str
     * @return
     */
    public static boolean isValidDate(String str) {
        if (StringUtils.isEmpty(str)) return false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            format.setLenient(false);
            format.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidDate(String str, String reg) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(reg)) return false;
        SimpleDateFormat format = new SimpleDateFormat(reg);
        try {
            format.setLenient(false);
            format.parse(str);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 获取当前时间字符串 年月日
     *
     * @return
     */
    public static String dateNow() {
        LocalDateTime time = LocalDateTime.now();
        return df4.format(time);
    }

    /**
     * 获取当前时间字符串 年月日时分秒
     * @return
     */
    public static String dateFormatNow(){
        LocalDateTime time = LocalDateTime.now();
        return df2.format(time);
    }

    /**
     * 获取某天的00:00:00
     *
     * @param dateTime
     * @return
     */
    public static String getDayStart(LocalDateTime dateTime) {
        return getStringByTime(dateTime.with(LocalTime.MIN));
    }

    public static Date getDayStart(Date date) {
        Calendar dateStart = Calendar.getInstance();
        dateStart.setTime(date);
        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        return dateStart.getTime();
    }

    /**
     * 获取某天的23:59:59
     *
     * @param dateTime
     * @return
     */
    public static String getDayEnd(LocalDateTime dateTime) {
        return getStringByTime(dateTime.with(LocalTime.MAX));
    }

    /**
     * 获取某月第一天的00:00:00
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static String getFirstDayOfMonth(LocalDateTime dateTime) {
        return getStringByTime(dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN));
    }

    public static LocalDateTime getFirstDayOfMonth2(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取某月最后一天的23:59:59
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static String getLastDayOfMonth(LocalDateTime dateTime) {
        return getStringByTime(dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX));
    }

    public static LocalDateTime getLastDayOfMonth2(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }

    /**
     * 获取某月第一天的00:00:00
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static LocalDateTime getFirstDayOfMonthOnLocalDate(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    }

    /**
     * 获取某月最后一天的23:59:59
     *
     * @param dateTime LocalDateTime对象
     * @return
     */
    public static LocalDateTime getLastDayOfMonthOnLocalDate(LocalDateTime dateTime) {
        return dateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    }


    /**
     * 获取两时间相差天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long getDay(LocalDateTime startTime, LocalDateTime endTime) {
        return endTime.toLocalDate().toEpochDay() - startTime.toLocalDate().toEpochDay();
    }


    public static List<String> findDates(String dBegin, String dEnd) throws ParseException {
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        lDate.add(sd.format(sd.parse(dBegin)));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        Date end = sd.parse(dEnd);
        calBegin.setTime(sd.parse(dBegin));
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(end);
        // 测试此日期是否在指定日期之后
        while (end.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }

    //获取月份
    public static List<Integer> getMonths(String startTime, String endTime) {
        List<Integer> months = new CopyOnWriteArrayList<>();
        int end = Integer.parseInt(endTime);
        int start = Integer.parseInt(startTime);
        if (start == end) {
            months.add(start);
        } else {
            for (int i = start; i <= end; i++) {
                months.add(i);
            }
        }
        return months;
    }

    /**
     * 根据月份获取月初  月末时间
     *
     * @param date
     * @return
     */
    public static String[] getBetweenMonth(String date) {
        Integer year = Integer.parseInt(date.split("-")[0]);
        Integer month = Integer.parseInt(date.split("-")[1]);
        LocalDate localDate = LocalDate.of(year, month, 1);
        int maxDay = localDate.lengthOfMonth();
        date = date.split("-")[0] + "-" + String.format("%02d", month);
        return new String[]{date + "-01 00:00:00", date + "-" + String.format("%02d", maxDay) + " 23:59:59"};
    }


    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/d");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDateByFormat(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM/d");
        String result = format.format(today);
        return result;
    }

    /**
     * 获取时间字符串
     *
     * @param date
     * @return
     */
    public static String getStringByTimeOfDate(Date date) {
        return df3.format(date);
    }

    public static String getStringByTimeOfDate2(Date date) {
        return df5.format(date);
    }


    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * 获取当前时间之后几天后的 时间戳(毫秒)token
     *
     * @return
     */
    public static Long getUnixTimestampPlusDaysOnToken(long days) {
        return LocalDateTime.now().plusDays(days).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 计算两个时间相差的毫秒数
     *
     * @param timestr
     * @param timeEnd
     * @return
     */
    public static Long getMisBetweenTime(LocalDateTime timestr, LocalDateTime timeEnd) {
        Duration duration = Duration.between(timestr, timeEnd);
        return duration.toMillis();
    }

    /**
     * 计算两个时间相差的分钟数
     *
     * @param timestr
     * @param timeEnd
     * @return
     */
    public static Long getMinutesBetweenTime(LocalDateTime timestr, LocalDateTime timeEnd) {
        Duration duration = Duration.between(timestr, timeEnd);
        return duration.toMinutes();
    }

    /**
     * localDate转String  yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getStringByLocalDate(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN);
        return date.format(fmt);
    }

    /**
     * localDate转String  yyyy-MM
     *
     * @param date
     * @return
     */
    public static String getStringYMByLocalDate(LocalDate date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN_YM);
        return date.format(fmt);
    }

    public static void main(String[] args) {
        /*System.out.println(dateNow());
        LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(1603953639L, 0, ZoneOffset.ofHours(8));
        LocalDateTime localDateTime2 = getTimestampOfDateTime(1604025514004L);
        System.out.println(localDateTime2);*/
        System.out.println(getTimeByStringDf6("20201110000000"));
        System.out.println("20201110".length());

        System.out.println(getDayStart(LocalDateTime.now()));

        System.out.println(getDaysAgo(new Date(), -30));
        System.out.println(getStringByLocalDate(LocalDate.now()).replace("-", ""));
        // System.out.println(dateNow());

    }

    public static String[] getLastMonth(String yearAndMonth, int reduce) {
        String[] timeStr = null;
        try {
            Date currentDate = new SimpleDateFormat("yyyy-M").parse(yearAndMonth);
            String currentStr = new SimpleDateFormat("yyyy-MM-dd").format(currentDate) + " 00:00:00";
            LocalDateTime billTime = DateUtil.getTimeByString(currentStr);
            int todayMonthValue = billTime.getMonthValue();
            int monthAgo = todayMonthValue - reduce;
            int todayYearValue = billTime.getYear();
            if (monthAgo <= 0) {
                todayYearValue = todayYearValue - 1;
                monthAgo += 12;
            }
            String customDate = todayYearValue + "-" + monthAgo;
            Date newDate = new SimpleDateFormat("yyyy-M").parse(customDate);
            String newDateStr = new SimpleDateFormat("yyyy-MM-dd").format(newDate) + " 00:00:00";
            timeStr = DateUtil.getBetweenMonth(newDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    public static String getDistanceTimes(Long startTime, Long endTime) {
        long day = 0;
        long hour = 0;
        long minute = 0;
        long second = 0;
        try {
//            one = df.parse(starttime);
//            two = df.parse(endtime);
            long time1 = startTime;
            long time2 = endTime;
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            minute = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            second = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dayStr = "";
        if (String.valueOf(day).length() < 2) {
            dayStr = "0" + day;
        } else {
            dayStr = "" + day;
        }
        String hourStr = "";
        if (String.valueOf(hour).length() < 2) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        String minuteStr = "";
        if (String.valueOf(minute).length() < 2) {
            minuteStr = "0" + minute;
        } else {
            minuteStr = "" + minute;
        }
        String secondStr = "";
        if (String.valueOf(second).length() < 2) {
            secondStr = "0" + second;
        } else {
            secondStr = "" + second;
        }
        String time = dayStr + ":" + hourStr + ":" + minuteStr + ":" + secondStr;
//        if (day <= 0) {
//            time = hourStr + ":" + minuteStr + ":" + secondStr;
//        }
        long[] times = {day, hour, minute, second};
        return time;
    }

    /**
     * 根据起始时间和结束时间得到期间所有的日期集合
     *
     * @param startDate 起始时间
     * @param endDate   结束时间
     * @return
     */
    public static List<String> getBetweenDateList(String startDate, String endDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime;
        Date endTime;
        List<String> dateList = new LinkedList<>();
        try {
            startTime = df.parse(startDate);
            endTime = df.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateList;
        }
        while (true) {
            int compareTo = startTime.compareTo(endTime);
            if (compareTo > 0) break;
            dateList.add(DateUtil.d2s(startTime, "yyyy-MM-dd"));
            startTime.setTime(startTime.getTime() + 1000 * 60 * 60 * 24);
        }
        return dateList;
    }

    public static String d2s(Date d, String mask) {
        SimpleDateFormat sdf = new SimpleDateFormat(mask);
        return sdf.format(d);
    }
}
