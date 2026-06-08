package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_RollWiseGrayDeliveryToStoreSaveResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("challan_no")
    @Expose
    private String challanNo;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
