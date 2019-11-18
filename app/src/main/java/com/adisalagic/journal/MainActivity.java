package com.adisalagic.journal;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {
    private LinearLayout         list;
    private EditText             searchBox;
    private FloatingActionButton fab;
    private EditText             name, surname, patronymic, phoneNum;
    private        TextView  birthday;
    private static Customers customers;

    public static Customers getCustomers() {
        return customers;
    }

    public void refreshEntries() {
        list.setId(R.id.listOfCustomers);
        FragmentTransaction  ft      = getSupportFragmentManager().beginTransaction();
        DBClass              dbClass = new DBClass(this);
        ArrayList<Customers> names   = dbClass.getNames(dbClass.getWritableDatabase());
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            ft.remove(getSupportFragmentManager().getFragments().get(i));
        }
        for (int i = 0; i < names.size(); i++) {
            NameCustomer nameCustomer = new NameCustomer(names.get(i).getFullName(), names.get(i).getId());
            ft.add(list.getId(), nameCustomer, null);
        }
        ft.commit();
    }

    public void refreshEntries(ArrayList<Customers> names) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < getSupportFragmentManager().getFragments().size(); i++) {
            ft.remove(getSupportFragmentManager().getFragments().get(i));
        }
        for (int i = 0; i < names.size(); i++) {
            NameCustomer nameCustomer = new NameCustomer(names.get(i).getFullName(), names.get(i).getId());
            ft.add(list.getId(), nameCustomer, null);
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
        final DBClass dbClass = new DBClass(this);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchBox.getText().toString().isEmpty()) {
                    refreshEntries();
                }
                refreshEntries(dbClass.searchCustomersByEverything(dbClass.getWritableDatabase(),
                        searchBox.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

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


        name       = dialogL.findViewById(R.id.t_name);
        surname    = dialogL.findViewById(R.id.t_surname);
        phoneNum   = dialogL.findViewById(R.id.t_phoneNum);
        patronymic = dialogL.findViewById(R.id.t_patronymic);
        birthday   = dialogL.findViewById(R.id.t_birthday);
        ok         = dialogL.findViewById(R.id.submit);


        birthday.setText(Html.fromHtml("<u>" + customers.getTodayAsString() + "<u>", Html.FROM_HTML_MODE_COMPACT));

        birthday.setTextColor(Color.BLACK);

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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        refreshEntries();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        refreshEntries();

    }
}
