package com.logicsoftbd.lsl.data.network.v1_model;
import java.io.Serializable;

public class V1_GreyRollReceiveItemModel implements Serializable {
    private String barcodeNo;
    private String rollNo;
    private String qnty;
    private String jobNo;
    private String bookingNo;
    private String programNo;
    private String internalRef;
    private String construction;
    private String composition;
    private String gsm;
    private String width;
    private String colorName;
    private String yarnLot;
    private String stitchLength;
    private String brandId;
    private String sysNumber;
    private String machineNo;
    private String companyId;
    private String delivery_number;
    private Boolean status;

    public String getBarcodeNo() {
        return barcodeNo;
    }

    public void setBarcodeNo(String barcodeNo) {
        this.barcodeNo = barcodeNo;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo;
    }

    public String getInternalRef() {
        return internalRef;
    }

    public void setInternalRef(String internalRef) {
        this.internalRef = internalRef;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getYarnLot() {
        return yarnLot;
    }

    public void setYarnLot(String yarnLot) {
        this.yarnLot = yarnLot;
    }

    public String getStitchLength() {
        return stitchLength;
    }

    public void setStitchLength(String stitchLength) {
        this.stitchLength = stitchLength;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getSysNumber() {return sysNumber;}

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getDelivery_number() {
        return delivery_number;
    }

    public void setDelivery_number(String delivery_number) {
        this.delivery_number = delivery_number;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
