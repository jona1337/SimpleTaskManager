package taskManager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static final String DEFAULT_DATE_PATTERN = "dd.MM.yyyy HH:mm";
    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(DEFAULT_DATE_PATTERN);

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

}
