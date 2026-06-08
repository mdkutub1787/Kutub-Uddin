package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ConsolitatedOrderSummeryModelClass {

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

        @SerializedName("MONTH")
        @Expose
        private String mONTH;
        @SerializedName("COMPANY")
        @Expose
        private String cOMPANY;
        @SerializedName("CONFIRM")
        @Expose
        private String cONFIRM;
        @SerializedName("PROJECTION")
        @Expose
        private String pROJECTION;
        @SerializedName("CONFIRM_QTY")
        @Expose
        private String cONFIRMQTY;
        @SerializedName("CONFIRM_AMOUNT")
        @Expose
        private String cONFIRMAMOUNT;
        @SerializedName("AVG")
        @Expose
        private String aVG;
        @SerializedName("FORECAST")
        @Expose
        private String fORECAST;
        @SerializedName("FORECAST_QTY")
        @Expose
        private String fORECASTQTY;
        @SerializedName("FORECAST_AMOUNT")
        @Expose
        private String fORECASTAMOUNT;

        public String getMONTH() {
            return mONTH;
        }

        public void setMONTH(String mONTH) {
            this.mONTH = mONTH;
        }

        public String getCOMPANY() {
            return cOMPANY;
        }

        public void setCOMPANY(String cOMPANY) {
            this.cOMPANY = cOMPANY;
        }

        public String getCONFIRM() {
            return cONFIRM;
        }

        public void setCONFIRM(String cONFIRM) {
            this.cONFIRM = cONFIRM;
        }

        public String getPROJECTION() {
            return String.valueOf(pROJECTION);
        }

        public void setPROJECTION(String pROJECTION) {
            this.pROJECTION = pROJECTION;
        }

        public String getCONFIRMQTY() {
            return String.valueOf(cONFIRMQTY);
        }

        public void setCONFIRMQTY(String cONFIRMQTY) {
            this.cONFIRMQTY = cONFIRMQTY;
        }

        public String getCONFIRMAMOUNT() {
            return String.valueOf(cONFIRMAMOUNT);
        }

        public void setCONFIRMAMOUNT(String cONFIRMAMOUNT) {
            this.cONFIRMAMOUNT = cONFIRMAMOUNT;
        }

        public String getAVG() {
            return String.valueOf(aVG);
        }

        public void setAVG(String aVG) {
            this.aVG = aVG;
        }

        public String getFORECAST() {
            return fORECAST;
        }

        public void setFORECAST(String fORECAST) {
            this.fORECAST = fORECAST;
        }

        public String getFORECASTQTY() {
            return fORECASTQTY;
        }

        public void setFORECASTQTY(String fORECASTQTY) {
            this.fORECASTQTY = fORECASTQTY;
        }

        public String getFORECASTAMOUNT() {
            return fORECASTAMOUNT;
        }

        public void setFORECASTAMOUNT(String fORECASTAMOUNT) {
            this.fORECASTAMOUNT = fORECASTAMOUNT;
        }

    }
}