package com.kutub.insurance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillResponse {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("fire")
    @Expose
    private double fire;

    @SerializedName("rsd")
    @Expose
    private double rsd;

    @SerializedName("tax")
    @Expose
    private double tax;

    @SerializedName("netPremium")
    @Expose
    private double netPremium;

    @SerializedName("grossPremium")
    @Expose
    private double grossPremium;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getFire() {
        return fire;
    }

    public void setFire(double fire) {
        this.fire = fire;
    }

    public double getRsd() {
        return rsd;
    }

    public void setRsd(double rsd) {
        this.rsd = rsd;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getNetPremium() {
        return netPremium;
    }

    public void setNetPremium(double netPremium) {
        this.netPremium = netPremium;
    }

    public double getGrossPremium() {
        return grossPremium;
    }

    public void setGrossPremium(double grossPremium) {
        this.grossPremium = grossPremium;
    }

    @Override
    public String toString() {
        return "BillResponse{" +
                "id='" + id + '\'' +
                ", fire=" + fire +
                ", rsd=" + rsd +
                ", tax=" + tax +
                ", netPremium=" + netPremium +
                ", grossPremium=" + grossPremium +
                '}';
    }
}