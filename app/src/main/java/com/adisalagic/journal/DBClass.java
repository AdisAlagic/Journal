package com.adisalagic.journal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class DBClass extends SQLiteOpenHelper {
    private int ID;
    private int FULL_NAME;
    private int PHONE_NUM;
    private int PRICE;
    private int ID_HUMAN;
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
                "  \"full_name\" TEXT(255),\n" +
                "  \"phone_num\" TEXT(255),\n" +
                "  \"birthday\" TEXT(255)\n" +
                ");");
        db.execSQL("CREATE TABLE \"Entry\" (\n" +
                "  \"id\" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  \"id_people\" INTEGER NOT NULL,\n" +
                "  \"service\" TEXT(255),\n" +
                "  \"day_of_visit\" TEXT(255),\n" +
                "  \"time_of_visit\" TEXT(10),\n" +
                "  \"today\" TEXT(255),\n" +
                "  \"price\" INTEGER,\n" +
                "  \"discount\" INTEGER,\n" +
                "  \"extra\" TEXT(255),\n" +
                "  \"extra_info\" TEXT,\n" +
                "  FOREIGN KEY (\"id_people\") REFERENCES \"Customer\" (\"id\") ON DELETE CASCADE ON UPDATE NO ACTION\n" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void setEntryTable(Cursor cursor) {
        ID           = cursor.getColumnIndex("id");
        SERVICE      = cursor.getColumnIndex("service");
        DAY_OF_VISIT = cursor.getColumnIndex("day_of_visit");
        PRICE        = cursor.getColumnIndex("price");
        DISCOUNT     = cursor.getColumnIndex("discount");
    }

    private void setHumanTable(Cursor cursor) {
        ID        = cursor.getColumnIndex("id");
        FULL_NAME = cursor.getColumnIndex("full_name");
    }

    private void setTables(Cursor cursor) {
        ID           = cursor.getColumnIndex("id");
        ID_HUMAN     = cursor.getColumnIndex("id_people");
        FULL_NAME    = cursor.getColumnIndex("full_name");
        PHONE_NUM    = cursor.getColumnIndex("phone_num");
        DAY_OF_VISIT = cursor.getColumnIndex("day_of_visit");
        PRICE        = cursor.getColumnIndex("price"); // TEST IT
        BIRTHDAY     = cursor.getColumnIndex("birthday");
        SERVICE      = cursor.getColumnIndex("service");
        TODAY        = cursor.getColumnIndex("today");
        DISCOUNT     = cursor.getColumnIndex("discount");
        EXTRAINFO    = cursor.getColumnIndex("extra_info");
        EXTRA        = cursor.getColumnIndex("extra");
    }

    public long addCustomer(SQLiteDatabase db, Bundle customer) {
        Customers customers = Customers.fromBundle(customer);
        return addCustomer(db, customers);
    }

    public long addCustomer(SQLiteDatabase db, Customers customer) {
        ContentValues contentValues = customer.toContentValuesCustomer();
        return db.insertWithOnConflict("Customer", null, contentValues, SQLiteDatabase.CONFLICT_FAIL);
    }

    public long addEntry(SQLiteDatabase db, Customers customers) {
        ContentValues contentValues = customers.toContentValuesEntry();
        return db.insertWithOnConflict("Entry", null, contentValues, SQLiteDatabase.CONFLICT_FAIL);
    }

    public int getIdFromRowID(SQLiteDatabase db, long id) {
        String get    = "SELECT id FROM Entry WHERE rowid IS " + id;
        Cursor cursor = db.rawQuery(get, null);
        cursor.moveToFirst();
        int res = -1;
        while (!cursor.isAfterLast()) {
            res = cursor.getInt(cursor.getColumnIndex("id"));
        }
        return res;
    }

    public int getId_peopleFromRowID(SQLiteDatabase db, long id) {
        String get    = "SELECT id_people FROM Entry WHERE rowid IS " + id;
        Cursor cursor = db.rawQuery(get, null);
        cursor.moveToFirst();
        int res = -1;
        while (!cursor.isAfterLast()) {
            res = cursor.getInt(cursor.getColumnIndex("id"));
        }
        return res;
    }

    public ArrayList<Customers> getFragmentCustomers(SQLiteDatabase db) {
        String               get       = "SELECT id, service, day_of_visit, price, discount FROM Entry";
        ArrayList<Customers> customers = new ArrayList<>();
        Cursor               cursor    = db.rawQuery(get, null);
        cursor.moveToFirst();
        setEntryTable(cursor);
        while (!cursor.isAfterLast()) {
            Customers customer = new Customers();
            customer.setId(cursor.getInt(ID));
            customer.setService(cursor.getString(SERVICE));
            customer.setPrice(cursor.getInt(PRICE));
            customer.setDiscount(cursor.getInt(DISCOUNT));
            customers.add(customer);
            customers.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    // TODO: 24.10.2019 Redo data base
/*
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
    */
    public Customers getCustomer(SQLiteDatabase db, int id, int id_human) {
        Customers customer = new Customers();
        String get = "SELECT full_name, phone_num, birthday, Entry.id as " +
                "id_entry, service, day_of_visit, time_of_visit, price, discount, extra, extra_info" +
                " FROM Customer JOIN Entry ON Customer.id == Entry.id_people WHERE Entry.id IS " + id + " AND id_people IS " + id_human;
        Cursor cursor = db.rawQuery(get, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customer.setId(cursor.getInt(cursor.getColumnIndex("id_entry")));
            customer.setFullName(cursor.getString(cursor.getColumnIndex("full_name")));
            customer.setPhoneNum(cursor.getString(cursor.getColumnIndex("phone_num")));
            customer.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
            customer.setService(cursor.getString(cursor.getColumnIndex("service")));
            customer.setsDayOfVisit(cursor.getString(cursor.getColumnIndex("day_of_visit")));
            customer.setTimeOfVisit(cursor.getString(cursor.getColumnIndex("time_of_visit"))); // FIXME: 07.11.2019 
            customer.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
            customer.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
            customer.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
            customer.setExtraInfo(cursor.getString(cursor.getColumnIndex("extra_info")));
            cursor.moveToNext();
        }
        cursor.close();
        return customer;
    }

    public int getHumanId(SQLiteDatabase db, String name) {
        String   get    = "SELECT id FROM Customer WHERE full_name IS ?";
        String[] args   = {name};
        Cursor   cursor = db.rawQuery(get, args);
        int      res    = -1;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int j = cursor.getColumnIndex("id");
            res = cursor.getInt(j);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    public ArrayList<Customers> getNames(SQLiteDatabase db) {
        String               get    = "SELECT full_name, id FROM Customer ORDER BY \"full_name\";";
        Cursor               cursor = db.rawQuery(get, null);
        ArrayList<Customers> names  = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int       column   = cursor.getColumnIndex("full_name");
            String    name     = cursor.getString(column);
            Customers customer = new Customers();
            customer.setFullName(name);
            customer.setId(cursor.getInt(cursor.getColumnIndex("id")));
            names.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    public ArrayList<Customers> getEntries(SQLiteDatabase db, int id_human) {
        String get    = "SELECT * FROM Entry WHERE id_people IS " + id_human;
        Cursor cursor = db.rawQuery(get, null);
        cursor.moveToFirst();
        ArrayList<Customers> entries = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            Customers customers = new Customers();
            int       i         = cursor.getColumnIndex("id_people");
            customers.setId(cursor.getInt(cursor.getColumnIndex("id_people")));
            customers.setId_entry(cursor.getInt(cursor.getColumnIndex("id")));
            customers.setService(cursor.getString(cursor.getColumnIndex("service")));
            customers.setsDayOfVisit(cursor.getString(cursor.getColumnIndex("day_of_visit")));
            customers.setTimeOfVisit(cursor.getString(cursor.getColumnIndex("time_of_visit")));
            customers.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
            customers.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
            customers.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
            customers.setExtraInfo(cursor.getString(cursor.getColumnIndex("extra_info")));
            entries.add(customers);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public boolean deleteEntry(SQLiteDatabase db, int id) {
        return db.delete("Entry", "id IS " + id, null) > 0;
    }

    public boolean deleteCustomer(SQLiteDatabase db, int id) {
        ArrayList<Customers> list = getEntries(db, id);
        for (int i = 0; i < list.size(); i++){
            deleteEntry(db, list.get(i).getId_entry());
        }
        String[] args = {String.valueOf(id)};
        db.delete("Customer", "id IS ?", args);
        return true;
    }

    public Customers getCustomerNameById(SQLiteDatabase db, int id){
        String sql = "SELECT * FROM Customer WHERE id IS ?";
        String[] args = {String.valueOf(id)};
        Cursor cursor = db.rawQuery(sql, args);
        Customers customers = new Customers();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            customers.setFullName(cursor.getString(cursor.getColumnIndex("full_name")));
            customers.setPhoneNum(cursor.getString(cursor.getColumnIndex("phone_num")));
            customers.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    public void changeName(SQLiteDatabase db, ContentValues customer, int id){
        db.update("Customer", customer, "id IS " + id, null);
    }

    public ArrayList<Customers> searchCustomersByEverything(SQLiteDatabase db, String request){
        ArrayList<Customers> customers = new ArrayList<>();
        request = "%" + request + "%";
        String sql = "SELECT full_name, id, phone_num, birthday FROM Customer WHERE id LIKE ? OR full_name LIKE ? OR birthday LIKE ? OR phone_num LIKE ?";
        String[] args = {request, request, request, request};
        Cursor cursor = db.rawQuery(sql, args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Customers customer = new Customers();
            customer.setId(cursor.getInt(cursor.getColumnIndex("id")));
            customer.setFullName(getSaveString("full_name", cursor));
            customer.setPhoneNum(getSaveString("phone_num", cursor));
            customer.setBirthday(getSaveString("birthday", cursor));
            customers.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    private String getSaveString(String table_name, Cursor cursor){
        return cursor.getString(cursor.getColumnIndex(table_name)) == null ? "" : cursor.getString(cursor.getColumnIndex(table_name));
    }
}
