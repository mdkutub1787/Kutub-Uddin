package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_BarcodeByBatchForQCResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private ResultSet resultSet;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public class ResultSet {

        @SerializedName("data")
        @Expose
        private List<Datum> data;
        @SerializedName("msg")
        @Expose
        private String msg;

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

    }

    public class Datum {

        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("barcode_no")
        @Expose
        private String barcodeNo;
        @SerializedName("roll_status")
        @Expose
        private String rollStatus;
        private Boolean status;

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getRollStatus() {
            return rollStatus;
        }

        public void setRollStatus(String rollStatus) {
            this.rollStatus = rollStatus;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }
    }
}
