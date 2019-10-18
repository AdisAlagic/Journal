package com.adisalagic.journal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private LinearLayout         list;
    private EditText             searchBox;
    private FloatingActionButton fab;
    private EditText             name, surname, patronymic, birthday, price, service, discount, dayOfVisit, extraInfo, other;
    private static Customers customers;

    public static Customers getCustomers() {
        return customers;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list      = findViewById(R.id.listOfCustomers);
        searchBox = findViewById(R.id.search);
        fab       = findViewById(R.id.floatingActionButton);

        //DEBUG
        list.setId(R.id.listOfCustomers);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < 10; i++) {
            Fragment fragment = new CustomerFragment();
            ft.add(list.getId(), fragment, null);
        }
        ft.commit();
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
        patronymic = dialogL.findViewById(R.id.t_patronymic);
        birthday   = dialogL.findViewById(R.id.t_birthday);
        price      = dialogL.findViewById(R.id.t_price);
        service    = dialogL.findViewById(R.id.t_service);
        discount   = dialogL.findViewById(R.id.t_discount);
        dayOfVisit = dialogL.findViewById(R.id.t_day_of_visit);
        extraInfo  = dialogL.findViewById(R.id.t_extra_info);
        other      = dialogL.findViewById(R.id.t_other);
        ok         = dialogL.findViewById(R.id.submit);

        birthday.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        customers.setBirthday(calendar.toString());
                    }
                });
                return true;
            }
        });

        dayOfVisit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext());
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, dayOfMonth);
                        customers.setBirthday(calendar.toString());
                    }
                });
                return true;
            }
        });

        builder.setView(dialogL);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void submit(View view) {
        // TODO: 17.10.2019 Add entry in db
        customers = new Customers();
        customers.setName(name.getText().toString());
        customers.setSurname(surname.getText().toString());
        customers.setPatronymic(patronymic.getText().toString());
        //customers.setBirthday(); fixme
        customers.setPrice(Integer.parseInt(price.getText().toString()));
        customers.setService(service.getText().toString());
        customers.setDiscount(Integer.parseInt(discount.getText().toString()));
        //customers.setsDayOfVisit(); fixme
        customers.setExtraInfo(extraInfo.getText().toString());
        customers.setExtra(other.getText().toString());

        DBClass        dbClass        = new DBClass(view.getContext());
        SQLiteDatabase sqLiteDatabase = dbClass.getReadableDatabase();
    }
}
