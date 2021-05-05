package com.adisalagic.journal;

import android.app.AlertDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ss.anoop.awesometextinputlayout.AwesomeTextInputLayout;

public class DialogCreateEntry {

    private AlertDialog dialog;
    private DoneClickListener doneClickListener;
    private AwesomeTextInputLayout service;
    private AwesomeTextInputLayout price;
    private AwesomeTextInputLayout dayOfVisit;
    private AwesomeTextInputLayout timeOfVisit;
    private AwesomeTextInputLayout discount;
    private AwesomeTextInputLayout extra;
    private AwesomeTextInputLayout extraInfo;
    private Entry entry;

    public interface DoneClickListener{
        void onDone(Entry entry);
    }

    public DialogCreateEntry(Context context, boolean forEdit, DoneClickListener doneClickListener){
        this.doneClickListener = doneClickListener;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_create_entry, null, false);
        builder.setView(rootView);
        service = rootView.findViewById(R.id.service);
        price = rootView.findViewById(R.id.price);
        dayOfVisit = rootView.findViewById(R.id.day_of_visit);
        timeOfVisit = rootView.findViewById(R.id.time_of_visit);
        discount = rootView.findViewById(R.id.discount);
        extra = rootView.findViewById(R.id.extra);
        extraInfo = rootView.findViewById(R.id.extra_info);
        TextView error = rootView.findViewById(R.id.error);
        Button submit = rootView.findViewById(R.id.submit);
        if (forEdit){
            submit.setText("Изменить");
        }

        getChildOfATIL(dayOfVisit).setOnClickListener(v -> {
            DateDialog dateDialog = new DateDialog(context, date -> getChildOfATIL(dayOfVisit).setText(date));
            dateDialog.show();
        });

        getChildOfATIL(timeOfVisit).setOnClickListener(v -> {
            TimeDialog dialog = new TimeDialog(context, time -> getChildOfATIL(timeOfVisit).setText(time));
            dialog.show();
        });

        dialog = builder.create();
        submit.setOnClickListener(v -> {
            Entry entry = new Entry();
            try {
                entry.setToday(getToday());
                entry.setService(getTextOfATIL(service));
                entry.setExtra(getTextOfATIL(extra, false));
                entry.setPrice(getTextOfATIL(price));
                entry.setExtraInfo(getTextOfATIL(extraInfo, false));
                entry.setDiscount(Integer.parseInt(getTextOfATIL(discount)));
                entry.setTimeOfVisit(getTextOfATIL(timeOfVisit));
                entry.setDayOfVisit(getTextOfATIL(dayOfVisit));
                if (this.entry != null){
                    entry.setId(this.entry.getId());
                    entry.setIdPeople(this.entry.getIdPeople());
                }
                doneClickListener.onDone(entry);
                dialog.dismiss();
            }catch (Exception e){
                if (e instanceof NullPointerException && forEdit){
                    dialog.dismiss();
                    return;
                }
                error.setVisibility(View.VISIBLE);
            }
        });

    }

    public void setOnDoneClickListener(DoneClickListener doneClickListener){
        this.doneClickListener = doneClickListener;
    }

    private String getToday(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM y 'г.'", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return dateFormat.format(calendar.getTime());
    }


    private String getTextOfATIL(AwesomeTextInputLayout inputLayout, boolean needCheck){
        String text = getChildOfATIL(inputLayout).getText().toString();
        if (text.isEmpty()){
            if (needCheck){
                throw new NullPointerException("Строка не должна быть пустой");
            }
        }
        return text;
    }

    private String getTextOfATIL(AwesomeTextInputLayout inputLayout){
        return getTextOfATIL(inputLayout, true);
    }

    private EditText getChildOfATIL(AwesomeTextInputLayout inputLayout){
        return (EditText) inputLayout.getChildAt(0);
    }

    public void provideData(Entry entry){
        getChildOfATIL(service).setText(entry.getService());
        getChildOfATIL(price).setText(entry.getPrice());
        getChildOfATIL(dayOfVisit).setText(entry.getDayOfVisit());
        getChildOfATIL(timeOfVisit).setText(entry.getTimeOfVisit());
        getChildOfATIL(discount).setText(entry.getDiscount() + "");
        getChildOfATIL(extra).setText(entry.getExtra());
        getChildOfATIL(extraInfo).setText(entry.getExtraInfo());
        this.entry = entry;
    }

    public void show(){
        dialog.show();
    }
}
