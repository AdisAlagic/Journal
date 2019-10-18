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

public class CustomerFragment extends Fragment implements View.OnClickListener{
    private TextView name, phone, dayOfVisit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        name = rootView.findViewById(R.id.name);
        phone = rootView.findViewById(R.id.phoneNum);
        dayOfVisit = rootView.findViewById(R.id.day_of_visit);
        name.setText("Test");
        phone.setText("TEST");
        dayOfVisit.setText("19.01.2019");
        return rootView;
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(v.getContext(), ViewEntry.class);
        String sName = name.getText().toString();
        String sPhone = phone.getText().toString();
        String sDayOfVisit = dayOfVisit.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putString("name", sName);
        bundle.putString("phoneNum", sPhone);
        bundle.putString("dayOfVisit", sDayOfVisit);
        intent.putExtra("customer", bundle);
        startActivity(intent);
    }


}
