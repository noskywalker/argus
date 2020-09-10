package com.monitor.argus.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 日期格式化工具类
 *
 * @Author null
 * @Date 2014-5-7 上午11:29:38
 */
public class DateUtil {

    /** 1day=86400000ms=0x5265c00L */
    // private static long DAY_IN_MILLISECOND = 0x5265c00L;

    /** 日期格式：yyyyMMdd */
//  private static final SimpleDateFormat shortSimpleDateFormat = new SimpleDateFormat("yyyyMMdd");

    /** 日期格式：yyyy-MM-dd */
//    private static final SimpleDateFormat longSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /** 日期格式：yyyy.MM.dd */
//    private static final SimpleDateFormat longSimpleDateDotFormat = new SimpleDateFormat("yyyy.MM.dd");

    /** 日期格式：yyyy年MM月dd日 */
//    private static final SimpleDateFormat longCnSimpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");

    /** 时间格式：yyyy-M-d */
//    private static final SimpleDateFormat midSimpleDateFormat = new SimpleDateFormat("yyyy-M-d");

    /** 时间格式：M/d/yyyy */
//    private static final SimpleDateFormat midSimpleDateFormatForEasyUi = new SimpleDateFormat("M/d/yyyy");

    /** 时间格式：yyyy年M月d日 */
//    private static final SimpleDateFormat midCnSimpleDateFormat = new SimpleDateFormat("yyyy年M月d日");

    /** 日期格式：MMM dd */
//    private static final SimpleDateFormat shortUSSimpleDateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

    /** 日期格式：MMM dd,yyyy */
//    private static final SimpleDateFormat longUSSimpleDateFormat = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH);

    /** 时间格式：yyyyMMddHHmmss */
//    private static final SimpleDateFormat longTimeSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    /** 时间格式：yyyy-MM-dd HH:mm:ss */
//    private static final SimpleDateFormat longTimePlusSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /** 时间格式：MM-dd HH:mm */
//    private static final SimpleDateFormat MMddHHmmSimpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    /** 时间格式：yyyy-MM-dd HH:mm */
//    private static final SimpleDateFormat longTimePlusNoSecondSimpleDateFormat = new SimpleDateFormat(
//            "yyyy-MM-dd HH:mm");

    /** 时间格式：yyyy年MM月dd日 HH:mm */
//    private static final SimpleDateFormat shortLongTimePlusCnSimpleDateFormat = new SimpleDateFormat(
//            "yyyy年MM月dd日 HH:mm");

    /** 时间格式：yyyyMMddHHmmssSSSS */
//    private static final SimpleDateFormat longTimePlusMillSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");

    /** 时间格式：HH:mm */
//    private static final SimpleDateFormat HHmmSimpleDateFormat = new SimpleDateFormat("HH:mm");
    /** 日期格式：MM-dd */
//    private static final SimpleDateFormat MMddSimpleDateFormat = new SimpleDateFormat("MM-dd");

    /**
     * 日期格式：MM-dd
     */
//    private static final SimpleDateFormat CHN_DATE_FORMAT = new SimpleDateFormat("MM月dd号");
    public DateUtil() {
    }

