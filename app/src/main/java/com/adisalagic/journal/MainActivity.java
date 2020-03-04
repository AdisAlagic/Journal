package com.adisalagic.journal;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private        LinearLayout         list;
    private        EditText             searchBox;
    private        FloatingActionButton fab;
    private        EditText             name;
    private        EditText             surname;
    private        EditText             patronymic;
    private        MaskedEditText       phoneNum;
    private        TextView             birthday;
    private static Customers            customers;

    public static Customers getCustomers() {
        return customers;
    }


    public void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    new DBClass(this).backUpBD(this);
                    break;
                }
            default:
                Toast.makeText(this,
                        "Нужно разрешение на использования памяти для резервного копирования базы данных",
                        Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Refreshes entries in main_activity
     */
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

    /**
     * Refreshes entries while searching
     *
     * @param names Search results
     */
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
        fab       = findViewById(R.id.restore);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Snackbar.make(v, "К сожалению, сделать резевную копию БД нельзя", Snackbar.LENGTH_LONG)
                        .setAction("Восстановить БД", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DBClass dbClass = new DBClass(v.getContext());
                                dbClass.restore();
                            }
                        });
//                final DBClass       dbClass = new DBClass(v.getContext());
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setMessage("Вы хотите восстановить базу данных?");
//                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dbClass.restore();
//                        Toast.makeText(v.getContext(), "Успешно!", Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNeutralButton("Хочу сохранить!", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (dbClass.backUpBD()){
//                            Toast.makeText(v.getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(v.getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
//
//                refreshEntries();
//                dbClass.close();
            }
        });
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final DBClass dbClass = new DBClass(this);
        dbClass.getWritableDatabase().needUpgrade(2);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchBox.getText().toString().isEmpty()) {
                    refreshEntries();
                } else {
                    refreshEntries(dbClass.searchCustomersByEverything(dbClass.getWritableDatabase(),
                            searchBox.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        customers = new Customers();
        refreshEntries();
    }

    /**
     * Creates new AlertDialog for adding new customer
     *
     * @param view Needed to init AlertDialog
     */
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
                if (name.getText().toString().toLowerCase().equals("jojo")) {
                    throw new JoJoReferenceException();
                }
                if (submit(v)) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();

    }

    /**
     * Submits a new customer into the database
     *
     * @param view Needed for database init
     * @return <code>true</code> if OK
     */
    public boolean submit(View view) {
        customers = new Customers();
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, "Имя не может быть пустым!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (surname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Фамилия не может быть пустой!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (patronymic.getText().toString().isEmpty()) {
            Toast.makeText(this, "Отчество не может быть пустым!", Toast.LENGTH_SHORT).show();
            return false;
        }
        customers.setName(name.getText().toString());
        customers.setSurname(surname.getText().toString());
        customers.setPatronymic(patronymic.getText().toString());
        customers.setPhoneNum(phoneNum.getUnmaskedText());
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
    protected void onStart() {
        super.onStart();
        askPermission();
    }

//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        refreshEntries();
//    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            refreshEntries();
        }
    }
}
