package pt.andrew.blisschallenge.helpers;

import android.annotation.SuppressLint;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class DateHelper {

    private static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    public static String formatDate(Date dateToConvert) {
        @SuppressLint("SimpleDateFormat") Format formatter = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        return formatter.format(dateToConvert);
    }

}
