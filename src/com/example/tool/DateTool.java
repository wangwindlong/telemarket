package com.example.tool;

import android.content.Context;
import android.text.format.Time;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-9
 * Time: 上午9:53
 */
public class DateTool {
    private static String date_time_format = "yyyy-MM-dd HH:mm:ss";
    private static String date_time_without_sec_format = "yyyy-MM-dd HH:mm";
    private static String date_format = "yyyy-MM-dd";
    private static String date_cn_format = "yyyy年MM月dd";
    public static Time t = new Time();
    public static Calendar calendar = Calendar.getInstance();

    public static String getCurYear() {
        t.setToNow();
        return String.valueOf(t.year);
    }

    public static int getIntCurYear() {
        t.setToNow();
        return t.year;
    }

    public static String getCurMonth() {
        t.setToNow();
        if (t.month + 1 < 10) {
            int month = t.month + 1;
            return "0" + month;
        }
        return String.valueOf(t.month + 1);
    }

    public static int getIntCurMonth() {
        t.setToNow();
        if (t.month + 1 < 10) {
            int month = t.month + 1;
            return month;
        }
        return t.month + 1;
    }

    public static String checkNextMonth(int check_month) {
        String month = "";
        if (check_month + 1 < 10) {
            int mon_th = check_month + 1;
            month = "0" + mon_th;
        } else {
            month = String.valueOf(check_month + 1);
        }
        return month;
    }

    public static String getCurDay() {
        t.setToNow();
        if (t.monthDay < 10) {
            int month = t.monthDay;
            return "0" + month;
        }
        return String.valueOf(t.monthDay);
    }

    public static int getIntCurDay() {
        t.setToNow();
        return t.monthDay;
    }

    public static String checkPreMonth(int check_month) {
        String month = "";
        if (check_month - 1 < 10) {
            int mon_th = check_month - 1;
            month = "0" + mon_th;
        } else {
            month = String.valueOf(check_month - 1);
        }
        return month;
    }

    public static String checkMonth(int check_month) {
        String month = "";
        if (check_month < 10) {
            int mon_th = check_month;
            month = "0" + mon_th;
        } else {
            month = String.valueOf(check_month);
        }
        return month;
    }

    /**
     * 使用预设格式将字符串转为Date
     * 返回结果2013-01-24 10:07
     */
    public static String getdatetimewithoutsecNow() {
        SimpleDateFormat df = new SimpleDateFormat(date_time_without_sec_format);
        return df.format(new Date());
    }


    public static String getDateTimeNow() {
        SimpleDateFormat df = new SimpleDateFormat(date_time_format);
        return df.format(new Date());
    }

    public static String getDateTimeNow(long datetime) {
        Date cur_date = new Date(datetime);
        SimpleDateFormat df = new SimpleDateFormat(date_time_format);
        return df.format(cur_date);
    }

    /**
     * 使用预设格式将字符串转为Date
     * 返回结果2013-01-24 10:07
     */
    public static String getdatetimewithoutsec() {
        SimpleDateFormat df = new SimpleDateFormat(date_time_without_sec_format);
        return df.format(new Date());
    }

    /**
     * 使用预设格式将字符串转为Date
     * 返回结果      2013-01-24 10:07:55
     */
    public static String getdatetimeNow(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(date_time_without_sec_format);
        return df.format(date);
    }

