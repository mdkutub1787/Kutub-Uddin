package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_Consolitated_Order_Summery_Model {

    private int id;
    private String MONTH;
    private String COMPANY;
    private String CONFIRM;
    private String PROJECTION;
    private String CONFIRM_QTY;
    private String CONFIRM_AMOUNT;
    private String AVG;
    private String FORECAST;
    private String FORECAST_QTY;
    private String FORECAST_AMOUNT;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMONTH() {
        return MONTH;
    }

    public void setMONTH(String MONTH) {
        this.MONTH = MONTH;
    }

    public String getCOMPANY() {
        return COMPANY;
    }

    public void setCOMPANY(String COMPANY) {
        this.COMPANY = COMPANY;
    }

    public String getCONFIRM() {
        return CONFIRM;
    }

    public void setCONFIRM(String CONFIRM) {
        this.CONFIRM = CONFIRM;
    }

    public String getPROJECTION() {
        return PROJECTION;
    }

    public void setPROJECTION(String PROJECTION) {
        this.PROJECTION = PROJECTION;
    }

    public String getCONFIRM_QTY() {
        return CONFIRM_QTY;
    }

    public void setCONFIRM_QTY(String CONFIRM_QTY) {
        this.CONFIRM_QTY = CONFIRM_QTY;
    }

    public String getCONFIRM_AMOUNT() {
        return CONFIRM_AMOUNT;
    }

    public void setCONFIRM_AMOUNT(String CONFIRM_AMOUNT) {
        this.CONFIRM_AMOUNT = CONFIRM_AMOUNT;
    }

    public String getAVG() {
        return AVG;
    }

    public void setAVG(String AVG) {
        this.AVG = AVG;
    }

    public String getFORECAST() {
        return FORECAST;
    }

    public void setFORECAST(String FORECAST) {
        this.FORECAST = FORECAST;
    }

    public String getFORECAST_QTY() {
        return FORECAST_QTY;
    }

    public void setFORECAST_QTY(String FORECAST_QTY) {
        this.FORECAST_QTY = FORECAST_QTY;
    }

    public String getFORECAST_AMOUNT() {
        return FORECAST_AMOUNT;
    }

    public void setFORECAST_AMOUNT(String FORECAST_AMOUNT) {
        this.FORECAST_AMOUNT = FORECAST_AMOUNT;
    }


}
