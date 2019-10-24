package com.adisalagic.journal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private LinearLayout         list;
    private EditText             searchBox;
    private FloatingActionButton fab;
    private EditText             name, surname, patronymic, price, service, discount, extraInfo, other, phoneNum;
    private TextView birthday, dayOfVisit, timeOfVisit;
    private static Customers customers;

    public static Customers getCustomers() {
        return customers;
    }

    public void refreshEntries() {
        //DEBUG
        list.setId(R.id.listOfCustomers);

        FragmentTransaction  ft        = getSupportFragmentManager().beginTransaction();
        DBClass              dbClass   = new DBClass(this);
        ArrayList<Customers> customers = dbClass.getFragmentCustomers(dbClass.getReadableDatabase(), 0, 10);
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++){
            ft.remove(getSupportFragmentManager().getFragments().get(i));
        }
        for (int i = 0; i < 10; i++) {
            CustomerFragment fragment = new CustomerFragment();
            if (!customers.isEmpty()) {
                if (customers.size() <= i) {
                    break;
                }
                Customers customer = customers.get(i);

                fragment.setValues(customer.getFullName(), customer.getPhoneNum(),
                        customer.getsDayOfVisit(), customer.getService());
                ft.add(list.getId(), fragment, null);

            }
        }
        ft.commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list      = findViewById(R.id.listOfCustomers);
        searchBox = findViewById(R.id.search);
        fab       = findViewById(R.id.floatingActionButton);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        searchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    return onEnterPressed();
                }
                return false;
            }
        });
        customers = new Customers();
        refreshEntries();
    }

    private boolean onEnterPressed() {
        return false;
    }

    public void newEntryCreate(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final View          dialogL = getLayoutInflater().inflate(R.layout.dialog_add_customer, null);

        Button ok;


        name        = dialogL.findViewById(R.id.t_name);
        surname     = dialogL.findViewById(R.id.t_surname);
        patronymic  = dialogL.findViewById(R.id.t_patronymic);
        birthday    = dialogL.findViewById(R.id.t_birthday);
        price       = dialogL.findViewById(R.id.t_price);
        service     = dialogL.findViewById(R.id.t_service);
        discount    = dialogL.findViewById(R.id.t_discount);
        dayOfVisit  = dialogL.findViewById(R.id.t_day_of_visit);
        extraInfo   = dialogL.findViewById(R.id.t_extra_info);
        other       = dialogL.findViewById(R.id.t_other);
        timeOfVisit = dialogL.findViewById(R.id.t_time_of_visit);
        ok          = dialogL.findViewById(R.id.submit);
        phoneNum    = dialogL.findViewById(R.id.t_phoneNum);


        birthday.setText(Html.fromHtml("<u>" + customers.getTodayAsString() + "<u>", Html.FROM_HTML_MODE_COMPACT));
        dayOfVisit.setText(Html.fromHtml("<u>" + customers.getTodayAsString() + "<u>", Html.FROM_HTML_MODE_COMPACT));
        timeOfVisit.setText(Html.fromHtml("<u>" + new TimeService().toString(true)
                + "<u>", Html.FROM_HTML_MODE_COMPACT));

        birthday.setTextColor(Color.BLACK);
        dayOfVisit.setTextColor(Color.BLACK);
        timeOfVisit.setTextColor(Color.BLACK);

        birthday.setOnClickListener(new View.OnClickListener() {
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
                        getCustomers().setBirthday(format.format(calendar.getTime()));
                        birthday.setText(Html.fromHtml("<u>" + format.format(calendar.getTime())
                                + "<u>", Html.FROM_HTML_MODE_COMPACT));
                    }
                });
                datePickerDialog.show();
            }
        });

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


        builder.setView(dialogL);

        final AlertDialog dialog = builder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit(v)) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    public boolean submit(View view) {
        customers = new Customers();
        customers.setName(name.getText().toString());
        customers.setSurname(surname.getText().toString());
        customers.setPatronymic(patronymic.getText().toString());
        customers.setPhoneNum(phoneNum.getText().toString());
        customers.setFullName();
        customers.setBirthday(birthday.getText().toString());
        try {
            customers.setPrice(Integer.parseInt(price.getText().toString()));
            customers.setDiscount(Integer.parseInt(discount.getText().toString()));
        } catch (Exception e) {
            Toast.makeText(view.getContext(), "Проверьте правильность ввода данных", Toast.LENGTH_LONG).show();
            customers.finalize();
            return false;
        }
        customers.setService(service.getText().toString());
        customers.setsDayOfVisit(dayOfVisit.getText().toString());
        customers.setExtraInfo(extraInfo.getText().toString());
        customers.setExtra(other.getText().toString());

        DBClass        dbClass        = new DBClass(view.getContext());
        SQLiteDatabase sqLiteDatabase = dbClass.getReadableDatabase();

        if (dbClass.addCustomer(sqLiteDatabase, customers) != -1) {
            refreshEntries();
            return true;
        } else {
            Toast.makeText(view.getContext(), "Произошла ошибка при добавлении записи", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