    /**
     * 时间戳转换为date 型
     *
     * @author dyc
     * @date 2015年1月12日上午11:38:52
     */
    public static Date getDateFromUnix(String unixDate) {
        SimpleDateFormat fm1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        SimpleDateFormat fm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long unixLong = 0;
        unixLong = Long.parseLong(unixDate) * 1000;

        Date date = new Date();
        try {
            date = fm2.parse(fm2.format(unixLong));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 根据日期获取日期字符串：yyyyMMdd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateShortStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyyMMdd").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyyMMddHH
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateShorthhStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyyMMddHH").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     *
     * @return
     * @Author null
     * @Date 2014-6-18 下午05:26:28
     * @Version V1.0
     */
    public static Date getDateHHLong(String dateStr) {
        Date date;
        try {
            if (!dateStr.isEmpty()) {
                date = new SimpleDateFormat("yyyyMMddHH").parse(dateStr);
            } else {
                date = null;
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy-MM-dd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy.MM.dd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateDateDotStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期字符串(yyyy-MM-dd)获取日期
     *
     * @param dateStr 日期字符串(yyyy-MM-dd)
     * @return
     * @Author null
     * @Date 2014-6-18 下午05:26:28
     * @Version V1.0
     */
    public static Date getDateLong(String dateStr) {
        Date date;
        try {
            if (!dateStr.isEmpty()) {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            } else {
                date = null;
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据日期时间获取日期(只获取日期，不获取时间：即当天00:00:00)
     *
     * @param date 日期
     * @return Date 00:00:00
     * @Author null
     * @Date 2014-6-18 下午05:26:28
     * @Version V1.0
     */
    public static Date getDateLong(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据日期时间获取日期(只获取日期，和小时，不获取分钟和秒：即当天XX:00:00)
     *
     * @param date 日期
     * @return Date XX:00:00
     * @Author aijiaHua
     * @Date 2015-6-4 下午02:26:28
     * @Version V1.0
     */
    public static Date getDateHourStart(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getDateHourEnd(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy年MM月dd日
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongCnStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy年MM月dd日").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy-MM-dd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateMidStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy-M-d").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：M/d/yyyy
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-21 下午04:56:37
     * @Version V1.0
     */
    public static String getDateMidForEasyUiStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("M/d/yyyy").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy年MM月dd日
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateMidCnStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy年M月d日").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：MMM dd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateShortUSStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("MMM dd", Locale.ENGLISH).format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy-MM-dd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongUSStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("MMM dd,yyyy", Locale.ENGLISH).format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyyMMddHHmmss
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateTimeStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 据日期获取日期字符串：yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static String getSimpleDateTimeStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy-MM-dd HH:mm
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongTimePlusNoSecondStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy-MM-dd HH
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongTimePlusNoMinuteStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat(
                        "yyyy-MM-dd HH").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy-MM-dd HH:mm
     *
     * @param dateStr
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static Date getDateLongTimePlusNoSecond(String dateStr) {
        Date date;
        try {
            if (!dateStr.isEmpty()) {
                date = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm").parse(dateStr);
            } else {
                date = null;
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 根据日期获取日期字符串：yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongTimePlusStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * @Description: 根据yyyy-MM-dd HH:mm:ss 得到日期
     * @author zhanlei
     * @date 2014-8-1 下午04:59:29
     * @version V1.0
     */
    public static Date getSimpleDateTime(String dateStr) {
        Date date;
        try {
            if (!dateStr.isEmpty()) {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
            } else {
                date = null;
            }
            return date;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @Description: 得到 MM-dd HH:mm
     * @author zhanlei
     * @date 2014-7-16 上午11:29:46
     * @version V1.0
     */
    public static String getMMddHHmmSimpleDateStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("MM-dd HH:mm").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：yyyy年MM月dd日 HH:mm
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongTimePlusCnStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat(
                        "yyyy年MM月dd日 HH:mm").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串yyyyMMddHHmmssSSSS
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author null
     * @Date 2014-5-7 上午11:44:40
     * @Version V1.0
     */
    public static String getDateLongTimePlusMillStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 返回年月日的字符串 yyyy-MM-dd
     */
    public static String getDateStr(String dateTimeStr) {
        String dateStr = "";
        Date date;
        try {
            if (!dateTimeStr.isEmpty()) {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(dateTimeStr);
                dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
            } else {
                date = null;
            }
        } catch (Exception e) {
            return null;
        }
        return dateStr;
    }

    /**
     * 获取两个日期的第一天
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     * @author alex zhang
     * @author
     * @CreateDate 2015年9月24日 下午12:31:30
     */
    public static Date getFirstDateFromTowDate(Date beginDate, Date endDate) throws ParseException {
        if ((beginDate == null) || (endDate == null)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        int result = 0;
        Calendar endDateCalendar = Calendar.getInstance();
        Calendar beginDateCalendar = Calendar.getInstance();

        beginDateCalendar.setTime(df.parse(df.format(beginDate)));
        endDateCalendar.setTime(df.parse(df.format(endDate)));

        result = (endDateCalendar.get(1) - beginDateCalendar.get(1)) * 12 + endDateCalendar.get(2)
                - beginDateCalendar.get(2);
        beginDateCalendar.add(2, result);

        if (endDate.before(beginDateCalendar.getTime())) {
            beginDateCalendar.add(2, -1);
            if (beginDate.after(beginDateCalendar.getTime())) {
                return beginDate;
            }
        }
        return df.parse(df.format(beginDateCalendar.getTime()));
    }

    /**
     * 获取两个日期间的月份数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     * @author alex zhang
     * @author
     * @CreateDate 2015年9月24日 下午12:33:59
     */
    public static Integer getMonthsFromTowDate(Date beginDate, Date endDate) throws ParseException {
        if ((beginDate == null) || (endDate == null)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        int result = 0;
        Calendar beginDateCalendar = Calendar.getInstance();
        Calendar endDateCalendar = Calendar.getInstance();
        beginDateCalendar.setTime(df.parse(df.format(beginDate)));
        endDateCalendar.setTime(df.parse(df.format(endDate)));
        result = (endDateCalendar.get(1) - beginDateCalendar.get(1)) * 12 + endDateCalendar.get(2)
                - beginDateCalendar.get(2);
        return Integer.valueOf(result);
    }

    /**
     * 获取两个日期间的天数
     *
     * @param beginDate
     * @param endDate
     * @return
     * @throws ParseException
     * @author alex zhang
     * @author
     * @CreateDate 2015年9月24日 下午12:37:40
     */
    public static Integer getDaysFromTowDate(Date beginDate, Date endDate) throws ParseException {
        if ((beginDate == null) || (endDate == null)) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date endDateTmp = df.parse(df.format(endDate));
        Date beginDateTmp = df.parse(df.format(beginDate));
        long l = endDateTmp.getTime() - beginDateTmp.getTime();
        long between_days = l / 86400000L;
        return Integer.valueOf(Integer.parseInt(String.valueOf(between_days)));
    }

    /**
     * @param firstDate
     * @param SecondDate
     * @return
     * @author alex zhang
     * @author
     * @CreateDate 2015年9月24日 下午12:24:20
     */
    public static int getMonthsTwoDateSub(String firstDate, String SecondDate) {
        Date date1 = getDateLong(firstDate);
        Date date2 = getDateLong(SecondDate);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @param dateStr1
     * @param dateStr2
     * @return 相差的天数
     * @Description: date2 - date1 格式 2012-01-01
     * @author zhanlei
     * @date 2015-3-25 下午03:19:38
     * @version V1.0
     */
    public static int getTwoDateSub(String dateStr1, String dateStr2) {
        Date date1 = getDateLong(dateStr1);
        Date date2 = getDateLong(dateStr2);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @param date1
     * @param date2
     * @return
     * @Description: 相差的天数 date2 - date1
     * @author zhanlei
     * @date 2015-4-17 下午06:31:16
     * @version V1.0
     */
    public static int getTwoDateSub(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * @param date1
     * @param date2
     * @return
     * @Description: 相差的小时数 date2 - date1
     * @author zhanlei
     * @date 2015-4-17 下午06:31:16
     * @version V1.0
     */
    public static int getTwoDateHoursSub(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_hours = (time2 - time1) / (1000 * 3600);

        return Integer.parseInt(String.valueOf(between_hours));
    }

    /**
     * 计算两个日期之间相差的月数
     *
     * @param date1
     * @param date2 date2 - date1
     * @return
     */
    public static int getMonths(Date date1, Date date2) {
        int iMonth = 0;
        int flag = 0;
        try {
            Calendar objCalendarDate1 = Calendar.getInstance();
            objCalendarDate1.setTime(date1);

            Calendar objCalendarDate2 = Calendar.getInstance();
            objCalendarDate2.setTime(date2);

            if (objCalendarDate2.equals(objCalendarDate1))
                return 0;
            if (objCalendarDate1.after(objCalendarDate2)) {
                Calendar temp = objCalendarDate1;
                objCalendarDate1 = objCalendarDate2;
                objCalendarDate2 = temp;
            }
            if (objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1.get(Calendar.DAY_OF_MONTH))
                flag = 1;

            if (objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1.get(Calendar.YEAR))
                iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1.get(Calendar.YEAR)) * 12
                        + objCalendarDate2.get(Calendar.MONTH) - flag) - objCalendarDate1.get(Calendar.MONTH);
            else
                iMonth = objCalendarDate2.get(Calendar.MONTH) - objCalendarDate1.get(Calendar.MONTH) - flag;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return iMonth;
    }

    /**
     * 返回昨天的日期 yyyy-MM-dd
     */
    public static Date getLastDay() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    /**
     * @Description: 返回昨天的日期 yyyy-MM-dd
     * @author zhanlei
     * @date 2014-5-16 上午11:08:49
     * @version V1.0
     */
    public static String getLastDayOfString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(getLastDay());
    }

    /**
     * @Description: 返回今天的日期 yyyy-MM-dd
     * @author zhanlei
     * @date 2014-5-16 上午11:10:14
     * @version V1.0
     */
    public static String getDayOfString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * 根据所传的日期，加上所填的参数得到新的时间字符串
     *
     * @param year  年数
     * @param month 月数
     * @param day   天数
     * @param hour  小时数
     * @param min   分钟数
     * @param date  date为null是默认为当前时间
     * @return
     * @Version V1.0
     */
    public static String getCalculateDayStr(int year, int month, int day, int hour, int min, Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE, min);
        return getDateLongTimePlusStr(cal.getTime());
    }

    /**
     * 根据所传的日期，加上所填的参数得到新的时间字符串
     *
     * @param year  年数
     * @param month 月数
     * @param day   天数
     * @param hour  小时数
     * @param min   分钟数
     * @param date  date为null是默认为当前时间
     * @return
     * @Version V1.0
     */
    public static Date getCalculateDay(int year, int month, int day, int hour, int min, Date date) {
        if (date == null) {
            date = new Date();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE, min);
        return cal.getTime();
    }

    /**
     * 当前时间加上所填的参数得到一个时间
     */
    public static String getCalculateDay(int year, int month, int day, int hour, int min) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);
        cal.add(Calendar.HOUR_OF_DAY, hour);
        cal.add(Calendar.MINUTE, min);
        return getDateLongTimePlusStr(cal.getTime());
    }

    /**
     * @param type 0最小时间字符串 1最大时间字符串
     * @return
     * @Description:获取当天时间的字符串
     * @CreateDate: 2015-6-4 下午06:07:46
     * @Version: V1.0
     */
    public static String getCurrentStr(int type) {
        String date = getDateLongStr(new Date());
        if (type == 0) {
            return date + " 00:00:00";
        } else if (type == 1) {
            return date + " 23:59:59";
        } else {
            return "";
        }
    }

    /**
     * true是过期 false不过期(含当天) date是日期格式 yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static boolean validateOutTime(Date date) {
        if (date.getTime() >= getDateLong(new Date()).getTime()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据日期获取日期字符串：HH:mm
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author aijia
     * @Date 2015-7-2 下午18:00:00
     * @Version V1.0
     */
    public static String getTimeHHmmStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("HH:mm").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 根据日期获取日期字符串：MM-dd
     *
     * @param date
     * @return 日期String字符串
     * @throws Exception
     * @Author aijia
     * @Date 2015-7-9 下午18:00:00
     * @Version V1.0
     */
    public static String getDateMMddStr(Date date) {
        String nowDate = "";
        try {
            if (date != null) {
                nowDate = new SimpleDateFormat("MM-dd").format(date);
            }
            return nowDate;
        } catch (Exception e) {
            return "";
        }
    }

    public static long getCurrentDayTimeInMillis(Calendar dateTime) {
        try {
            long ms = 1000 * (dateTime.get(Calendar.HOUR_OF_DAY) * 3600 + dateTime.get(Calendar.MINUTE) * 60
                    + dateTime.get(Calendar.SECOND));// 毫秒数
            return ms;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 取得当天最大时间戳
     */
    public static long getCurDayTimeMillis() {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime();
    }

    /**
     * 取得当月最大时间戳
     */
    public static long getCurMonthTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 取得当天剩余的秒数
     */
    public static int expireSecondsOfDay() {
        return (int) ((getCurDayTimeMillis() - System.currentTimeMillis()) / 1000);
    }

    /**
     * 取得当月剩余的秒数
     */
    public static int expireSecondsOfMonth() {
        return (int) ((getCurMonthTimeMillis() - System.currentTimeMillis()) / 1000);
    }

    /**
     * 返回Unix 时间戳 ，date为null时，取当前时间
     *
     * @param date
     * @return
     * @author null
     * @date 2015年12月3日 上午11:48:12
     */
    public static Long getUnixtimestamp(Date date) {
        if (date == null) {
            date = new Date();
        }
        try {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date))).getTime() / 1000L;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * retrieve the day of today
     *
     * @return day of today
     */
    public static int getCurrentDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int datenum = c.get(Calendar.DATE);
        return datenum;
    }

    public static Date firstDayOfMonth() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date theDate = calendar.getTime();

        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        gcLast.set(Calendar.HOUR_OF_DAY, 0);
        gcLast.set(Calendar.MINUTE, 0);
        gcLast.set(Calendar.SECOND, 0);
        return gcLast.getTime();
    }

    /**
     * 根据日期获取日期字符串：10月2号
     */
    public static String getChnDateStr(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        return month + "月" + day + "号";
    }

    /**
     *
     * Description: 日期字符串转换成日期类型
     * @Version1.0 2017年9月3日 下午6:49:12 by zhenboliu 创建
     * @param date
     * @param parten
     * @return
     */
    public static String formatDate2String(Date date,String parten) {
        if(null!=date){
            SimpleDateFormat sdf = new SimpleDateFormat(parten);
            return sdf.format(date);
        }
        return null;
    }

    public static void main(String args[]) {
        System.out.println(getChnDateStr(new Date()));
        Date date = new Date(1428422400000L);
        System.out.println(date);
        System.out.println(getDateLongStr(date));
    }

}

