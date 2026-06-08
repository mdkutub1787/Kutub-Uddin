package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_GRNWiseYarnModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Rfid {

        @SerializedName("RFID_NO")
        @Expose
        private String rfidNo;
        @SerializedName("TRANS_ID")
        @Expose
        private String transId;

        public String getRfidNo() {
            return rfidNo;
        }

        public void setRfidNo(String rfidNo) {
            this.rfidNo = rfidNo;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

    }
    public class Data {

        @SerializedName("GRN_ID")
        @Expose
        private String grnId;
        @SerializedName("GRN_NO")
        @Expose
        private String grnNo;
        @SerializedName("MRR_ID")
        @Expose
        private String mrrId;
        @SerializedName("MRR_NO")
        @Expose
        private String mrrNo;
        @SerializedName("RFIDS")
        @Expose
        private List<Rfid> rfids;

        public String getGrnId() {
            return grnId;
        }

        public void setGrnId(String grnId) {
            this.grnId = grnId;
        }

        public String getGrnNo() {
            return grnNo;
        }

        public void setGrnNo(String grnNo) {
            this.grnNo = grnNo;
        }

        public String getMrrId() {
            return mrrId;
        }

        public void setMrrId(String mrrId) {
            this.mrrId = mrrId;
        }

        public String getMrrNo() {
            return mrrNo;
        }

        public void setMrrNo(String mrrNo) {
            this.mrrNo = mrrNo;
        }

        public List<Rfid> getRfids() {
            return rfids;
        }

        public void setRfids(List<Rfid> rfids) {
            this.rfids = rfids;
        }

    }

}