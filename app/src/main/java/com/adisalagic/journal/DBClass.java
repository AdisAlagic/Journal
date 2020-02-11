package com.adisalagic.journal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class DBClass extends SQLiteOpenHelper {
    private int ID;
    private int PRICE;
    private int SERVICE;
    private int DISCOUNT;


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
        switch (oldVersion) {
            case 1:
                db.execSQL("create table Entry_dg_tmp( id INTEGER not null primary key autoincrement, id_people INTEGER not null references Customer on delete cascade,service TEXT(255),day_of_visit TEXT(255),time_of_visit TEXT(10),today TEXT(255),price TEXT,discount INTEGER,extra TEXT(255),extra_info TEXT);");
                db.execSQL("insert into Entry_dg_tmp(id, id_people, service, day_of_visit, time_of_visit, today, price, discount, extra, extra_info) select id, id_people, service, day_of_visit, time_of_visit, today, price, discount, extra, extra_info from Entry;");
                db.execSQL("drop table Entry;");
                db.execSQL("alter table Entry_dg_tmp rename to Entry;");

                break;
        }
    }

    /**
     * Sets up table's columns
     *
     * @param cursor Needed for an init
     */
    private void setEntryTable(Cursor cursor) {
        ID       = cursor.getColumnIndex("id");
        SERVICE  = cursor.getColumnIndex("service");
        PRICE    = cursor.getColumnIndex("price");
        DISCOUNT = cursor.getColumnIndex("discount");
    }


    /**
     * Adds customer in the database
     *
     * @param db       database
     * @param customer customer, that will be add
     * @return rowId of the new created row
     */
    long addCustomer(SQLiteDatabase db, Customers customer) {
        ContentValues contentValues = customer.toContentValuesCustomer();
        return db.insertWithOnConflict("Customer", null, contentValues, SQLiteDatabase.CONFLICT_FAIL);
    }

    /**
     * Adds entry in the database
     *
     * @param db        database
     * @param customers entry, that will be add
     * @return rowId of the new entry
     */
    long addEntry(SQLiteDatabase db, Customers customers) {
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
        cursor.close();
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
        cursor.close();
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
            customer.setPrice(cursor.getString(PRICE));
            customer.setDiscount(cursor.getInt(DISCOUNT));
            customers.add(customer);
            customers.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    /**
     * Gives the customer
     *
     * @param db       database
     * @param id       id of the entry
     * @param id_human id of the human
     * @return customer as <code>Customers</code>
     */
    Customers getCustomer(SQLiteDatabase db, int id, int id_human) {
        Customers customer = new Customers();
        String get = "SELECT full_name, phone_num, birthday, Entry.id as " +
                "id_entry, service, day_of_visit, time_of_visit, price, discount, extra, extra_info" +
                " FROM Customer JOIN Entry ON Customer.id == Entry.id_people WHERE Entry.id IS " + id + " AND id_people IS " + id_human;
        Cursor cursor = db.rawQuery(get, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customer.setId_entry(cursor.getInt(cursor.getColumnIndex("id_entry")));
            customer.setFullName(cursor.getString(cursor.getColumnIndex("full_name")));
            customer.setPhoneNum(cursor.getString(cursor.getColumnIndex("phone_num")));
            customer.setBirthday(cursor.getString(cursor.getColumnIndex("birthday")));
            customer.setService(cursor.getString(cursor.getColumnIndex("service")));
            customer.setsDayOfVisit(cursor.getString(cursor.getColumnIndex("day_of_visit")));
            customer.setTimeOfVisit(cursor.getString(cursor.getColumnIndex("time_of_visit")));
            customer.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            customer.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
            customer.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
            customer.setExtraInfo(cursor.getString(cursor.getColumnIndex("extra_info")));
            cursor.moveToNext();
        }
        cursor.close();
        return customer;
    }

    /**
     * Gives the human id from the name
     *
     * @param db   database
     * @param name Name of the customer
     * @return id
     */
    int getHumanId(SQLiteDatabase db, String name) {
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
     * Give names for main_activity list
     *
     * @param db database
     * @return <code>ArrayList</code> with names
     */
    ArrayList<Customers> getNames(SQLiteDatabase db) {
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
     *
     * @param db       database
     * @param id_human id of the customer
     * @return <code>ArrayList</code> with <code>Customers</code>
     */
    ArrayList<Customers> getEntries(SQLiteDatabase db, int id_human) {
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
            customers.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            customers.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
            customers.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
            customers.setExtraInfo(cursor.getString(cursor.getColumnIndex("extra_info")));
            entries.add(customers);
            cursor.moveToNext();
        }
        cursor.close();
        return entries;
    }

    public ArrayList<Customers> getEntries(SQLiteDatabase database, int id_human, int from) {
        ArrayList<Customers> customers = getEntries(database, id_human);
        ArrayList<Customers> res       = new ArrayList<>();
        for (int i = from; i < i + 10; i++) {
            res.set(i, customers.get(i));
        }
        return res;
    }

    /**
     * Deletes a single Entry from DB
     *
     * @param db database
     * @param id id of the Entry
     * @return <code>true</code> if ok, <code>false</code> otherwise
     */
    public boolean deleteEntry(SQLiteDatabase db, int id) {
        return db.delete("Entry", "id IS " + id, null) > 0;
    }

    /**
     * Deletes customer and every entry connected with customer
     *
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
     *
     * @param db database
     * @param id id of the customer
     * @return <code>Customers</code> with <code>Full Name, Phone Number and Birthday</code>
     */
    Customers getCustomerNameById(SQLiteDatabase db, int id) {
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
     * Changes data about the customer
     *
     * @param db       database
     * @param customer <code>Customers</code> as <code>ContentValues</code> for inserting in DB
     * @param id       id of the Customer
     */
    public void changeName(SQLiteDatabase db, ContentValues customer, int id) {
        db.update("Customer", customer, "id IS " + id, null);
    }

    /**
     * Searches in database customers by everything
     *
     * @param db      database
     * @param request <code>string</code> with keyword search
     * @return <code>ArrayList</code> of <code>customers</code> as result of search
     */
    ArrayList<Customers> searchCustomersByEverything(SQLiteDatabase db, String request) {
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
     *
     * @param column_name Specifies the column of string we getting
     * @param cursor      Specifies the cursor
     * @return NOT NULL <code>String</code>
     */
    private String getSaveString(String column_name, Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(column_name)) == null ? "" : cursor.getString(cursor.getColumnIndex(column_name));
    }

    /**
     * Gives full entry
     *
     * @param database database
     * @param id       id of the entry
     * @return <code>Customers</code>
     */
    public Customers getEntry(SQLiteDatabase database, int id) {
        String sql    = "SELECT * FROM Entry WHERE id IS " + id;
        Cursor cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        Customers customers = new Customers();
        while (!cursor.isAfterLast()) {
            customers.setService(getSaveString("service", cursor));
            customers.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            customers.setsDayOfVisit(getSaveString("day_of_visit", cursor));
            customers.setTimeOfVisit(getSaveString("time_of_visit", cursor));
            customers.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
            customers.setToday(getSaveString("today", cursor));
            customers.setExtra(getSaveString("extra", cursor));
            customers.setExtraInfo(getSaveString("extra_info", cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    public ArrayList<Customers> searchEntryByEverything(SQLiteDatabase db, String request, int idPeople) {
        String sql = "SELECT price, discount, day_of_visit, service FROM Entry WHERE (id LIKE ? OR " +
                "service LIKE ? OR day_of_visit LIKE ? OR today LIKE ? OR price LIKE ? OR discount LIKE ?" +
                "OR extra LIKE ? OR extra_info LIKE ?) AND id_people IS " + idPeople;
        request = "%" + request + "%";
        ArrayList<Customers> customers = new ArrayList<>();
        String[]             args      = {request, request, request, request, request, request, request, request};
        Cursor               cursor    = db.rawQuery(sql, args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customers customer = new Customers();
            customer.setPrice(cursor.getString(cursor.getColumnIndex("price")));
            customer.setDiscount(cursor.getInt(cursor.getColumnIndex("discount")));
            customer.setsDayOfVisit(getSaveString("day_of_visit", cursor));
            customer.setService(getSaveString("service", cursor));
            customers.add(customer);
            cursor.moveToNext();
        }
        cursor.close();
        return customers;
    }

    public void changeEntry(SQLiteDatabase db, ContentValues contentValues, int id_human, int id_entry) {
        db.update("Entry", contentValues, "id IS " + id_entry + " AND id_people IS " + id_human, null);
    }

    public boolean backUpBD(Context context) {
        try {
            File sd   = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = this.getWritableDatabase().getPath();
                String backupDBPath  = "customers";
                File   currentDB     = new File(currentDBPath);
                File   backupDB      = new File(sd, backupDBPath);
                if (!backupDB.exists()) {
                    backupDB.createNewFile();
                }

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                } else {
                    Toast.makeText(context, "Ошибка копирования текущей БД", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "Не удалось скопировать Базу Данных!", Toast.LENGTH_LONG).show();
        }
        return true;
//        Toast.makeText(context, "База данных успешно скопирована!", Toast.LENGTH_SHORT).show();
    }

    public void restore(Context context) {
        try {
            File sd   = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = this.getWritableDatabase().getPath();
                String backupDBPath  = "customers";
                File   currentDB     = new File(currentDBPath);
                File   backupDB      = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Toast.makeText(context, "База данных успешно скопирована!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            //ignored
        }
    }

//    public void backUpDB(Activity activity) {
//        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission_group.STORAGE)){
//
//            }else {
//                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission_group.STORAGE},
//                        1);
//            }
//        }
//
//        File    file    = new File(Environment.getExternalStorageDirectory() + File.separator + activity.getResources().getString(R.string.app_name) + "/customersb");
//        File    dbFile  = new File(getWritableDatabase().getPath());
//        boolean success = true;
//        if (!file.exists()) {
//            success = file.mkdirs();
//        }
//        OutputStream    stream          = null;
//        FileInputStream fileInputStream = null;
//        try {
//            file.createNewFile();
//            stream          = new FileOutputStream(file);
//            fileInputStream = new FileInputStream(dbFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (success) {
//            byte[] buffer = new byte[1024];
//            int    length;
//            try {
//                assert fileInputStream != null;
//                while ((length = fileInputStream.read(buffer)) > 0) {
//                    stream.write(buffer, 0, length);
//                }
//                stream.flush();
//                stream.close();
//                fileInputStream.close();
//            } catch (Exception e) {
//                //ignored
//            }
//
//        }
//    }
}
