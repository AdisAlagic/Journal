package com.adisalagic.journal;

import android.app.DatePickerDialog;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateDialog {
    public interface DoneListener{
        void onDone(String date);
    }
    Context context;
    DatePickerDialog datePickerDialog;
    DateDialog(Context context, DoneListener doneListener){
        this.context = context;
        datePickerDialog = new DatePickerDialog(context);
        datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            DateFormat dateFormat = new SimpleDateFormat("dd MMM y 'г.'", Locale.getDefault());
            doneListener.onDone(dateFormat.format(calendar.getTime()));
        });
    }

    public void show(){
        datePickerDialog.show();
    }

    public void dismiss(){
        datePickerDialog.dismiss();
    }
}
