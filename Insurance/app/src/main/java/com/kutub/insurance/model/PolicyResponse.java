package com.kutub.insurance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PolicyResponse {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("policyholder")
    @Expose
    private String policyholder;
    @SerializedName("bankname")
    @Expose
    private String bankname;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("sumInsured")
    @Expose
    private double sumInsured;
    @SerializedName("coverage")
    @Expose
    private String coverage;
    @SerializedName("construction")
    @Expose
    private String construction;
    @SerializedName("usedAs")
    @Expose
    private String usedAs;
    @SerializedName("periodFrom")
    @Expose
    private String periodFrom;
    @SerializedName("periodTo")
    @Expose
    private String periodTo;
    @SerializedName("id")
    @Expose
    private String id;
    private BillResponse billResponse;

    public BillResponse getBillResponse() {
        return billResponse;
    }

    public void setBillResponse(BillResponse billResponse) {
        this.billResponse = billResponse;
    }

    // Default constructor
    public PolicyResponse() {
    }

    public PolicyResponse(String date, String policyholder, String bankname, String address, double sumInsured, String coverage, String construction, String usedAs, String periodFrom, String periodTo, String id) {
        this.date = date;
        this.policyholder = policyholder;
        this.bankname = bankname;
        this.address = address;
        this.sumInsured = sumInsured;
        this.coverage = coverage;
        this.construction = construction;
        this.usedAs = usedAs;
        this.periodFrom = periodFrom;
        this.periodTo = periodTo;
        this.id = id;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPolicyholder() {
        return policyholder;
    }

    public void setPolicyholder(String policyholder) {
        this.policyholder = policyholder;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getSumInsured() {
        return sumInsured;
    }

    public void setSumInsured(double sumInsured) {
        this.sumInsured = sumInsured;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getUsedAs() {
        return usedAs;
    }

    public void setUsedAs(String usedAs) {
        this.usedAs = usedAs;
    }

    public String getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(String periodFrom) {
        this.periodFrom = periodFrom;
    }

    public String getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(String periodTo) {
        this.periodTo = periodTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Override toString for debugging
    @Override
    public String toString() {
        return "PolicyResponse{" +
                "date='" + date + '\'' +
                ", policyholder='" + policyholder + '\'' +
                ", bankname='" + bankname + '\'' +
                ", address='" + address + '\'' +
                ", sumInsured=" + sumInsured +
                ", coverage='" + coverage + '\'' +
                ", construction='" + construction + '\'' +
                ", usedAs='" + usedAs + '\'' +
                ", periodFrom='" + periodFrom + '\'' +
                ", periodTo='" + periodTo + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}