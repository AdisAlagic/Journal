package com.adisalagic.journal;

import android.content.ContentValues;

public class Customer {
    private int id;
    private String firstName;
    private String surName;
    private String patronymic;
    private String birthday;
    private String phoneNumber;


    public Customer(String firstName, String surName, String patronymic, String birthday, String phoneNumber) {
        this.firstName = firstName;
        this.surName = surName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
    }

    public Customer(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName(){
        String space = " ";
        return surName + space + firstName + space + patronymic;
    }

    public void setFullName(String name){
        String[] spl = name.split(" ");
        firstName = spl[1];
        if (spl.length > 1){
            surName = spl[0];
        }
        if (spl.length > 2){
            patronymic = spl[2];
        }
        if (firstName == null){
            firstName = "";
        }
        if (surName == null){
            surName = "";
        }
        if (patronymic == null){
            patronymic = "";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.FULL_NAME, getFullName());
        contentValues.put(DBHelper.BIRTHDAY, getBirthday());
        contentValues.put(DBHelper.PHONE_NUMBER, getPhoneNumber());
        return contentValues;
    }
}
