package pt.andrew.blisschallenge.helpers;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andrew.fernandes on 16/08/2019
 */
public class DateHelper {

    public static String formatDate(Date dateToConvert, Context context) {
        @SuppressLint("SimpleDateFormat") Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(dateToConvert);
    }

}
