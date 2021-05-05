package com.adisalagic.journal;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String ID           = "id";
    public static final String FULL_NAME    = "full_name";
    public static final String PHONE_NUMBER = "phone_num";
    public static final String BIRTHDAY     = "birthday";

    public static final String ID_PEOPLE     = "id_people";
    public static final String SERVICE       = "service";
    public static final String DAY_OF_VISIT  = "day_of_visit";
    public static final String TIME_OF_VISIT = "time_of_visit";
    public static final String TODAY         = "today";
    public static final String PRICE         = "price";
    public static final String DISCOUNT      = "discount";
    public static final String EXTRA         = "extra";
    public static final String EXTRA_INFO    = "extra_info";

    public static final String DB_NAME = "customers";


    private Context        context;
    private SQLiteDatabase database;


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 2);
        this.context = context;
        database = getWritableDatabase();
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        database = db;
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
        switch (oldVersion) {
            case 0:
            case 1:
                db.execSQL("create table Entry_dg_tmp( id INTEGER not null primary key autoincrement, id_people INTEGER not null references Customer on delete cascade,service TEXT(255),day_of_visit TEXT(255),time_of_visit TEXT(10),today TEXT(255),price TEXT,discount INTEGER,extra TEXT(255),extra_info TEXT);");
                db.execSQL("insert into Entry_dg_tmp(id, id_people, service, day_of_visit, time_of_visit, today, price, discount, extra, extra_info) select id, id_people, service, day_of_visit, time_of_visit, today, price, discount, extra, extra_info from Entry;");
                db.execSQL("drop table Entry;");
                db.execSQL("alter table Entry_dg_tmp rename to Entry;");
                break;
        }
    }


    public ArrayList<Customer> getAllCustomers() {
        String              sql       = "SELECT * FROM Customer ORDER BY full_name ASC";
        ArrayList<Customer> customers = new ArrayList<>();
        Cursor              cursor    = database.rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer     customer     = new Customer();
            CursorHelper cursorHelper = new CursorHelper(cursor);
            customer.setFullName(cursorHelper.getString(FULL_NAME));
            customer.setPhoneNumber(cursorHelper.getString(PHONE_NUMBER));
            customer.setBirthday(cursorHelper.getString(BIRTHDAY));
            customer.setId(cursorHelper.getInt(ID));
            customers.add(customer);
            cursor.moveToNext();
        }
        return customers;
    }

    public Customer getCustomer(int id) {
        String   sql      = "SELECT * FROM Customer WHERE id = ?1";
        Customer customer = new Customer();
        Cursor   cursor   = database.rawQuery(sql, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        CursorHelper cursorHelper = new CursorHelper(cursor);
        customer.setFullName(cursorHelper.getString(FULL_NAME));
        customer.setPhoneNumber(cursorHelper.getString(PHONE_NUMBER));
        customer.setBirthday(cursorHelper.getString(BIRTHDAY));
        customer.setId(cursorHelper.getInt(ID));
        cursor.close();
        return customer;
    }

    public boolean addNewCustomer(Customer customer) {
        long res = database.insert("Customer", null, customer.toContentValues());
        return res != 1;
    }


    public ArrayList<Entry> getEntries(int id) {
        ArrayList<Entry> entries      = new ArrayList<>();
        String           sql          = "SELECT * FROM Entry WHERE id_people = ?1";
        Cursor           cursor       = database.rawQuery(sql, new String[]{String.valueOf(id)});
        CursorHelper     cursorHelper = new CursorHelper(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry entry = new Entry();
            entry.setId(cursorHelper.getInt(ID));
            entry.setIdPeople(cursorHelper.getInt(ID_PEOPLE));
            entry.setDayOfVisit(cursorHelper.getString(DAY_OF_VISIT));
            entry.setTimeOfVisit(cursorHelper.getString(TIME_OF_VISIT));
            entry.setDiscount(cursorHelper.getInt(DISCOUNT));
            entry.setPrice(cursorHelper.getString(PRICE));
            entry.setService(cursorHelper.getString(SERVICE));
            entry.setToday(cursorHelper.getString(TODAY));
            entry.setExtraInfo(cursorHelper.getString(EXTRA_INFO));
            entry.setExtra(cursorHelper.getString(EXTRA));
            entries.add(entry);
            cursor.moveToNext();
        }
        return entries;
    }

    public Entry getEntry(int id, int idPeople) {
        String sql    = "SELECT * FROM Entry WHERE id_people = ?1 AND id = ?2";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(idPeople), String.valueOf(id)});
        cursor.moveToFirst();
        CursorHelper cursorHelper = new CursorHelper(cursor);
        Entry        entry        = new Entry();
        entry.setId(cursorHelper.getInt(ID));
        entry.setIdPeople(cursorHelper.getInt(ID_PEOPLE));
        entry.setDayOfVisit(cursorHelper.getString(DAY_OF_VISIT));
        entry.setTimeOfVisit(cursorHelper.getString(TIME_OF_VISIT));
        entry.setDiscount(cursorHelper.getInt(DISCOUNT));
        entry.setPrice(cursorHelper.getString(PRICE));
        entry.setService(cursorHelper.getString(SERVICE));
        entry.setToday(cursorHelper.getString(TODAY));
        entry.setExtraInfo(cursorHelper.getString(EXTRA_INFO));
        entry.setExtra(cursorHelper.getString(EXTRA));
        return entry;
    }

    public long addEntry(int id, Entry entry) {
        entry.setIdPeople(id);
        return database.insert("Entry", null, entry.toContentValues());
    }

    public Pair<Customer, Entry> getFullInfoOfEntry(int id, int idPeople) {
        return new Pair<>(getCustomer(idPeople), getEntry(id, idPeople));
    }

    public Entry getEntry(long rowID) {
        String sql    = "SELECT * FROM Entry WHERE rowID = ?1";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(rowID)});
        cursor.moveToFirst();
        CursorHelper cursorHelper = new CursorHelper(cursor);
        Entry        entry        = new Entry();
        entry.setId(cursorHelper.getInt(ID));
        entry.setIdPeople(cursorHelper.getInt(ID_PEOPLE));
        entry.setDayOfVisit(cursorHelper.getString(DAY_OF_VISIT));
        entry.setTimeOfVisit(cursorHelper.getString(TIME_OF_VISIT));
        entry.setDiscount(cursorHelper.getInt(DISCOUNT));
        entry.setPrice(cursorHelper.getString(PRICE));
        entry.setService(cursorHelper.getString(SERVICE));
        entry.setToday(cursorHelper.getString(TODAY));
        entry.setExtraInfo(cursorHelper.getString(EXTRA_INFO));
        entry.setExtra(cursorHelper.getString(EXTRA));
        return entry;
    }

    public ArrayList<Customer> searchCustomer(String request) {
        ArrayList<Customer> customers = new ArrayList<>();
        request = request.replaceAll("%", "");
        request = "%" + request + "%";
        String   sql    = "SELECT full_name, id, phone_num, birthday FROM Customer WHERE id LIKE ? OR full_name LIKE ? OR birthday LIKE ? OR phone_num LIKE ?";
        String[] args   = {request, request, request, request};
        Cursor   cursor = database.rawQuery(sql, args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer     customer     = new Customer();
            CursorHelper cursorHelper = new CursorHelper(cursor);
            customer.setFullName(cursorHelper.getString(FULL_NAME));
            customer.setPhoneNumber(cursorHelper.getString(PHONE_NUMBER));
            customer.setBirthday(cursorHelper.getString(BIRTHDAY));
            customer.setId(cursorHelper.getInt(ID));
            customers.add(customer);
            cursor.moveToNext();
        }
        return customers;
    }

    public ArrayList<Entry> searchEntry(String request, int idPeople) {
        ArrayList<Entry> entries = new ArrayList<>();
        request = request.replaceAll("%", "");
        request = "%" + request + "%";
        String[] args = {request, request, request, request, request, request, request, request};
        String sql = "SELECT * FROM Entry WHERE (id LIKE ? OR " +
                "service LIKE ? OR day_of_visit LIKE ? OR today LIKE ? OR price LIKE ? OR discount LIKE ? " +
                "OR extra LIKE ? OR extra_info LIKE ?) AND id_people IS " + idPeople;
        Cursor       cursor       = database.rawQuery(sql, args);
        CursorHelper cursorHelper = new CursorHelper(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Entry entry = new Entry();
            entry.setId(cursorHelper.getInt(ID));
            entry.setIdPeople(cursorHelper.getInt(ID_PEOPLE));
            entry.setDayOfVisit(cursorHelper.getString(DAY_OF_VISIT));
            entry.setTimeOfVisit(cursorHelper.getString(TIME_OF_VISIT));
            entry.setDiscount(cursorHelper.getInt(DISCOUNT));
            entry.setPrice(cursorHelper.getString(PRICE));
            entry.setService(cursorHelper.getString(SERVICE));
            entry.setToday(cursorHelper.getString(TODAY));
            entry.setExtraInfo(cursorHelper.getString(EXTRA_INFO));
            entry.setExtra(cursorHelper.getString(EXTRA));
            entries.add(entry);
            cursor.moveToNext();
        }
        return entries;
    }

    public void deleteEntry(int id) {
        database.delete("Entry", "id = ?1",
                new String[]{String.valueOf(id)});
    }

    public void editEntry(int id, Entry entry) {
        int i = database.update("Entry", entry.toContentValues(), "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteCustomer(int id) {
        database.delete("Customer", "id = ?1", new String[]{String.valueOf(id)});
    }

    public void editCustomer(int id, Customer customer) {
        database.update("Customer", customer.toContentValues(), "id = ?", new String[]{String.valueOf(id)});
    }

    public boolean backUpDB() {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        String         currentPath    = contextWrapper.getDatabasePath(DB_NAME).getAbsolutePath();
        String         backupPath     = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Customers/";
        File           file           = new File(backupPath);

        Log.i("DB", currentPath);
        Log.i("DB", backupPath);

        if (!file.exists()) {
            boolean d = file.mkdirs();
        }

        File cDB = new File(currentPath);
        File bDB = new File(file, cDB.getName());
        if (bDB.exists()) {
            boolean i = bDB.delete();
        }
        FileInputStream  cDBIS;
        FileOutputStream bDBOS;
        int              i = -1;

        try {
            cDBIS = new FileInputStream(cDB);
            bDBOS = new FileOutputStream(bDB);
            i = IOUtils.copy(cDBIS, bDBOS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean success = i > -1;
        Toast.makeText(context, success ? "Успех" : "Неудача", Toast.LENGTH_LONG).show();
        return success;
    }

    public boolean restoreDB() {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        String         currentPath    = contextWrapper.getDatabasePath(DB_NAME).getAbsolutePath();
        String         backupPath     = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Customers/";
        File           file           = new File(backupPath);

        if (!file.exists()) {
            return false;
        }

        File cDB = new File(currentPath);
        File bDB = new File(file, cDB.getName());

        FileInputStream  bDBOS;
        FileOutputStream cDBIS;
        int              i = -1;

        try {
            bDBOS = new FileInputStream(bDB);
            cDBIS = new FileOutputStream(cDB);
            i = IOUtils.copy(bDBOS, cDBIS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean success = i > -1;
        Toast.makeText(context, success ? "Успех" : "Неудача", Toast.LENGTH_LONG).show();
        return success;
    }

    private static class CursorHelper {
        Cursor cursor;

        CursorHelper(Cursor cursor) {
            this.cursor = cursor;
        }

        public int getInt(String name) {
            return cursor.getInt(cursor.getColumnIndex(name));
        }

        public double getDouble(String name) {
            return cursor.getDouble(cursor.getColumnIndex(name));
        }

        public long getLong(String name) {
            return cursor.getLong(cursor.getColumnIndex(name));
        }

        public String getString(String name) {
            return cursor.getString(cursor.getColumnIndex(name));
        }

        public short getShort(String name) {
            return cursor.getShort(cursor.getColumnIndex(name));
        }
    }

}
