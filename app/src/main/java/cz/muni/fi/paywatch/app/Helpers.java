package cz.muni.fi.paywatch.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

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

    // Returns formated string from date object
    public static String getDateString(Date d) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(PayWatchApplication.getContext());
        return dateFormat.format(d);
    }

    // Returns formatted date string created from integer numbers of year, month and day (BEWARE: month numbers are 0-11)
    public static String intToDateString(int year, int monthOfYear, int dayOfMonth) {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(PayWatchApplication.getContext());
        Calendar c = Calendar.getInstance();
        c.set(year, monthOfYear, dayOfMonth);
        return dateFormat.format(c.getTime());
    }

    // Returns normalized (rounded) double
    public static Double parseDouble(String value) {
        try {
            Double d = Double.parseDouble(value);
            d = Double.parseDouble(String.format("%.2f", d));
            return d;
        } catch (NumberFormatException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    // Hides the keyboard
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    // Shows OK dialog
    public static void showOkDialog(Activity activity, String title, String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}
