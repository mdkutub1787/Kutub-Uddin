package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_SewingInputPendingModel {
    private int id;
    private String PO_NUMBER;
    private String BARCODE_NO;
    private String BUNDLE_NO;
    private String CUT_NO;
    private String PRODUCTION_QNTY;
    private String JOB_NO;

    public String getJOB_NO() {
        return JOB_NO;
    }

    public void setJOB_NO(String JOB_NO) {
        this.JOB_NO = JOB_NO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPO_NUMBER() {
        return PO_NUMBER;
    }

    public void setPO_NUMBER(String PO_NUMBER) {
        this.PO_NUMBER = PO_NUMBER;
    }

    public String getBARCODE_NO() {
        return BARCODE_NO;
    }

    public void setBARCODE_NO(String BARCODE_NO) {
        this.BARCODE_NO = BARCODE_NO;
    }

    public String getBUNDLE_NO() {
        return BUNDLE_NO;
    }

    public void setBUNDLE_NO(String BUNDLE_NO) {
        this.BUNDLE_NO = BUNDLE_NO;
    }

    public String getCUT_NO() {
        return CUT_NO;
    }

    public void setCUT_NO(String CUT_NO) {
        this.CUT_NO = CUT_NO;
    }

    public String getPRODUCTION_QNTY() {
        return PRODUCTION_QNTY;
    }

    public void setPRODUCTION_QNTY(String PRODUCTION_QNTY) {
        this.PRODUCTION_QNTY = PRODUCTION_QNTY;
    }



}
