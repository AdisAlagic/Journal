package com.adisalagic.journal;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.pinball83.maskededittext.MaskedEditText;

import java.text.DateFormat;
import java.util.Calendar;

public class NameCustomer extends Fragment {
    private TextView name;
    private String   sName;
    private int      id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    NameCustomer(String name, int id) {
        sName   = name;
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_customer_select, container, false);
        name = rootView.findViewById(R.id.name);
        name.setText(sName);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(v.getContext(), HumanEntries.class);
                Customers customers = new Customers();
                customers.setFullName(name.getText().toString());
                customers.setId(id); //Human id
                Bundle bundle = customers.toBundle();
                intent.putExtra("customer", bundle);
                startActivity(intent);
            }
        });
        name.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Что вы хотите сделать?");
                CharSequence[] answers = {"Редактировать", "Удалить", "Отмена"};
                builder.setPositiveButton(answers[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                        View                      view     = inflater.inflate(R.layout.dialog_change_customer, container, false);

                        Button               button;
                        final EditText       name, surname, patronymic;
                        final MaskedEditText phone;
                        final TextView       birthday;

                        button     = view.findViewById(R.id.change);
                        name       = view.findViewById(R.id.t_name);
                        surname    = view.findViewById(R.id.t_surname);
                        patronymic = view.findViewById(R.id.t_patronymic);
                        phone      = view.findViewById(R.id.t_phoneNum);
                        birthday   = view.findViewById(R.id.t_birthday);
                        String[] data = Customers.parseFullName(sName);
                        builder1.setView(view);
                        final AlertDialog dialog1 = builder1.create();
                        name.setText(data[1]);
                        surname.setText(data[0]);
                        patronymic.setText(data[2]);
                        final DBClass   dbClass   = new DBClass(v.getContext());
                        dbClass.backUpBD(v.getContext());
                        final Customers customers = dbClass.getCustomerNameById(dbClass.getWritableDatabase(), id);
                        birthday.setText(Html.fromHtml("<u>" + customers.getBirthday()
                                + "<u>", Html.FROM_HTML_MODE_COMPACT));
                        birthday.setTextColor(Color.BLACK);
                        phone.setMaskedText(customers.getPhoneNum());
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
                                        birthday.setTextColor(Color.BLACK);
                                        birthday.setText(Html.fromHtml("<u>" + format.format(calendar.getTime())
                                                + "<u>", Html.FROM_HTML_MODE_COMPACT));
                                    }
                                });
                                datePickerDialog.show();
                            }
                        });
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbClass.backUpBD(v.getContext());
                                customers.setBirthday(birthday.getText().toString());
                                customers.setId(id);
                                customers.setPhoneNum(phone.getUnmaskedText());
                                customers.setName(name.getText().toString());
                                customers.setSurname(surname.getText().toString());
                                customers.setPatronymic(patronymic.getText().toString());
                                customers.setFullName();
                                ContentValues res = customers.toContentValuesCustomer();
                                dbClass.changeName(dbClass.getWritableDatabase(), res, id);
                                dialog1.dismiss();
                                Toast.makeText(v.getContext(), "Успешно!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog1.show();
                    }
                });
                builder.setNegativeButton(answers[2], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNeutralButton(answers[1], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBClass dbClass = new DBClass(v.getContext());
                        dbClass.backUpBD(v.getContext());
                        if (dbClass.deleteCustomer(dbClass.getWritableDatabase(), id)) {
                            Toast.makeText(v.getContext(), "Удаление завершено!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
