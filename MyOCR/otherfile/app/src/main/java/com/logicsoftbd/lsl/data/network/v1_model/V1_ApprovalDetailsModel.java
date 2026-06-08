package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_ApprovalDetailsModel {
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
    public class Data {

        @SerializedName("PAGE_ID")
        @Expose
        private String pAGEID;
        @SerializedName("APP_TITLE")
        @Expose
        private String aPPTITLE;
        @SerializedName("COMPANY_ID")
        @Expose
        private String cOMPANYID;
        @SerializedName("APP_ID")
        @Expose
        private String aPPID;
        @SerializedName("MESSAGE1")
        @Expose
        private String mESSAGE1;
        @SerializedName("MESSAGE2")
        @Expose
        private String mESSAGE2;
        @SerializedName("MESSAGE3")
        @Expose
        private String mESSAGE3;
        @SerializedName("MESSAGE4")
        @Expose
        private String mESSAGE4;

        public String getPAGEID() {
            return pAGEID;
        }

        public void setPAGEID(String pAGEID) {
            this.pAGEID = pAGEID;
        }

        public String getAPPTITLE() {
            return aPPTITLE;
        }

        public void setAPPTITLE(String aPPTITLE) {
            this.aPPTITLE = aPPTITLE;
        }

        public String getCOMPANYID() {
            return cOMPANYID;
        }

        public void setCOMPANYID(String cOMPANYID) {
            this.cOMPANYID = cOMPANYID;
        }

        public String getAPPID() {
            return aPPID;
        }

        public void setAPPID(String aPPID) {
            this.aPPID = aPPID;
        }

        public String getMESSAGE1() {
            return mESSAGE1;
        }

        public void setMESSAGE1(String mESSAGE1) {
            this.mESSAGE1 = mESSAGE1;
        }

        public String getMESSAGE2() {
            return mESSAGE2;
        }

        public void setMESSAGE2(String mESSAGE2) {
            this.mESSAGE2 = mESSAGE2;
        }

        public String getMESSAGE3() {
            return mESSAGE3;
        }

        public void setMESSAGE3(String mESSAGE3) {
            this.mESSAGE3 = mESSAGE3;
        }

        public String getMESSAGE4() {
            return mESSAGE4;
        }

        public void setMESSAGE4(String mESSAGE4) {
            this.mESSAGE4 = mESSAGE4;
        }

    }
}
