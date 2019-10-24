package com.adisalagic.journal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ViewEntry extends AppCompatActivity {
    Customers customers;
    TextView  fullName, phoneNum, birthday, service, price, dayOfVisit, discount, extraInfo, other;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("customer");
        DBClass dbClass = new DBClass(this);
        if (bundle != null) {
            customers = Customers.fromBundle(bundle);
            customers = dbClass.getCustomer(dbClass.getWritableDatabase(), customers.getId());
        }
        fullName   = findViewById(R.id.full_nameV);
        phoneNum   = findViewById(R.id.phoneV);
        birthday   = findViewById(R.id.birthdayV);
        service    = findViewById(R.id.serviceV);
        price      = findViewById(R.id.priceV);
        dayOfVisit = findViewById(R.id.day_of_visitV);
        discount   = findViewById(R.id.discountV);
        extraInfo  = findViewById(R.id.extra_infoV);
        other      = findViewById(R.id.other);
        customers.setFullName();
        fullName.setText(customers.getFullName());
        String s = "Тел. " + customers.getPhoneNum();
        phoneNum.setText(s);
        s = "День рождения: " + customers.getBirthday();
        birthday.setText(s);
        s = "Услуга: " + customers.getService();
        service.setText(s);
        s = "Цена: " + customers.getPrice() + "₽";
        price.setText(s);
        s = "День посещения: " + customers.getsDayOfVisit();
        dayOfVisit.setText(s);
        s = "Скидка: " + customers.getDiscount() + "₽";
        discount.setText(s);
        s = "Примечание: " + customers.getExtraInfo();
        extraInfo.setText(s);
        s = "Другое: " + customers.getExtra();
        other.setText(s);
    }
}
