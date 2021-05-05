package com.adisalagic.journal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.ScaleInRightAnimator;

public class MainActivity extends AppCompatActivity {

    private EditText             mSearch;
    private CustomRecyclerView   mList;
    private FloatingActionButton mAddNew;
    private FloatingActionButton mDb;
    private DBHelper             dbHelper;
    private PeopleListAdapter    adapter;
    private ScrollUpButton       scrollUpButton;



    public void permission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSearch = findViewById(R.id.search);
        mList = findViewById(R.id.list);
        mAddNew = findViewById(R.id.add_new);
        scrollUpButton = findViewById(R.id.scroll);
        scrollUpButton.animate().translationX(-300).setDuration(1);
        mDb = findViewById(R.id.db);
        dbHelper = new DBHelper(this);
        adapter = new PeopleListAdapter(dbHelper.getAllCustomers(), new ChangeListener() {
            @Override
            public void onDeleteListener(int id) {
                super.onDeleteListener(id);
                dbHelper.deleteCustomer(id);
                adapter.changeArray(dbHelper.getAllCustomers());
            }

            @Override
            public void onChangeListener(int id) {
                super.onChangeListener(id);
                DialogCreateCustomer dialogCreateCustomer = new DialogCreateCustomer(MainActivity.this, true, customer -> {
                    dbHelper.editCustomer(id, customer);
                    adapter.changeArray(dbHelper.getAllCustomers());
                });
                dialogCreateCustomer.provideCustomer(dbHelper.getCustomer(id));
                dialogCreateCustomer.show();
            }
        });
        adapter.sort();
        mList.setAdapter(adapter);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        mList.setLayoutManager(flexboxLayoutManager);
        mList.setItemAnimator(new ScaleInRightAnimator());
        mList.setOnRvScrollListener(new CustomRecyclerView.OnScrollListener() {
            final long duration = 400;

            @Override
            protected void onGoUp() {

            }

            @Override
            protected void onGoDown() {

            }


            @Override
            void onScrollChanged(CustomRecyclerView RvView, int x, int y, int oldX, int oldY) {
                super.onScrollChanged(RvView, x, y, oldX, oldY);
                if (flexboxLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    scrollUpButton.animate().translationX(-300).setDuration(duration - 200);
                }
                if (oldY > 0) {
                    //Up
                    mAddNew.animate().translationX(0).setDuration(duration - 100);
                    mDb.animate().translationX(0).setDuration(duration - 100);
                } else if (oldY < 0) {
                    //Down
                    mAddNew.animate().translationX(300).setDuration(duration);
                    mDb.animate().translationX(300).setDuration(duration);
                    scrollUpButton.animate().translationX(0).setDuration(duration - 300);
                }
            }
        });


        initFloatingButtons();

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Customer> customers = dbHelper.searchCustomer(s.toString());
                adapter.changeArray(customers);
                animateFloating(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        permission();
    }


    private void initFloatingButtons() {
        mAddNew.setOnClickListener(v -> {
            DialogCreateCustomer dialogCreateCustomer = new DialogCreateCustomer(this, false, customer -> {
                dbHelper.addNewCustomer(customer);
                adapter.addCustomer(customer);

            });
            dialogCreateCustomer.show();
        });
        scrollUpButton.setOnClickListener(v -> {
            final long duration = 400;
            mList.smoothScrollToPosition(0);
            scrollUpButton.animate().translationX(-300).setDuration(duration - 200);
            mAddNew.animate().translationX(0).setDuration(duration - 100);
            mDb.animate().translationX(0).setDuration(duration - 100);
        });

        mDb.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setMessage("Что сделать с базой данных?")
                    .setPositiveButton("Сохранить", (dialog, which) -> {
                        dbHelper.backUpDB();
                    })
                    .setNeutralButton("Заменить", (dialog, which) -> {
                        dbHelper.restoreDB();
                        refresh();
                    })
                    .show();
        });
    }

    private void animateFloating(boolean hide) {
        final long duration = 400;
        if (hide) {
            mAddNew.animate().translationX(300).setDuration(duration);
            mDb.animate().translationX(300).setDuration(duration);
            scrollUpButton.animate().translationX(0).setDuration(duration - 300);
        } else {
            mAddNew.animate().translationX(0).setDuration(duration - 100);
            mDb.animate().translationX(0).setDuration(duration - 100);
        }
    }

    public void refresh(){
        adapter = new PeopleListAdapter(dbHelper.getAllCustomers(), new ChangeListener() {
            @Override
            public void onDeleteListener(int id) {
                super.onDeleteListener(id);
                dbHelper.deleteCustomer(id);
                adapter.changeArray(dbHelper.getAllCustomers());
            }

            @Override
            public void onChangeListener(int id) {
                super.onChangeListener(id);
                DialogCreateCustomer dialogCreateCustomer = new DialogCreateCustomer(MainActivity.this, true, customer -> {
                    dbHelper.editCustomer(id, customer);
                    adapter.changeArray(dbHelper.getAllCustomers());
                });
                dialogCreateCustomer.provideCustomer(dbHelper.getCustomer(id));
                dialogCreateCustomer.show();
            }
        });
        adapter.sort();
        mList.setAdapter(adapter);
    }
}