package com.adisalagic.journal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {

    private TextView mClient;
    private TextView mName;
    private TextView mPhone;
    private TextView mBirthday;
    private TextView mEntry;
    private View     mEntryLine;
    private TextView mService;
    private TextView mDayOfVisit;
    private TextView mTimeOfVisit;
    private TextView mToday;
    private TextView mPrice;
    private TextView mDiscount;
    private TextView mExtra;
    private TextView mExtraInfo;
    private TextView mEditCustomer;
    private TextView mEditEntry;

    private Entry    entry;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        mClient = findViewById(R.id.client);
        mName = findViewById(R.id.name);
        mPhone = findViewById(R.id.phone);
        mBirthday = findViewById(R.id.birthday);
        mEditCustomer = findViewById(R.id.edit_customer_button);
        mEditEntry = findViewById(R.id.edit_entry_button);
        mEntry = findViewById(R.id.entry);
        mEntryLine = findViewById(R.id.entry_line);
        mService = findViewById(R.id.service);
        mDayOfVisit = findViewById(R.id.day_of_visit);
        mTimeOfVisit = findViewById(R.id.time_of_visit);
        mToday = findViewById(R.id.today);
        mPrice = findViewById(R.id.price);
        mDiscount = findViewById(R.id.discount);
        mExtra = findViewById(R.id.extra);
        mExtraInfo = findViewById(R.id.extra_info);
        Intent   intent   = getIntent();
        int      id       = intent.getIntExtra("id", -1);
        int      idPeople = intent.getIntExtra("idPeople", -1);
        DBHelper dbHelper = new DBHelper(this);
        refreshData(id, idPeople, dbHelper);
        setUpText(customer, entry);
        mEditEntry.setOnClickListener(v -> {
            DialogCreateEntry dialogCreateEntry = new DialogCreateEntry(this, true, entry1 -> {
                dbHelper.editEntry(entry.getId(), entry1);
                refreshData(id, idPeople, dbHelper);
                setUpText(customer, entry);
            });
            dialogCreateEntry.provideData(entry);
            dialogCreateEntry.show();
        });

        mEditCustomer.setOnClickListener(v -> {
            DialogCreateCustomer dialogCreateCustomer = new DialogCreateCustomer(this, true, customer1 -> {
                dbHelper.editCustomer(customer.getId(), customer1);
                refreshData(id, idPeople, dbHelper);
                setUpText(customer, entry);
            });
            dialogCreateCustomer.provideCustomer(customer);
            dialogCreateCustomer.show();
        });
    }


    public String correctPhone(String phone) {
        String replacement = "+7 (***) ***-**-**";
        char[] chars       = phone.toCharArray();
        for (int i = 0; i < phone.length(); i++) {
            replacement = replacement.replaceFirst("\\*", String.valueOf(chars[i]));
        }
        return replacement;
    }

    public void setUpText(Customer customer, Entry entry) {
        mName.setText(customer.getFullName());
        mPhone.setText(getString(R.string.phone_view, correctPhone(customer.getPhoneNumber())));
        mBirthday.setText(getString(R.string.birthday_view, customer.getBirthday()));
//      ----------------------------------------------------------------------------------------
        mService.setText(getString(R.string.service_view, entry.getService()));
        mDayOfVisit.setText(getString(R.string.day_of_visit_view, entry.getDayOfVisit()));
        mTimeOfVisit.setText(getString(R.string.time_of_visit_view, entry.getTimeOfVisit()));
        mToday.setText(getString(R.string.today_view, entry.getToday()));
        mPrice.setText(getString(R.string.price_view, entry.getPrice()));
        mDiscount.setText(getString(R.string.discount_view, entry.getDiscount()));
        mExtra.setText(getString(R.string.extra_view, entry.getExtra()));
        mExtraInfo.setText(getString(R.string.extra_info_view, entry.getExtraInfo()));
        if (entry.getExtraInfo().isEmpty()) {
            mExtraInfo.setVisibility(View.GONE);
        }
        if (entry.getExtra().isEmpty()) {
            mExtra.setVisibility(View.GONE);
        }
    }

    public void refreshData(int id, int idPeople, DBHelper dbHelper) {
        Pair<Customer, Entry> pair = dbHelper.getFullInfoOfEntry(id, idPeople);
        customer = pair.first;
        entry = pair.second;
    }
}