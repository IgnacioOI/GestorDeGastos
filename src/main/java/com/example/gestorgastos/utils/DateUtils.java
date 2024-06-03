package com.example.gestorgastos.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    //Configura el formato de la fecha y devuelve en un array las dos fechas
    public static String[] getCurrentWeekDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] weekDates = new String[2];
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        weekDates[0] = sdf.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_WEEK, 6);
        weekDates[1] = sdf.format(calendar.getTime());

        return weekDates;
    }

    public static String[] getCurrentMonthDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] monthDates = new String[2];
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        monthDates[0] = sdf.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        monthDates[1] = sdf.format(calendar.getTime());

        return monthDates;
    }

    public static String[] getCurrentYearDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] yearDates = new String[2];
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DAY_OF_YEAR, 1);
        yearDates[0] = sdf.format(calendar.getTime());

        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
        yearDates[1] = sdf.format(calendar.getTime());

        return yearDates;
    }


    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }
}
