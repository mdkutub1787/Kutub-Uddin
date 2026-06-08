package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ActiveDeviceModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private List<Resultset> resultset;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Resultset> getResultset() {
        return resultset;
    }

    public void setResultset(List<Resultset> resultset) {
        this.resultset = resultset;
    }

    public class Resultset {

        @SerializedName("ROWID")
        @Expose
        private String rowid;
        @SerializedName("FLOOR_NAME")
        @Expose
        private String floorName;
        @SerializedName("LINE_NUMBER")
        @Expose
        private String lineNumber;
        @SerializedName("DEVICE_ID")
        @Expose
        private String deviceId;
        @SerializedName("ITEM_NAME")
        @Expose
        private String itemName;
        @SerializedName("PO_NUMBER")
        @Expose
        private String poNumber;
        @SerializedName("JOB_NO")
        @Expose
        private String jobNo;
        @SerializedName("INTERNAL_REF")
        @Expose
        private String internalRef;
        @SerializedName("USER_ID")
        @Expose
        private String USER_ID;
        @SerializedName("USER_NAME")
        @Expose
        private String USER_NAME;

        public String getUSER_ID() {
            return USER_ID;
        }

        public void setUSER_ID(String USER_ID) {
            this.USER_ID = USER_ID;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getUSER_FULL_NAME() {
            return USER_FULL_NAME;
        }

        public void setUSER_FULL_NAME(String USER_FULL_NAME) {
            this.USER_FULL_NAME = USER_FULL_NAME;
        }

        @SerializedName("USER_FULL_NAME")
        @Expose
        private String USER_FULL_NAME;

        public String getRowid() {
            return rowid;
        }

        public void setRowid(String rowid) {
            this.rowid = rowid;
        }

        public String getFloorName() {
            return floorName;
        }

        public void setFloorName(String floorName) {
            this.floorName = floorName;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public String getInternalRef() {
            return internalRef;
        }

        public void setInternalRef(String internalRef) {
            this.internalRef = internalRef;
        }

    }
}
