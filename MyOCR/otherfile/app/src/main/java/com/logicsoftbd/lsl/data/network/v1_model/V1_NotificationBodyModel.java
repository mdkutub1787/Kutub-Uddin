package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_NotificationBodyModel {
    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("DATE")
    @Expose
    private String date;
    @SerializedName("DELIVERY_DATE")
    @Expose
    private String deliveryDate;
    @SerializedName("COMPANY")
    @Expose
    private String company;
    @SerializedName("BUYER")
    @Expose
    private String buyer;
    @SerializedName("SYS_NUMBER")
    @Expose
    private String sysNumber;
    @SerializedName("SYS_DEF")
    @Expose
    private String sysDef;
    @SerializedName("DESC")
    @Expose
    private String desc;
    @SerializedName("MENU_ID")
    @Expose
    private String menuId;
    @SerializedName("UN_APPROVED")
    @Expose
    private Boolean UN_APPROVED;
    @SerializedName("UN_APPROVED_MSG")
    @Expose
    private String UN_APPROVED_MSG;


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



    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public Boolean getUN_APPROVED() {
        return UN_APPROVED;
    }

    public void setUN_APPROVED(Boolean UN_APPROVED) {
        this.UN_APPROVED = UN_APPROVED;
    }

    public String getUN_APPROVED_MSG() {
        return UN_APPROVED_MSG;
    }

    public void setUN_APPROVED_MSG(String UN_APPROVED_MSG) {
        this.UN_APPROVED_MSG = UN_APPROVED_MSG;
    }
}