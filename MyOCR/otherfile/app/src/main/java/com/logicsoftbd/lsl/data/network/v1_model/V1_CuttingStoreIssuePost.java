package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_CuttingStoreIssuePost {
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("barcodes")
    @Expose
    private List<IssueBarcodeModelPost> barcodes;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<IssueBarcodeModelPost> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(List<IssueBarcodeModelPost> barcodes) {
        this.barcodes = barcodes;
    }
}
