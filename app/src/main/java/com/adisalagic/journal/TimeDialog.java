package com.adisalagic.journal;

import android.app.TimePickerDialog;
import android.content.Context;

import java.text.SimpleDateFormat;

public class TimeDialog {

    public interface DoneListener{
        void onDone(String time);
    }
    private TimePickerDialog dialog;

    TimeDialog(Context context, DoneListener listener){
        dialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            TimeService timeService = new TimeService(hourOfDay, minute);
            String s = timeService.toString(true);
            listener.onDone(s);
        }, 15, 45, true);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
