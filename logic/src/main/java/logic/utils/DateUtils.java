package logic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    private static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy HH:mm";
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

    public static Date getDate(LocalDate localDate) {

        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        return Date.from(instant);

    }

    public static LocalDate getLocalDate(Date date) {

        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();

    }


    public static String format(Date date, SimpleDateFormat sdf) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    public static String format(Date date) {
        return format(date, DEFAULT_DATE_FORMAT);
    }


    public static Date parse(String dateString, SimpleDateFormat sdf) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parse(String dateString) {
        return parse(dateString, DEFAULT_DATE_FORMAT);
    }


    public static boolean isValidDate(String dateString, SimpleDateFormat sdf) {
        return DateUtils.parse(dateString, sdf) != null;
    }

    public static boolean isValidDate(String dateString) {
        return isValidDate(dateString, DEFAULT_DATE_FORMAT);
    }

    public static boolean isFormatEquals(Date date1, Date date2, SimpleDateFormat sdf) {
        return format(date1, sdf).equals(format(date2, sdf));
    }

    public static boolean isFormatEquals(Date date1, Date date2) {
        return isFormatEquals(date1, date2, DEFAULT_DATE_FORMAT);
    }


    public static void setDateHours(Date date, Date hoursDate) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Calendar hoursCalendar = new GregorianCalendar();
        hoursCalendar.setTime(hoursDate);

        calendar.set(Calendar.HOUR_OF_DAY, hoursCalendar.get(Calendar.HOUR_OF_DAY));
        date.setTime(calendar.getTimeInMillis());

    }

    public static void setDateMinutes(Date date, Date minutesDate) {

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Calendar minutesCalendar = new GregorianCalendar();
        minutesCalendar.setTime(minutesDate);

        calendar.set(Calendar.MINUTE, minutesCalendar.get(Calendar.MINUTE));
        date.setTime(calendar.getTimeInMillis());

    }

    public static void setDateSecondsToZero(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        Calendar zeroCalendar = new GregorianCalendar();
        zeroCalendar.setTimeInMillis(0);

        calendar.set(Calendar.SECOND, zeroCalendar.get(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, zeroCalendar.get(Calendar.MILLISECOND));
        date.setTime(calendar.getTimeInMillis());
    }

    public static void setDateTime(Date date, Date timeDate) {
        setDateHours(date, timeDate);
        setDateMinutes(date, timeDate);
    }



}
