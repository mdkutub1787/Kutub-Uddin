package com.kutub.billcreate.model;

public class Bill {
    private String billId;
    private String month;
    private Double amount;

    public Bill() {
    }

    public Bill(String billId, String month, Double amount) {
        this.billId = billId;
        this.month = month;
        this.amount = amount;
    }

    // Getters and Setters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}