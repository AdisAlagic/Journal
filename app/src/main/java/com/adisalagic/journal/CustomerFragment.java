package com.adisalagic.journal;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomerFragment extends Fragment {
    private TextView price, discount, dayOfVisit, service;
    private String sPrice, sDiscount, sDayOfVisit, sService;
    private int id, humanId;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(rootView.getContext(), ViewEntry.class);
                DBClass   dbClass   = new DBClass(rootView.getContext());
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
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View                view    = inflater.inflate(R.layout.dialog_change_customer, null);

                EditText service     = view.findViewById(R.id.t_service);
                EditText price       = view.findViewById(R.id.t_price);
                TextView dayOfVisit  = view.findViewById(R.id.t_day_of_visit);
                TextView timeOfVisit = view.findViewById(R.id.t_time_of_visit);
                EditText discount    = view.findViewById(R.id.t_discount);
                EditText extraInfo   = view.findViewById(R.id.t_extra_info);
                EditText other       = view.findViewById(R.id.t_other);
                Button   submit      = view.findViewById(R.id.submit);



            }
        });
        return rootView;
    }

    CustomerFragment(int price, int discount, String dayOfVisit, String service, int id, int humanId) {
        sPrice       = "Цена: " + price + "₽";
        sDiscount    = "Скидка: " + discount + "₽";
        sDayOfVisit  = dayOfVisit;
        sService     = service;
        this.id      = id;
        this.humanId = humanId;
    }
}
