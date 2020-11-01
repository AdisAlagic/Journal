package com.adisalagic.journal;

import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Customers {
    private String   fullName;
    private String   name;
    private String   surname;
    private String   patronymic;
    private String   phoneNum;
    private String   price;
    private String   birthday;
    private String   service;
    private Date     today;
    private Calendar day;
    private String   sToday;
    private int      discount;
    private String   extraInfo;
    private String   extra;
    private String   sDayOfVisit;
    private int      id;
    private int      id_entry;
    private String   timeOfVisit;

    Customers() {
        today = new Date();
        DateFormat dateFormat = DateFormat.getDateInstance();
        Calendar   calendar   = Calendar.getInstance();
        calendar.setTime(today);
        dateFormat.setCalendar(calendar);
        sToday = dateFormat.format(today);
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFullName() {
        this.fullName = surname + " " + name + " " + patronymic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setsDayOfVisit(int year, int month, int date, int hours, int minute) {
        day = Calendar.getInstance();
        day.set(year, month, date, hours, minute);
        day.setTimeZone(TimeZone.getTimeZone("UTС+03"));
    }

    public String getDayOfVisit() {
        day         = Calendar.getInstance();
        sDayOfVisit = day.get(Calendar.DAY_OF_MONTH) + "." + day.get(Calendar.MONTH) + "." + day.get(Calendar.YEAR);
        return sDayOfVisit;
    }

    public String getsDayOfVisit() {
        return sDayOfVisit;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getTodayAsString() {
        return sToday;
    }

    public void setToday(String sToday) {
        this.sToday = sToday;
    }

    public void setsDayOfVisit(String day) {
        sDayOfVisit = day;
    }

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public void setTimeOfVisit(String timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_entry() {
        return id_entry;
    }

    public void setId_entry(int id_entry) {
        this.id_entry = id_entry;
    }

    @Override
    public void finalize() {
        fullName   = null;
        name       = null;
        surname    = null;
        patronymic = null;
        phoneNum   = null;
        price      = null;
        birthday   = null;
        service    = null;
        today      = null;
        day        = null;
        sToday     = null;
        discount   = 0;
        extraInfo  = null;
        extra      = null;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", getId());
        bundle.putInt("id_entry", getId_entry());
        bundle.putString("name", getName());
        bundle.putString("surname", getSurname());
        bundle.putString("patronymic", getPatronymic());
        bundle.putString("fullName", getFullName());
        bundle.putString("phoneNum", getPhoneNum());
        bundle.putString("price", getPrice());
        bundle.putString("birthday", getBirthday());
        bundle.putString("service", getService());
        bundle.putString("today", getTodayAsString());
        bundle.putInt("discount", getDiscount());
        bundle.putString("extraInfo", getExtraInfo());
        bundle.putString("extra", getExtra());
        bundle.putString("time_of_visit", getTimeOfVisit());
        bundle.putString("other", getExtra());
        bundle.putString("dayOfVisit", getsDayOfVisit());
        bundle.putString("fullname", fullName);
        return bundle;
    }

    public static Customers fromBundle(Bundle bundle) {
        Customers customers = new Customers();
        customers.setId(bundle.getInt("id"));
        customers.setId_entry(bundle.getInt("id_entry"));
        customers.setName(bundle.getString("name"));
        customers.setSurname(bundle.getString("surname"));
        customers.setPatronymic(bundle.getString("patronymic"));
        customers.setPhoneNum(bundle.getString("phoneNum"));
        customers.setFullName(bundle.getString("fullName"));
        customers.setTimeOfVisit(bundle.getString("time_of_visit"));
        customers.setPrice(bundle.getString("price"));
        customers.setBirthday(bundle.getString("birthday"));
        customers.setService(bundle.getString("service"));
        customers.setToday(bundle.getString("today"));
        customers.setDiscount(bundle.getInt("discount"));
        customers.setExtraInfo(bundle.getString("extraInfo"));
        customers.setExtra(bundle.getString("other"));
        customers.setsDayOfVisit(bundle.getString("dayOfVisit"));
        return customers;
    }

    ContentValues toContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("full_name", getFullName());
        contentValues.put("phone_num", getPhoneNum());
        contentValues.put("birthday", getBirthday());
        return contentValues;
    }

    ContentValues toContentValuesEntry() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("price", getPrice());
        contentValues.put("service", getService());
        contentValues.put("today", getTodayAsString());
        contentValues.put("discount", getDiscount());
        contentValues.put("time_of_visit", getTimeOfVisit());
        contentValues.put("extra_info", getExtraInfo());
        contentValues.put("extra", getExtra());
        contentValues.put("id_people", getId());
        contentValues.put("day_of_visit", getsDayOfVisit());
        return contentValues;
    }

    public static String[] parseFullName(String fullName) {
        return fullName.split(" ");
    }

    void parseFullName() {
        String[] data = getFullName().split(" ");
        if (data.length == 3) {
            setName(data[0]);
            setSurname(data[1]);
            setPatronymic(data[2]);
        } else {
            Log.e("PARSING", "Error parsing full name");
        }

    }

    String getPhoneNumAsMuskedText() {
        return phoneNum
                .replace('+', '\0')
                .replace('7', '\0')
                .replace('(', '\0')
                .replace(')', '\0')
                .replace('-', '\0');
    }
}
