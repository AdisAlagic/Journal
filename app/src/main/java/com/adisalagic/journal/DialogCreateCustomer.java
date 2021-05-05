package com.adisalagic.journal;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.pinball83.maskededittext.MaskedEditText;

import ss.anoop.awesometextinputlayout.AwesomeTextInputLayout;

public class DialogCreateCustomer {
    public interface DoneClickListener{
        void onDone(Customer customer);
    }

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Customer customer;
    private AwesomeTextInputLayout firstName;
    private AwesomeTextInputLayout surName;
    private AwesomeTextInputLayout patronymic;
    private AwesomeTextInputLayout birthday;
    private MaskedEditText         phone;

    DialogCreateCustomer(Context context, boolean forEdit, DoneClickListener doneClickListener) {
        builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_create_customer, null, false);
        builder.setView(view);
        Button mAdd = view.findViewById(R.id.apply);
        firstName = view.findViewById(R.id.firstname);
        surName = view.findViewById(R.id.surname);
        patronymic = view.findViewById(R.id.patronymic);
        birthday = view.findViewById(R.id.birthday);
        phone = view.findViewById(R.id.phone);
        getChildOfATIL(birthday).setOnClickListener(v -> {
            DateDialog dateDialog = new DateDialog(context, date -> {
                getChildOfATIL(birthday).setText(date);
            });
            dateDialog.show();
        });
        TextView error = view.findViewById(R.id.error);
        if (forEdit){
            mAdd.setText("Изменить");
        }
        mAdd.setOnClickListener(v -> {
            Customer customer = new Customer();
            try {
                customer.setFirstName(getTextOfATIL(firstName));
                customer.setSurName(getTextOfATIL(surName, false));
                customer.setPatronymic(getTextOfATIL(patronymic, false));
                customer.setBirthday(getTextOfATIL(birthday, false));
                customer.setPhoneNumber(phone.getUnmaskedText());
                doneClickListener.onDone(customer);
                dialog.dismiss();
            }catch (NullPointerException e){
                error.setVisibility(View.VISIBLE);
            }
        });
        dialog = builder.show();
    }

    public void provideCustomer(Customer customer){
        this.customer = customer;
        getChildOfATIL(firstName).setText(customer.getFirstName(), TextView.BufferType.EDITABLE);
        getChildOfATIL(surName).setText(customer.getSurName(), TextView.BufferType.EDITABLE);
        getChildOfATIL(patronymic).setText(customer.getPatronymic(), TextView.BufferType.EDITABLE);
        getChildOfATIL(birthday).setText(customer.getBirthday(), TextView.BufferType.EDITABLE);
        phone.setMaskedText(customer.getPhoneNumber());
    }

    private EditText getChildOfATIL(AwesomeTextInputLayout inputLayout){
        return (EditText) inputLayout.getChildAt(0);
    }

    public void show(){
        dialog.show();
    }

    public void hide(){
        dialog.dismiss();
    }

    private String getTextOfATIL(AwesomeTextInputLayout inputLayout){
        String text = getChildOfATIL(inputLayout).getText().toString();
        if (text.isEmpty()){
            throw new NullPointerException("Строка не должна быть пустой");
        }
        return text;
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
}
