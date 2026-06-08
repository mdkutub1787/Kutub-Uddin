package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_ApprovalUnApprovalDetailsModel {
    private String id;
    private String date;
    private String deliveryDate;
    private String company;
    private String buyer;
    private String sysNumber;
    private String sysDef;
    private String desc;
    private String isSeen;
    private Boolean isApproval;
    private Boolean isMarked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSysNumber() {
        return sysNumber;
    }

    public void setSysNumber(String sysNumber) {
        this.sysNumber = sysNumber;
    }

    public String getSysDef() {
        return sysDef;
    }

    public void setSysDef(String sysDef) {
        this.sysDef = sysDef;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getIsSeen() {
        return isSeen;
    }

    public void setIsSeen(String isSeen) {
        this.isSeen = isSeen;
    }

    public Boolean getIsApproval() {
        return isApproval;
    }

    public void setIsApproval(Boolean isApproval) {
        this.isApproval = isApproval;
    }

    public Boolean getMarked() {
        return isMarked;
    }

    public void setMarked(Boolean marked) {
        isMarked = marked;
    }
}
