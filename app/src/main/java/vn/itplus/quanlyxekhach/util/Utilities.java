package vn.itplus.quanlyxekhach.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AnhlaMrDuc on 14-May-16.
 */
public class Utilities {

    public static String getDateString(String format, Date date) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String dateString = dateFormat.format(date);
        return dateString;
    }
}
