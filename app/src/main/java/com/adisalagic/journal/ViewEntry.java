package com.adisalagic.journal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class ViewEntry extends AppCompatActivity {
    Customers customers;
    TextView  fullName, phoneNum, birthday, service, price, dayOfVisit, discount, extraInfo, other, timeOfVisit;
    Button delete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("customer");
        //DBClass dbClass = new DBClass(this);
        if (bundle != null) {
            customers = Customers.fromBundle(bundle);
            //customers = dbClass.getCustomer(dbClass.getWritableDatabase(), customers.getId());
        }else {
            Toast.makeText(this, "Записи не существует", Toast.LENGTH_SHORT).show();
            afterDelete();
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
        delete     = findViewById(R.id.delete);
        timeOfVisit = findViewById(R.id.time_of_visitV);
//        customers.setFullName();
        fullName.setText(customers.getFullName());
        if (customers.getPhoneNum() == null) {
            customers.setPhoneNum("Не указан");
        }
        Objects.requireNonNull(getSupportActionBar()).hide();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Вы уверены, что хотите удалить запись?");
                builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBClass dbClass = new DBClass(v.getContext());
                        if (dbClass.deleteEntry(dbClass.getWritableDatabase(), customers.getId())) {
                            Toast.makeText(v.getContext(), "Запись удалена", Toast.LENGTH_SHORT).show();
                            afterDelete();
                        }else {
                            Toast.makeText(v.getContext(), "Ошибка при удалении записи", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
        s = "Время посещения: " + customers.getTimeOfVisit();
        timeOfVisit.setText(s);
        s = "Скидка: " + customers.getDiscount() + "₽";
        discount.setText(s);
        s = "Примечание: " + customers.getExtraInfo();
        extraInfo.setText(s);
        s = "Другое: " + customers.getExtra();
        other.setText(s);
    }

    public void afterDelete(){
        onBackPressed();
    }
}
