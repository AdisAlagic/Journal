package com.adisalagic.journal;

import android.content.ContentValues;

public class Entry {
    private int    id;
    private int    idPeople;
    private String service;
    private String dayOfVisit;
    private String timeOfVisit;
    private String today;
    private String price;
    private int    discount;
    /**
     * Краткое примечание
     */
    private String extra;
    /**
     * Длинное примечание
     */
    private String extraInfo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPeople() {
        return idPeople;
    }

    public void setIdPeople(int idPeople) {
        this.idPeople = idPeople;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDayOfVisit() {
        return dayOfVisit;
    }

    public void setDayOfVisit(String dayOfVisit) {
        this.dayOfVisit = dayOfVisit;
    }

    public String getTimeOfVisit() {
        return timeOfVisit;
    }

    public void setTimeOfVisit(String timeOfVisit) {
        this.timeOfVisit = timeOfVisit;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }

    public ContentValues toContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.ID_PEOPLE, idPeople);
        contentValues.put(DBHelper.SERVICE, service);
        contentValues.put(DBHelper.PRICE, price);
        contentValues.put(DBHelper.DAY_OF_VISIT, dayOfVisit);
        contentValues.put(DBHelper.TIME_OF_VISIT, timeOfVisit);
        contentValues.put(DBHelper.DISCOUNT, discount);
        contentValues.put(DBHelper.EXTRA, extra);
        contentValues.put(DBHelper.EXTRA_INFO, extraInfo);
        contentValues.put(DBHelper.TODAY, today);
        return contentValues;
    }
}
