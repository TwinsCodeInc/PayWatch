package cz.muni.fi.paywatch.app;

import android.content.Context;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 23. 4. 2017.
 */

public class Helpers {

    // Returns Date object for string formatted in localized format
    public static Date stringToDate(String dateString) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(PayWatchApplication.getContext());
        Date d = null;
        try {
            d = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    // Returns actual date formatted according to system localization
    public static String getActualDateString() {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(PayWatchApplication.getContext());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    // Returns formatted date string created from integer numbers of year, month and day (BEWARE: month numbers are 0-11)
    public static String intToDateString(int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(PayWatchApplication.getContext());
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        return dateFormat.format(c.getTime());
    }
}
