package com.example.gestorgastos.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarPersonalizado {

    public interface OnDateSelectedListener {
        void onDateRangeSelected(String startDate, String endDate);
    }

    private Context context;
    private OnDateSelectedListener listener;
    private SimpleDateFormat dateFormat;
    private Calendar calendar;

    public CalendarPersonalizado(@NonNull Context context, @Nullable OnDateSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.calendar = Calendar.getInstance();
    }

    public void mostrarCalendario() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    String startDate = dateFormat.format(calendar.getTime());

                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    String endDate = dateFormat.format(calendar.getTime());

                    if (listener != null) {
                        listener.onDateRangeSelected(startDate, endDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().findViewById(getYearPickerId()).performClick();

        datePickerDialog.show();
    }

    private int getYearPickerId() {
        int yearId = context.getResources().getIdentifier("year", "id", "android");
        if (yearId == 0) {
            yearId = context.getResources().getIdentifier("android:id/year", null, null);
        }
        return yearId;
    }
}
