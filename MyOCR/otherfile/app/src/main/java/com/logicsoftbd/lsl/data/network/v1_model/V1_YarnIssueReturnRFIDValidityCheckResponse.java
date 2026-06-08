package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_YarnIssueReturnRFIDValidityCheckResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class Datum {

        @SerializedName("RFID_NO")
        @Expose
        private String rfidNo;
        @SerializedName("RFID_STATUS")
        @Expose
        private Integer rfidStatus;
        @SerializedName("RFID_STATUS_NAME")
        @Expose
        private String rfidStatusName;

        public String getRfidNo() {
            return rfidNo;
        }

        public void setRfidNo(String rfidNo) {
            this.rfidNo = rfidNo;
        }

        public Integer getRfidStatus() {
            return rfidStatus;
        }

        public void setRfidStatus(Integer rfidStatus) {
            this.rfidStatus = rfidStatus;
        }

        public String getRfidStatusName() {
            return rfidStatusName;
        }

        public void setRfidStatusName(String rfidStatusName) {
            this.rfidStatusName = rfidStatusName;
        }

    }
}
