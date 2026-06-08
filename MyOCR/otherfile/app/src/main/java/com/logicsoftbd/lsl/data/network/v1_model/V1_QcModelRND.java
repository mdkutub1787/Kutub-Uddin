package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_QcModelRND {
    private int id;
    private int qcItemNumber;
    private String qcItemName;
    private int qcDefectNumber;
    private int defectID;
    private int spinneritem;
    private double totalPoint;
    private double rejectQty;

    public V1_QcModelRND() {
    }

    public V1_QcModelRND(int qcItemNumber, String qcItemName, int qcDefectNumber, int defectID, int spinneritem, double totalPoint, double rejectQty) {
        this.qcItemNumber = qcItemNumber;
        this.qcItemName = qcItemName;
        this.qcDefectNumber = qcDefectNumber;
        this.defectID = defectID;
        this.spinneritem = spinneritem;
        this.totalPoint = totalPoint;
        this.rejectQty = rejectQty;
    }

    public V1_QcModelRND(int id, int qcItemNumber, String qcItemName, int qcDefectNumber, int defectID, int spinneritem, double totalPoint, double rejectQty) {
        this.id = id;
        this.qcItemNumber = qcItemNumber;
        this.qcItemName = qcItemName;
        this.qcDefectNumber = qcDefectNumber;
        this.defectID = defectID;
        this.spinneritem = spinneritem;
        this.totalPoint = totalPoint;
        this.rejectQty = rejectQty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQcItemNumber() {
        return qcItemNumber;
    }

    public void setQcItemNumber(int qcItemNumber) {
        this.qcItemNumber = qcItemNumber;
    }

    public String getQcItemName() {
        return qcItemName;
    }

    public void setQcItemName(String qcItemName) {
        this.qcItemName = qcItemName;
    }

    public int getQcDefectNumber() {
        return qcDefectNumber;
    }

    public void setQcDefectNumber(int qcDefectNumber) {
        this.qcDefectNumber = qcDefectNumber;
    }

    public int getDefectID() {
        return defectID;
    }

    public void setDefectID(int defectID) {
        this.defectID = defectID;
    }

    public int getSpinneritem() {
        return spinneritem;
    }

    public void setSpinneritem(int spinneritem) {
        this.spinneritem = spinneritem;
    }

    public double getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(double totalPoint) {
        this.totalPoint = totalPoint;
    }

    public double getRejectQty() {
        return rejectQty;
    }

    public void setRejectQty(double rejectQty) {
        this.rejectQty = rejectQty;
    }
}