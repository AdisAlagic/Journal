package com.adisalagic.journal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class CustomerActivity extends AppCompatActivity {

    private       TextView             mName;
    private       EditText             mSearch;
    private       RecyclerView         mList;
    private       FloatingActionButton mAddNew;
    private       ScrollUpButton       scrollUpButton;
    private       int                  idPeople;
    private final long                 duration = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        mName = findViewById(R.id.name);
        mSearch = findViewById(R.id.search);
        mList = findViewById(R.id.list);
        mAddNew = findViewById(R.id.add_new);
        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(this);
        mList.setLayoutManager(flexboxLayoutManager);
        mList.setItemAnimator(new SlideInLeftAnimator());
        DBHelper dbHelper = new DBHelper(this);
        Intent   intent   = getIntent();
        scrollUpButton = findViewById(R.id.scroll);
        scrollUpButton.animate().translationX(-300).setDuration(1);
        mName.setText(intent.getStringExtra("name"));
        idPeople = intent.getIntExtra("id", -1);

        EntryListAdapter adapter = getAdapter(idPeople, dbHelper);
        mList.setAdapter(adapter);
        mAddNew.setOnClickListener(v -> {
            DialogCreateEntry dialogCreateEntry = new DialogCreateEntry(this, false, entry -> {
                final long rowId = dbHelper.addEntry(idPeople, entry);
                entry = dbHelper.getEntry(rowId);
                ((EntryListAdapter)(mList.getAdapter())).addEntry(entry);
            });
            dialogCreateEntry.show();
        });
        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (flexboxLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    scrollUpButton.animate().translationX(-300).setDuration(duration - 200);
                }
                if (dy > 0) {
                    //Down
                    mAddNew.animate().translationX(300).setDuration(duration);
                    scrollUpButton.animate().translationX(0).setDuration(duration - 300);
                } else if (dy < 0) {
                    //Up
                    mAddNew.animate().translationX(0).setDuration(duration - 100);
                }
            }
        });
        scrollUpButton.setOnClickListener(v -> {
            final long duration = 400;
            mList.smoothScrollToPosition(0);
            scrollUpButton.animate().translationX(-300).setDuration(duration - 200);
            mAddNew.animate().translationX(0).setDuration(duration - 100);
        });

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Entry> entries = dbHelper.searchEntry(s.toString(), idPeople);
                adapter.changeArray(entries);
                mAddNew.animate().translationX(0).setDuration(duration - 100);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mList.setAdapter(getAdapter(idPeople, new DBHelper(this)));
    }

    private EntryListAdapter getAdapter(int idPeople, DBHelper dbHelper) {
        EntryListAdapter adapter = new EntryListAdapter(dbHelper.getEntries(idPeople));
        adapter.addChangeListener(new ChangeListener() {
            @Override
            public void onDeleteListener(int id) {
                super.onDeleteListener(id);
                dbHelper.deleteEntry(id);
                adapter.changeArray(dbHelper.getEntries(idPeople));
            }

            @Override
            public void onChangeListener(int id) {
                super.onChangeListener(id);
                DialogCreateEntry dialogCreateEntry = new DialogCreateEntry(CustomerActivity.this, true, entry -> {
                    dbHelper.editEntry(id, entry);
                    adapter.changeArray(dbHelper.getEntries(idPeople));
                });
                Entry entry = dbHelper.getEntry(id, idPeople);
                dialogCreateEntry.provideData(entry);
                dialogCreateEntry.show();
            }
        });
        return adapter;
    }
}