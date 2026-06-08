package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_QcReportModelClass {
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
    public class YARNINFO {

        @SerializedName("DESCRIPTION")
        @Expose
        private String dESCRIPTION;
        @SerializedName("YARN_COUNT")
        @Expose
        private String yARNCOUNT;
        @SerializedName("LOT")
        @Expose
        private String lOT;
        @SerializedName("BRAND")
        @Expose
        private String bRAND;

        public String getDESCRIPTION() {
            return dESCRIPTION;
        }

        public void setDESCRIPTION(String dESCRIPTION) {
            this.dESCRIPTION = dESCRIPTION;
        }

        public String getYARNCOUNT() {
            return yARNCOUNT;
        }

        public void setYARNCOUNT(String yARNCOUNT) {
            this.yARNCOUNT = yARNCOUNT;
        }

        public String getLOT() {
            return lOT;
        }

        public void setLOT(String lOT) {
            this.lOT = lOT;
        }

        public String getBRAND() {
            return bRAND;
        }

        public void setBRAND(String bRAND) {
            this.bRAND = bRAND;
        }

    }
    public class QAINFO {

        @SerializedName("QC_NAME")
        @Expose
        private String qCNAME;
        @SerializedName("QC_STATUS")
        @Expose
        private String qCSTATUS;
        @SerializedName("QC_DATE")
        @Expose
        private String qCDATE;
        @SerializedName("ROLL_WEIGHT")
        @Expose
        private String rOLLWEIGHT;
        @SerializedName("FABRIC_GRADE")
        @Expose
        private String fABRICGRADE;
        @SerializedName("TOTAL_PENALTY_POINT")
        @Expose
        private String tOTALPENALTYPOINT;
        @SerializedName("TOTAL_POINT")
        @Expose
        private String tOTALPOINT;

        public String getQCNAME() {
            return qCNAME;
        }

        public void setQCNAME(String qCNAME) {
            this.qCNAME = qCNAME;
        }

        public String getQCSTATUS() {
            return qCSTATUS;
        }

        public void setQCSTATUS(String qCSTATUS) {
            this.qCSTATUS = qCSTATUS;
        }

        public String getQCDATE() {
            return qCDATE;
        }

        public void setQCDATE(String qCDATE) {
            this.qCDATE = qCDATE;
        }

        public String getROLLWEIGHT() {
            return rOLLWEIGHT;
        }

        public void setROLLWEIGHT(String rOLLWEIGHT) {
            this.rOLLWEIGHT = rOLLWEIGHT;
        }

        public String getFABRICGRADE() {
            return fABRICGRADE;
        }

        public void setFABRICGRADE(String fABRICGRADE) {
            this.fABRICGRADE = fABRICGRADE;
        }

        public String getTOTALPENALTYPOINT() {
            return tOTALPENALTYPOINT;
        }

        public void setTOTALPENALTYPOINT(String tOTALPENALTYPOINT) {
            this.tOTALPENALTYPOINT = tOTALPENALTYPOINT;
        }

        public String getTOTALPOINT() {
            return tOTALPOINT;
        }

        public void setTOTALPOINT(String tOTALPOINT) {
            this.tOTALPOINT = tOTALPOINT;
        }

    }
    public class KNITTINGINFO {

        @SerializedName("PRODUCTION_ID")
        @Expose
        private String pRODUCTIONID;
        @SerializedName("DATE")
        @Expose
        private String dATE;

        public String getPRODUCTIONID() {
            return pRODUCTIONID;
        }

        public void setPRODUCTIONID(String pRODUCTIONID) {
            this.pRODUCTIONID = pRODUCTIONID;
        }

        public String getDATE() {
            return dATE;
        }

        public void setDATE(String dATE) {
            this.dATE = dATE;
        }

    }
    public class Data {

        @SerializedName("BASIC_INFO")
        @Expose
        private BASICINFO bASICINFO;
        @SerializedName("KNITTING_INFO")
        @Expose
        private KNITTINGINFO kNITTINGINFO;
        @SerializedName("YARN_INFO")
        @Expose
        private YARNINFO yARNINFO;
        @SerializedName("QA_INFO")
        @Expose
        private QAINFO qAINFO;
        @SerializedName("BATCH_INFO")
        @Expose
        private BATCHINFO bATCHINFO;

        public BASICINFO getBASICINFO() {
            return bASICINFO;
        }

        public void setBASICINFO(BASICINFO bASICINFO) {
            this.bASICINFO = bASICINFO;
        }

        public KNITTINGINFO getKNITTINGINFO() {
            return kNITTINGINFO;
        }

        public void setKNITTINGINFO(KNITTINGINFO kNITTINGINFO) {
            this.kNITTINGINFO = kNITTINGINFO;
        }

        public YARNINFO getYARNINFO() {
            return yARNINFO;
        }

        public void setYARNINFO(YARNINFO yARNINFO) {
            this.yARNINFO = yARNINFO;
        }

        public QAINFO getQAINFO() {
            return qAINFO;
        }

        public void setQAINFO(QAINFO qAINFO) {
            this.qAINFO = qAINFO;
        }

        public BATCHINFO getBATCHINFO() {
            return bATCHINFO;
        }

        public void setBATCHINFO(BATCHINFO bATCHINFO) {
            this.bATCHINFO = bATCHINFO;
        }

    }
    public class BATCHINFO {

        @SerializedName("BATCH_NO")
        @Expose
        private String bATCHNO;
        @SerializedName("BATCH_DATE")
        @Expose
        private String bATCHDATE;
        @SerializedName("COLOR_ID")
        @Expose
        private String cOLORID;

        public String getBATCHNO() {
            return bATCHNO;
        }

        public void setBATCHNO(String bATCHNO) {
            this.bATCHNO = bATCHNO;
        }

        public String getBATCHDATE() {
            return bATCHDATE;
        }

        public void setBATCHDATE(String bATCHDATE) {
            this.bATCHDATE = bATCHDATE;
        }

        public String getCOLORID() {
            return cOLORID;
        }

        public void setCOLORID(String cOLORID) {
            this.cOLORID = cOLORID;
        }

    }
    public class BASICINFO {

        @SerializedName("BUYER")
        @Expose
        private String bUYER;
        @SerializedName("JOB")
        @Expose
        private String jOB;
        @SerializedName("STYLE")
        @Expose
        private String sTYLE;

        public String getBUYER() {
            return bUYER;
        }

        public void setBUYER(String bUYER) {
            this.bUYER = bUYER;
        }

        public String getJOB() {
            return jOB;
        }

        public void setJOB(String jOB) {
            this.jOB = jOB;
        }

        public String getSTYLE() {
            return sTYLE;
        }

        public void setSTYLE(String sTYLE) {
            this.sTYLE = sTYLE;
        }

    }
}
