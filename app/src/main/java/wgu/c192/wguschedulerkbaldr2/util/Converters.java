package wgu.c192.wguschedulerkbaldr2.util;

import androidx.room.TypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Converters {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @TypeConverter
    public static String dateToString(Date date) {
        if (date == null) return null;
        return dateFormat.format(date);
    }

    @TypeConverter
    public static Date stringToDate(String dateStr) {
        if (dateStr == null) return null;
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
