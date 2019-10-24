package com.adisalagic.journal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomerFragment extends Fragment implements View.OnClickListener {
    private TextView name, phone, dayOfVisit, service;
    String sName, sPhone, sDayOfVisit, sService;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        setVars(rootView);
        return rootView;
    }

    private void setVars(View rootView) {
        name       = rootView.findViewById(R.id.name);
        service    = rootView.findViewById(R.id.service);
        phone      = rootView.findViewById(R.id.phoneNum);
        dayOfVisit = rootView.findViewById(R.id.day_of_visit);
        name.setText(sName);
        service.setText(sService);
        phone.setText(sPhone);
        dayOfVisit.setText(sDayOfVisit);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(v.getContext(), ViewEntry.class);
        String sName       = name.getText().toString();
        String sPhone      = phone.getText().toString();
        String sService    = service.getText().toString();
        String sDayOfVisit = dayOfVisit.getText().toString();
        Bundle bundle      = new Bundle();
        bundle.putString("name", sName);
        bundle.putString("phoneNum", sPhone);
        bundle.putString("dayOfVisit", sDayOfVisit);
        bundle.putString("service", sService);
        intent.putExtra("customer", bundle);
        startActivity(intent);
    }

    void setValues(String name, String phoneNum, String dayOfVisit, String service) {
        if (name == null || phone == null || dayOfVisit == null || service == null) {
            this.sName       = name;
            this.sPhone      = phoneNum;
            this.sDayOfVisit = "Запись на " + dayOfVisit;
            this.sService    = service;
        } else {
            this.name.setText(name);
            this.phone.setText(phoneNum);
            String s = "Запись на " + dayOfVisit;
            this.dayOfVisit.setText(s);
            this.service.setText(service);
        }
    }
}
