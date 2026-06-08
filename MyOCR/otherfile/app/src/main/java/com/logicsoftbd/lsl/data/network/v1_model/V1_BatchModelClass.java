package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_BatchModelClass {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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
    public class Datum {

        @SerializedName("RECV_NUMBER")
        @Expose
        private String rECVNUMBER;
        @SerializedName("FABRIC_DESCRIPTION_ID")
        @Expose
        private String fABRICDESCRIPTIONID;
        @SerializedName("RECV_ID")
        @Expose
        private String rECVID;
        @SerializedName("PRO_DTLS_ID")
        @Expose
        private String pRODTLSID;
        @SerializedName("BATCH_NO")
        @Expose
        private String bATCHNO;
        @SerializedName("BATCH_ID")
        @Expose
        private String bATCHID;
        @SerializedName("PROD_ID")
        @Expose
        private String pRODID;
        @SerializedName("BUYER_ID")
        @Expose
        private String bUYERID;

        public String getRECVNUMBER() {
            return rECVNUMBER;
        }

        public void setRECVNUMBER(String rECVNUMBER) {
            this.rECVNUMBER = rECVNUMBER;
        }

        public String getFABRICDESCRIPTIONID() {
            return fABRICDESCRIPTIONID;
        }

        public void setFABRICDESCRIPTIONID(String fABRICDESCRIPTIONID) {
            this.fABRICDESCRIPTIONID = fABRICDESCRIPTIONID;
        }

        public String getRECVID() {
            return rECVID;
        }

        public void setRECVID(String rECVID) {
            this.rECVID = rECVID;
        }

        public String getPRODTLSID() {
            return pRODTLSID;
        }

        public void setPRODTLSID(String pRODTLSID) {
            this.pRODTLSID = pRODTLSID;
        }

        public String getBATCHNO() {
            return bATCHNO;
        }

        public void setBATCHNO(String bATCHNO) {
            this.bATCHNO = bATCHNO;
        }

        public String getBATCHID() {
            return bATCHID;
        }

        public void setBATCHID(String bATCHID) {
            this.bATCHID = bATCHID;
        }

        public String getPRODID() {
            return pRODID;
        }

        public void setPRODID(String pRODID) {
            this.pRODID = pRODID;
        }

        public String getBUYERID() {
            return bUYERID;
        }

        public void setBUYERID(String bUYERID) {
            this.bUYERID = bUYERID;
        }

    }
}
