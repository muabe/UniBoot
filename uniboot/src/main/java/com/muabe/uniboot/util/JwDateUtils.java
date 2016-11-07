package com.muabe.uniboot.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author 오재웅(JaeWoong-Oh)
 * @email markjmind@gmail.com
 * @since 2015-12-29
 */
public class JwDateUtils {
    private static Locale locale = Locale.KOREA;

    public static void setDefalutLocal(Locale locale){
        JwDateUtils.locale = locale;
    }

    public static String getFormat(Date date,  String format, Locale locale)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(format, locale);
        return formatter.format(date);
    }

    public static String getFormat(Date date,  String format)
    {
        return JwDateUtils.getFormat(date, format, locale);
    }

    public static int betweenSec(Date from, Date to){
        long duration = from.getTime() - to.getTime();
        return (int) (duration / (1000));
    }

    public static int betweenMin(Date from, Date to){
        long duration = from.getTime() - to.getTime();
        return (int) (duration / (1000 * 60));
    }

    public static int betweenHour(Date from, Date to){
        long duration = from.getTime() - to.getTime();
        return (int) (duration / (1000 * 60 * 60));
    }

    public static int betweenDay(Date from, Date to){
        long duration = from.getTime() - to.getTime();
        return (int) (duration / (1000 * 60 * 60 *24));
    }

    public static int betweenMonth(Date from, Date to){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", locale);
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM", locale);
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd", locale);

        int fromYear = Integer.parseInt(yearFormat.format(from));
        int toYear = Integer.parseInt(yearFormat.format(to));
        int fromMonth = Integer.parseInt(monthFormat.format(from));
        int toMonth = Integer.parseInt(monthFormat.format(to));
        int fromDay = Integer.parseInt(dayFormat.format(from));
        int toDay = Integer.parseInt(dayFormat.format(to));

        int result = 0;
        result += ((toYear - fromYear) * 12);
        result += (toMonth - fromMonth);

        //        if (((toDay - fromDay) < 0) ) result += fromDate.compareTo(toDate);
        // ceil과 floor의 효과
        if (((toDay - fromDay) > 0))
            result += to.compareTo(from);

        return result;
    }

    public static int betweenYear(Date from, Date to){
        long duration = from.getTime() - to.getTime();
        return (int) (duration / (1000 * 60 * 60 * 24 * 356));
    }

    public static boolean equalsSec(Date a, Date b){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMMddhhMMss", locale);
        return yearFormat.format(a).equals(yearFormat.format(b));
    }

    public static boolean equalsMin(Date a, Date b){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMMddhhMM", locale);
        return yearFormat.format(a).equals(yearFormat.format(b));
    }

    public static boolean equalsHour(Date a, Date b){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMMddhh", locale);
        return yearFormat.format(a).equals(yearFormat.format(b));
    }

    public static boolean equalsDay(Date a, Date b){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMMdd", locale);
        return yearFormat.format(a).equals(yearFormat.format(b));
    }

    public static boolean equalsMonth(Date a, Date b){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyyMM", locale);
        return yearFormat.format(a).equals(yearFormat.format(b));
    }

    public static boolean equalsYear(Date a, Date b){
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", locale);
        return yearFormat.format(a).equals(yearFormat.format(b));
    }
}
