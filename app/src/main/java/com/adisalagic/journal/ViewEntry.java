package com.adisalagic.journal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

import static com.adisalagic.journal.MainActivity.getCustomers;

public class ViewEntry extends AppCompatActivity {
    Customers customers;
    TextView  fullName, phoneNum, birthday, service, price, dayOfVisit, discount, extraInfo, other, timeOfVisit;
    Button delete, edit;
    View rootView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_view_entry, null);
        setContentView(rootView);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("customer");
        //DBClass dbClass = new DBClass(this);
        if (bundle != null) {
            customers = Customers.fromBundle(bundle);
            //customers = dbClass.getCustomer(dbClass.getWritableDatabase(), customers.getId());
        } else {
            Toast.makeText(this, "Записи не существует", Toast.LENGTH_SHORT).show();
            afterDelete(false);
        }
        fullName    = findViewById(R.id.full_nameV);
        phoneNum    = findViewById(R.id.phoneV);
        birthday    = findViewById(R.id.birthdayV);
        service     = findViewById(R.id.serviceV);
        price       = findViewById(R.id.priceV);
        dayOfVisit  = findViewById(R.id.day_of_visitV);
        discount    = findViewById(R.id.discountV);
        extraInfo   = findViewById(R.id.extra_infoV);
        other       = findViewById(R.id.other);
        timeOfVisit = findViewById(R.id.time_of_visitV);
        delete      = findViewById(R.id.delete);
        edit        = findViewById(R.id.edit);
//        customers.setFullName();
        fullName.setText(customers.getFullName());
        if (customers.getPhoneNum() == null) {
            customers.setPhoneNum("Не указан");
        }
        final int id      = bundle != null ? bundle.getInt("id_entry") : -1;
        DBClass   dbClass = new DBClass(this);
        final int humanId = dbClass.getHumanId(dbClass.getWritableDatabase(), customers.getFullName());
        Objects.requireNonNull(getSupportActionBar()).hide();
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
        s = "Краткое примечание: " + customers.getExtraInfo();
        extraInfo.setText(s);
        s = "Примечание: " + customers.getExtra();
        other.setText(s);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
                View                            view    = getLayoutInflater().inflate(R.layout.dialog_change_entry, null);

                final EditText service     = view.findViewById(R.id.t_service);
                final EditText price       = view.findViewById(R.id.t_price);
                final TextView dayOfVisit  = view.findViewById(R.id.t_day_of_visit);
                final TextView timeOfVisit = view.findViewById(R.id.t_time_of_visit);
                final EditText discount    = view.findViewById(R.id.t_discount);
                final EditText extraInfo   = view.findViewById(R.id.t_extra_info);
                final EditText other       = view.findViewById(R.id.t_other);
                Button         submit      = view.findViewById(R.id.submit);

                final DBClass   dbClass   = new DBClass(rootView.getContext());
                final Customers customers = dbClass.getCustomer(dbClass.getWritableDatabase(), id, humanId);
                service.setText(customers.getService());
                price.setText(String.valueOf(customers.getPrice()));
                timeOfVisit.setText(customers.getTimeOfVisit());
                discount.setText(String.valueOf(customers.getDiscount()));
                extraInfo.setText(customers.getExtraInfo());
                other.setText(customers.getExtra());
                dayOfVisit.setText(Html.fromHtml("<u>" + customers.getsDayOfVisit() + "<u>", Html.FROM_HTML_MODE_COMPACT));
                timeOfVisit.setText(Html.fromHtml("<u>" + timeOfVisit.getText().toString()
                        + "<u>", Html.FROM_HTML_MODE_COMPACT));
                dayOfVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
                        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, month, dayOfMonth);
                                DateFormat format = DateFormat.getDateInstance();
                                format.setCalendar(calendar);
                                getCustomers().setsDayOfVisit(format.format(calendar.getTime()));
                                dayOfVisit.setText(Html.fromHtml("<u>" + format.format(calendar.getTime()) + "<u>", Html.FROM_HTML_MODE_COMPACT));
                            }
                        });
                        datePickerDialog.show();
                    }
                });

                timeOfVisit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog dialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                TimeService timeService = new TimeService(hourOfDay, minute);
                                timeOfVisit.setText(timeService.toString(true));
                            }
                        }, 0, 0, true);
                        dialog.show();
                    }
                });


                builder.setView(view);
                final AlertDialog dialog2 = builder.create();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Customers updatedCustomer = new Customers();
                        updatedCustomer.setService(service.getText().toString());
                        int id_human = dbClass.getHumanId(dbClass.getWritableDatabase(), customers.getFullName());
                        updatedCustomer.setId(id_human);
                        updatedCustomer.setId_entry(customers.getId_entry());
                        if (price.getText().toString().isEmpty()) {
                            price.setText("0");
                        }
                        updatedCustomer.setPrice(price.getText().toString());
                        updatedCustomer.setsDayOfVisit(dayOfVisit.getText().toString());
                        updatedCustomer.setTimeOfVisit(timeOfVisit.getText().toString());
                        if (discount.getText().toString().isEmpty()) {
                            discount.setText(0);
                        }
                        updatedCustomer.setDiscount(Integer.parseInt(discount.getText().toString()));
                        updatedCustomer.setExtraInfo(extraInfo.getText().toString());
                        updatedCustomer.setExtra(other.getText().toString());
                        dbClass.changeEntry(dbClass.getWritableDatabase(), updatedCustomer.toContentValuesEntry(),
                                humanId, id);
                        updatedCustomer.setFullName(customers.getFullName());
                        updatedCustomer.setPhoneNum(customers.getPhoneNum());
                        dialog2.dismiss();
                        reloadText(updatedCustomer);
                    }
                });
                dialog2.show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBClass dbClass = new DBClass(v.getContext());
                if (dbClass.deleteEntry(dbClass.getWritableDatabase(), id)) {
                    Toast.makeText(v.getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
                    afterDelete(true);
                } else {
                    Toast.makeText(v.getContext(), "Не удалось удалить запись!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    void reloadText(Customers customers) {
        fullName.setText(customers.getFullName());
        if (customers.getPhoneNum() == null) {
            customers.setPhoneNum("Не указан");
        }
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
        s = "Краткое примечание: " + customers.getExtraInfo();
        extraInfo.setText(s);
        s = "Примечание: " + customers.getExtra();
        other.setText(s);
    }

    public void afterDelete(boolean Ok) {
        if (Ok) {
            onBackPressed();
            setResult(RESULT_OK);
            finish();
        } else {
            onBackPressed();
        }

    }
}