    public static String getDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(date_format);
        return df.format(date);
    }

    /**
     * 使用预设格式将字符串转为Date
     * 返回结果   2013-01-24
     */
    public static String getdateNow() {
        SimpleDateFormat df = new SimpleDateFormat(date_format);
        return df.format(new Date());
    }

    public static String getdateNowCn() {
        SimpleDateFormat df = new SimpleDateFormat(date_cn_format);
        return df.format(new Date());
    }


    /**
     * 使用预设格式将字符串转为Date
     * 参数格式 datetimewithoutsecParse("2012-01-02 13:09:23")
     * 返回结果Mon Jan 02 13:09:00 CST 2012
     * 参数格式 datetimewithoutsecParse("2012-01-02 13:09)
     * 返回结果Mon Jan 02 13:09:00 CST 2012
     */
    public static Date datetimewithoutsecParse(String strDate) {
        if (strDate == null) {
            return new Date();
        }
        return parse(strDate, date_time_without_sec_format);
    }

    /**
     * 使用预设格式将字符串转为Date
     * 参数格式 datetimeParse("2012-01-02 13:09:23")
     * 返回结果 Mon Jan 02 13:09:23 CST 2012
     */
    public static Date datetimeParse(String strDate) {
        if (strDate == null) {
            return new Date();
        }
        return parse(strDate, date_time_format);
    }

    /**
     * 使用预设格式将字符串转为Date
     * 参数值  dateParse("2012-01-02")
     * 返回结果 Mon Jan 02 00:00:00 CST 2012
     */
    public static Date dateParse(String strDate) {
        if (strDate == null) {
            return new Date();
        }
        return parse(strDate, date_format);
    }

    /**
     * 使用参数Format将字符串转为Date
     */
    public static Date parse(String strDate, String user_format) {
        SimpleDateFormat df = new SimpleDateFormat(user_format);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前日期的指定格式的字符串
     * 2013-06-05 12:23:36 转换为2013-06-05
     */
    public static String getDateTimeToDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        if (value == null || value.trim().equals("") || value.trim().length() < 10) {
            return getdateNow();
        } else {
            if (value.contains("T")) {
                value = value.split("T")[0];
            } else {
                value = value.split(" ")[0];
            }
        }
        return value;
    }

    //获取下next天 的 年 月 日
    public static String[] getNextDate(String year, String month, String day, int next) {
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        calendar.set(Calendar.MONTH, Integer.valueOf(month) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day) + next);
        return new String[]{String.valueOf(calendar.get(Calendar.YEAR)), getDate(calendar.get(Calendar.MONTH) + 1),
                getDate(calendar.get(Calendar.DAY_OF_MONTH))};
    }

    //获取下next个月的年和月
    public static String[] getNextMonth(String year, String month, int next) {
        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        calendar.set(Calendar.MONTH, Integer.valueOf(month) + next - 1);
        return new String[]{String.valueOf(calendar.get(Calendar.YEAR)), getDate(calendar.get(Calendar.MONTH) + 1)};
    }

    //获取这个月的月初日期 格式 2014-01-01
    public static String getCurrMonthFirstDay(){
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return getDate(calendar.getTime());
    }

    //获取这个月月末日期 格式2014-01-31
    public static String getCurrMonthLastDay(){
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        return getDate(calendar.getTime());
    }

    public static String getDate(int date) {
        if (date >= 10) {
            return String.valueOf(date);
        } else {
            return "0" + String.valueOf(date);
        }
    }

    /**
     * 获得指定日期的前一天
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = dateParse(specifiedDay);
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        String dayBefore = new SimpleDateFormat(date_format).format(c.getTime());
        return dayBefore;
    }

    /**
     * 获得指定日期的后一天
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = dateParse(specifiedDay);
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        String dayAfter = new SimpleDateFormat(date_format).format(c.getTime());
        return dayAfter;
    }

    /**
     * *
     */
    public static boolean checkWorkTime(String startTime, String finishTime) {
        try {
            String date = getdateNow() + " ";
            Date startDate = parse(date + startTime.trim(), date_time_without_sec_format);
            Date finishDate = parse(date + finishTime.trim(), date_time_without_sec_format);
            if (startDate.before(new Date()) && finishDate.after(new Date())) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证是否是当前日期***
     * 2013-05-12
     * 2013-05-02
     * 2013-5-12
     * 2013-5-2
     */
    public static boolean vaildteCurrDate(String strDate) {
        try {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat(date_format);
            //得到指定模范的时间
            Date d2 = sdf.parse(strDate);
            String dayAfter = sdf.format(d2.getTime());
            if (getdateNow().equals(dayAfter)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 时间累加
     */
    public static Date getSummationDate(String datetime, long waittime) {
        Date date = datetimeParse(datetime);
        long date_sum = date.getTime() + waittime;
        Date summationDate = new Date(date_sum);
        return summationDate;
    }

    /**
     * 时间累加
     */
    public static Date getSummationDate(Date date, long waittime) {
        long date_sum = date.getTime() + waittime;
        Date summationDate = new Date(date_sum);
        return summationDate;
    }

    //验证时间是否在规定的时间内
    public static boolean vaildteDateTime(Date date, String start_time, String finish_time) {
        try {
            Date startDate = datetimeParse(start_time);
            Date finishDate = datetimeParse(finish_time);
            if (startDate.before(date) && finishDate.after(date)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isSameDay(Date ori_date, String compare_date) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(ori_date);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateParse(compare_date));
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    public static boolean isSameDay(Date ori_date, Date compare_date) {
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(ori_date);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(compare_date);
        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                && calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);
    }

    //2013 0-5-06 14:22:00判断是否是休息时间，7：00-- 20：00
    public static boolean isWorkTime(String work_time) {
        try {
            String hour_minute = work_time.split(" ")[1];
            String[] hourMinute = hour_minute.split(":");
            int minuteOfDay = Integer.parseInt(hourMinute[0]) * 60 + Integer.parseInt(hourMinute[1]);// 从0:00分开是到目前为止的分钟数
            final int start = 7 * 60;// 起始时间 07:00的分钟数
            final int end = 20 * 60;// 结束时间 20:00的分钟数
            if (minuteOfDay >= start && minuteOfDay <= end) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return true;
        }
    }

    public static String formatDate(String date_string) {
        try {
            //设定时间的模板
            SimpleDateFormat sdf = new SimpleDateFormat(date_format);
            //得到指定模范的时间
            Date date = dateParse(date_string);
            return sdf.format(date);
        } catch (Exception e) {
        }
        return "";
    }

    static public List<String> trunkDate(String date) {
        return Arrays.asList(date.split("-"));
    }
}
