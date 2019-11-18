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

public class CustomerFragment extends Fragment{
    private TextView price, discount, dayOfVisit, service;
    private String sPrice, sDiscount, sDayOfVisit, sService;
    private int id, humanId;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(rootView.getContext(), ViewEntry.class);
                DBClass dbClass = new DBClass(rootView.getContext());
                Customers customers = dbClass.getCustomer(dbClass.getWritableDatabase(), id, humanId);
                intent.putExtra("customer", customers.toBundle());
                startActivity(intent);
            }
        });
        price      = rootView.findViewById(R.id.price);
        discount   = rootView.findViewById(R.id.discount);
        dayOfVisit = rootView.findViewById(R.id.day_of_visit);
        service    = rootView.findViewById(R.id.service);
        price.setText(sPrice);
        discount.setText(sDiscount);
        dayOfVisit.setText(sDayOfVisit);
        service.setText(sService);

        return rootView;
    }

    CustomerFragment(int price, int discount, String dayOfVisit, String service, int id, int humanId) {
        sPrice      = "Цена: " + price + "₽";
        sDiscount   = "Скидка: " + discount + "₽";
        sDayOfVisit = dayOfVisit;
        sService    = service;
        this.id = id;
        this.humanId = humanId;
    }
}
