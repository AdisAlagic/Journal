package com.adisalagic.journal;

import android.content.ContentValues;
import android.os.Bundle;

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
    private int      price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setsDayOfVisit(int year, int month, int date, int hours, int minute) {
        day = Calendar.getInstance();
        day.set(year, month, date, hours, minute);
        day.setTimeZone(TimeZone.getTimeZone("UTС+03"));
    }

    public String getDayOfVisit() {
        day = Calendar.getInstance();
        sDayOfVisit = day.get(Calendar.DAY_OF_MONTH) + "." + day.get(Calendar.MONTH) + "." + day.get(Calendar.YEAR);
        return sDayOfVisit;
    }

    public String getsDayOfVisit(){
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

    public void setsToday(String sToday) {
        this.sToday = sToday;
    }

    public void setsDayOfVisit(String day) {
        sDayOfVisit = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void finalize() {
        fullName   = null;
        name       = null;
        surname    = null;
        patronymic = null;
        phoneNum   = null;
        price      = 0;
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
        setFullName();
        bundle.putString("name", getName());
        bundle.putString("surname", getSurname());
        bundle.putString("patronymic", getPatronymic());
        bundle.putString("phoneNum", getPhoneNum());
        bundle.putInt("price", getPrice());
        bundle.putString("birthday", getBirthday());
        bundle.putString("service", getService());
        bundle.putString("today", getTodayAsString());
        bundle.putInt("discount", getDiscount());
        bundle.putString("extraInfo", getExtraInfo());
        bundle.putString("extra", getExtra());
        bundle.putString("other", getExtra());
        bundle.putString("dayOfVisit", getsDayOfVisit());
        bundle.putString("fullname", fullName);
        return bundle;
    }

    public static Customers fromBundle(Bundle bundle) {
        Customers customers = new Customers();
        customers.setName(bundle.getString("name"));
        customers.setSurname(bundle.getString("surname"));
        customers.setPatronymic(bundle.getString("patronymic"));
        customers.setPhoneNum(bundle.getString("phoneNum"));
        customers.setPrice(bundle.getInt("price"));
        customers.setBirthday(bundle.getString("birthday"));
        customers.setService(bundle.getString("service"));
        customers.setsToday(bundle.getString("today"));
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
        contentValues.put("price", getPrice());
        contentValues.put("birthday", getBirthday());
        contentValues.put("service", getService());
        contentValues.put("today", getTodayAsString());
        contentValues.put("discount", getDiscount());
        contentValues.put("extra_info", getExtraInfo());
        contentValues.put("extra", getExtra());
        contentValues.put("day_of_visit", getsDayOfVisit());
        return contentValues;
    }
}
