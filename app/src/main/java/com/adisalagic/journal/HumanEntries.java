package com.adisalagic.journal;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.adisalagic.journal.MainActivity.getCustomers;

public class HumanEntries extends AppCompatActivity {
    FloatingActionButton fab;
    EditText             search, service, price, discount, extraInfo, extra;
    TextView dayOfVisit, timeOfVisit, mainName;
    LinearLayout list;
    Button       ok;
    Customers    customers;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fab      = findViewById(R.id.floatingActionButton2);
        search   = findViewById(R.id.search);
        list     = findViewById(R.id.lineral_layout_human);
        mainName = findViewById(R.id.main_name);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("customer");
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (bundle != null) {
            customers = Customers.fromBundle(bundle);
            mainName.setText(customers.getFullName());
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final View          view    = getLayoutInflater().inflate(R.layout.dialog_add_entry, null);

                service     = view.findViewById(R.id.t_service);
                price       = view.findViewById(R.id.t_price);
                discount    = view.findViewById(R.id.t_discount);
                extraInfo   = view.findViewById(R.id.t_extra_info);
                extra       = view.findViewById(R.id.t_other);
                dayOfVisit  = view.findViewById(R.id.t_day_of_visit);
                timeOfVisit = view.findViewById(R.id.t_time_of_visit);
                ok          = view.findViewById(R.id.submit);

                dayOfVisit.setText(Html.fromHtml("<u>" + customers.getTodayAsString() + "<u>", Html.FROM_HTML_MODE_COMPACT));
                timeOfVisit.setText(Html.fromHtml("<u>" + new TimeService().toString(true)
                        + "<u>", Html.FROM_HTML_MODE_COMPACT));


                dayOfVisit.setTextColor(Color.BLACK);
                timeOfVisit.setTextColor(Color.BLACK);
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
                final AlertDialog dialog = builder.create();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBClass dbClass = new DBClass(view.getContext());
                        Customers humans = new Customers();
                        if (price.getText().toString().isEmpty()){
                            price.setText("0");
                        }
                        if (discount.getText().toString().isEmpty()){
                            discount.setText("0");
                        }
                        int id_human = dbClass.getHumanId(dbClass.getWritableDatabase(), customers.getFullName());
                        humans.setService(service.getText().toString());
                        try {
                            humans.setPrice(Integer.parseInt(price.getText().toString()));
                            humans.setDiscount(Integer.parseInt(discount.getText().toString()));
                        }catch (Exception e){
                            Toast.makeText(v.getContext(), "Слишком большое число!", Toast.LENGTH_SHORT).show();
                        }
                        humans.setExtraInfo(extraInfo.getText().toString());
                        humans.setExtra(extra.getText().toString());
                        humans.setId_entry(customers.getId());
                        humans.setId(id_human);
                        humans.setsDayOfVisit(dayOfVisit.getText().toString());
                        humans.setTimeOfVisit(timeOfVisit.getText().toString());
                        long res = dbClass.addEntry(dbClass.getWritableDatabase(), humans);
                        dialog.dismiss();
                        refreshEntries(id_human);
                    }
                });
                dialog.show();
            }
        });
        DBClass dbClass = new DBClass(this);
        refreshEntries(dbClass.getHumanId(dbClass.getWritableDatabase(), customers.getFullName()));

    }

    public void refreshEntries(int id_human) {
        FragmentTransaction  ft        = getSupportFragmentManager().beginTransaction();
        DBClass              dbClass   = new DBClass(this);
        ArrayList<Customers> customers = dbClass.getEntries(dbClass.getWritableDatabase(), id_human);
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            ft.remove(getSupportFragmentManager().getFragments().get(i));
        }
        Collections.reverse(customers);
        for (Customers customer : customers) {
            CustomerFragment customerFragment = new CustomerFragment
                    (customer.getPrice(), customer.getDiscount(), customer.getsDayOfVisit(),
                            customer.getService(), customer.getId_entry(), customer.getId());
            ft.add(list.getId(), customerFragment, null);
        }
        ft.commit();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        DBClass dbClass = new DBClass(this);
        refreshEntries(dbClass.getHumanId(dbClass.getWritableDatabase(), customers.getFullName()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_entries);

    }
}
