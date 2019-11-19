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

    /**
     * Sets up table's columns
     *
     * @param cursor Needed for init
     */
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


    /**
     * Adds customer in database
     *
     * @param db       database
     * @param customer customer, that will be add
     * @return rowId of the new created row
     */
    public long addCustomer(SQLiteDatabase db, Customers customer) {
        ContentValues contentValues = customer.toContentValuesCustomer();
        return db.insertWithOnConflict("Customer", null, contentValues, SQLiteDatabase.CONFLICT_FAIL);
    }

    /**
     * Adds entry in database
     *
     * @param db        database
     * @param customers entry, that will be add
     * @return rowId of the new entry
     */
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

    /**
     * Gives customer
     * @param db database
     * @param id id of the entry
     * @param id_human id of the human
     * @return customer as <code>Customers</code>
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

    /**
     * Gives human id from name
     * @param db database
     * @param name Name of the customer
     * @return id
     */
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

    /**
     * Gives names for main_activity list
     * @param db database
     * @return <code>ArrayList</code> with names
     */
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

    /**
     * Gives entries for activity_human_entries
     * @param db database
     * @param id_human id of the customer
     * @return <code>ArrayList</code> with <code>Customers</code>
     */
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

    /**
     * Deletes a single Entry from DB
     * @param db database
     * @param id id of the Entry
     * @return <code>true</code> if ok, <code>false</code> otherwise
     */
    public boolean deleteEntry(SQLiteDatabase db, int id) {
        return db.delete("Entry", "id IS " + id, null) > 0;
    }

    /**
     * Deletes customer and every entry connected with customer
     * @param db database
     * @param id id of the customer
     * @return <code>true</code> if ok, <code>false</code> otherwise
     */
    public boolean deleteCustomer(SQLiteDatabase db, int id) {
        ArrayList<Customers> list = getEntries(db, id);
        for (int i = 0; i < list.size(); i++) {
            deleteEntry(db, list.get(i).getId_entry());
        }
        String[] args = {String.valueOf(id)};
        db.delete("Customer", "id IS ?", args);
        return true;
    }

    /**
     * Gives short info about customer
     * @param db database
     * @param id id of the customer
     * @return <code>Customers</code> with <code>Full Name, Phone Number and Birthday</code>
     */
    public Customers getCustomerNameById(SQLiteDatabase db, int id) {
        String    sql       = "SELECT * FROM Customer WHERE id IS ?";
        String[]  args      = {String.valueOf(id)};
        Cursor    cursor    = db.rawQuery(sql, args);
        Customers customers = new Customers();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customers.setFullName(cursor.getString(cursor.getColumnIndex("full_name")));
            customers.setPhoneNum(cursor.getString(cursor.getColumnIndex("phone_num")));
            customers.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    /**
     * Changes data about customer
     * @param db database
     * @param customer <code>Customers</code> as <code>ContentValues</code> for inserting in DB
     * @param id id of the Customer
     */
    public void changeName(SQLiteDatabase db, ContentValues customer, int id) {
        db.update("Customer", customer, "id IS " + id, null);
    }

    /**
     * Searches in database customers by everything
     * @param db database
     * @param request <code>string</code> with keyword search
     * @return <code>ArrayList</code> of <code>customers</code> as result of search
     */
    public ArrayList<Customers> searchCustomersByEverything(SQLiteDatabase db, String request) {
        ArrayList<Customers> customers = new ArrayList<>();
        request = "%" + request + "%";
        String   sql    = "SELECT full_name, id, phone_num, birthday FROM Customer WHERE id LIKE ? OR full_name LIKE ? OR birthday LIKE ? OR phone_num LIKE ?";
        String[] args   = {request, request, request, request};
        Cursor   cursor = db.rawQuery(sql, args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
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

    /**
     * Gives NOT NULL string from <code>Cursor</code>
     * @param column_name In what column we getting string
     * @param cursor What do we use to get string
     * @return NOT NULL <code>String</code>
     */
    private String getSaveString(String column_name, Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(column_name)) == null ? "" : cursor.getString(cursor.getColumnIndex(column_name));
    }

    public Customers getEntry(SQLiteDatabase database, int id){
        String sql = "SELECT * FROM Entry WHERE id IS " + id;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        Customers customers = new Customers();
        while (!cursor.isAfterLast()){
            customers.setService(getSaveString("service", cursor));
            customers.setPrice(cursor.getInt(cursor.getColumnIndex("price")));
            customers.setsDayOfVisit(getSaveString("day_of_visit", cursor));
            customers.setTimeOfVisit(getSaveString("time_of_visit", cursor));
            customers.setDiscount();
            cursor.moveToNext();
        }
        cursor.close();
    }
}
