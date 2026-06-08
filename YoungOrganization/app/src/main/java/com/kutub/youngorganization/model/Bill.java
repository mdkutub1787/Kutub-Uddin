package com.kutub.youngorganization.model;

public class Bill {
    public String id;        // Bill ID
    public String userId;    // User ID (owner of the bill)
    public String month;
    public String year;
    public String amount;

    public Bill() {

    }

    public Bill(String userId, String month, String year, String amount) {
        this.userId = userId;
        this.month = month;
        this.year = year;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}