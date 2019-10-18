package com.adisalagic.journal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewEntry extends AppCompatActivity {
    Customers customers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("customer");
        if (bundle != null) {
            customers = Customers.fromBundle(bundle);
        }
        //customers.setsDayOfVisit();
    }
}
