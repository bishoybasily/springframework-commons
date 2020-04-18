package com.github.bishoybasily.springframework.commons.core.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author bishoybasily
 * @since 3/19/20
 */
public class DateUtils {

    public static Date ahead(int type, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(type, value);
        return calendar.getTime();
    }

    public static Date ahead(int type, int value, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, value);
        return calendar.getTime();
    }

    public static Date behind(int type, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(type, -value);
        return calendar.getTime();
    }

    public static Date behind(int type, int value, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, -value);
        return calendar.getTime();
    }

    public static Date now() {
        return new Date();
    }

    public static String formatted(String format, TimeZone timeZone) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(now());
    }

}
