package com.adisalagic.journal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.util.Calendar;

import static com.adisalagic.journal.MainActivity.getCustomers;

public class CustomerFragment extends Fragment {
    private TextView price, discount, dayOfVisit, service;
    private String sPrice, sDiscount, sDayOfVisit, sService, sRowPrice, sRowDiscount;
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
        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View                view    = inflater.inflate(R.layout.dialog_change_entry, null);

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
                final AlertDialog dialog = builder.create();
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
                        updatedCustomer.setPrice(Integer.parseInt(price.getText().toString()));
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
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
        return rootView;
    }

    CustomerFragment(int price, int discount, String dayOfVisit, String service, int id, int humanId) {
        sPrice       = "Цена: " + price + "₽";
        sRowPrice    = String.valueOf(price);
        sDiscount    = "Скидка: " + discount + "₽";
        sRowDiscount = String.valueOf(discount);
        sDayOfVisit  = dayOfVisit;
        sService     = service;
        this.id      = id;
        this.humanId = humanId;
    }
}
