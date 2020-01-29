package com.adisalagic.journal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

import static com.adisalagic.journal.MainActivity.getCustomers;

public class ClickListeners {
    static class Time implements View.OnClickListener {
        TextView timeOfVisit;
        Time(TextView t){
            timeOfVisit = t;
        }
        @Override
        public void onClick(View v) {
            TimePickerDialog dialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    TimeService timeService = new TimeService(hourOfDay, minute);
                    timeOfVisit.setText(timeService.toString(true));
                }
            }, 0, 0, true);
            dialog.show();
        }
    }
    static class DayOfVisit implements View.OnClickListener{
        TextView dayOfVisit;
        DayOfVisit(TextView d){
            dayOfVisit = d;
        }
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, dayOfMonth);
                    DateFormat format = DateFormat.getDateInstance();
                    format.setCalendar(calendar);
                    getCustomers().setsDayOfVisit(format.format(calendar.getTime()));
                    dayOfVisit.setText(Html.fromHtml("<u>" + format.format(calendar.getTime()) + "<u>", Html.FROM_HTML_MODE_COMPACT));
                }
            });
            datePickerDialog.show();
        }
    }
}
