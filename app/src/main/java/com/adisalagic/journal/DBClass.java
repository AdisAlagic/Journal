package com.adisalagic.journal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;

public class DBClass extends SQLiteOpenHelper {
    private int ID;
    private int FULL_NAME;
    private int PHONE_NUM;
    private int PRICE;
    private int BIRTHDAY;
    private int SERVICE;
    private int DAY_OF_VISIT;
    private int TODAY;
    private int DISCOUNT;
    private int EXTRAINFO;
    private int EXTRA;

    public DBClass(Context context) {
        super(context, "customers", null, 1);
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE \"Customer\" (\n" +
                "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  \"full_name\" TEXT,\n" +
                "  \"phone_num\" text,\n" +
                "  \"price\" REAL,\n" +
                "  \"birthday\" TEXT,\n" +
                "  \"service\" TEXT,\n" +
                "  \"day_of_visit\" TEXT,\n" +
                "  \"today\" TEXT,\n" +
                "  \"discount\" TEXT,\n" +
                "  \"extra_info\" TEXT,\n" +
                "  \"extra\" TEXT\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void setTables(Cursor cursor){
        ID           = cursor.getColumnIndex("id");
        FULL_NAME    = cursor.getColumnIndex("full_name");
        PHONE_NUM    = cursor.getColumnIndex("phone_num");
        DAY_OF_VISIT = cursor.getColumnIndex("day_of_visit");
        PRICE        = cursor.getColumnIndexOrThrow("price"); // TEST IT
        BIRTHDAY     = cursor.getColumnIndex("birthday");
        SERVICE      = cursor.getColumnIndex("service");
        TODAY        = cursor.getColumnIndex("today");
        DISCOUNT     = cursor.getColumnIndex("discount");
        EXTRAINFO    = cursor.getColumnIndex("extra_info");
        EXTRA        = cursor.getColumnIndex("extra");
    }

    public void addCustomer(SQLiteDatabase db, Bundle customer) {
        Customers customers = Customers.fromBundle(customer);
        addCustomer(db, customers);
    }

    public long addCustomer(SQLiteDatabase db, Customers customer) {
        ContentValues contentValues = customer.toContentValues();
        return db.insert("Customer", null, contentValues);
    }

    public ArrayList<Customers> getFragmentCustomers(SQLiteDatabase db, int from, int to) {
        String               get       = "SELECT id, full_name, phone_num, day_of_visit, service FROM main.Customer WHERE id >= " + from + " and id <= " + to;
        ArrayList<Customers> customers = new ArrayList<>();
        Cursor               cursor    = db.rawQuery(get, null);
        cursor.moveToFirst();
        setTables(cursor);
        while (!cursor.isAfterLast()) {
            Customers customer = new Customers();
            customer.setId(cursor.getInt(ID));
            customer.setFullName(cursor.getString(FULL_NAME));
            customer.setPhoneNum(cursor.getString(PHONE_NUM));
            customer.setsDayOfVisit(cursor.getString(DAY_OF_VISIT));
            customer.setService(cursor.getString(SERVICE));
            customers.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    public Customers getCustomer(SQLiteDatabase db, int id) {
        Customers customer = new Customers();
        String    get      = "SELECT * FROM Customer WHERE id = " + id;
        Cursor    cursor   = db.rawQuery(get, null);
        setTables(cursor);
        while (!cursor.isAfterLast()) {
            customer.setId(id);
            customer.setService(cursor.getString(SERVICE));
            customer.setsDayOfVisit(cursor.getString(DAY_OF_VISIT));
            customer.setPhoneNum(cursor.getString(PHONE_NUM));
            customer.setsToday(cursor.getString(TODAY));
            customer.setExtra(cursor.getString(EXTRA));
            customer.setExtraInfo(cursor.getString(EXTRAINFO));
            customer.setBirthday(cursor.getString(BIRTHDAY));
            customer.setDiscount(cursor.getInt(DISCOUNT));
            customer.setPrice(cursor.getInt(PRICE));
            customer.setFullName(cursor.getString(FULL_NAME));
            cursor.moveToNext();
        }
        cursor.close();
        return customer;
    }
}
