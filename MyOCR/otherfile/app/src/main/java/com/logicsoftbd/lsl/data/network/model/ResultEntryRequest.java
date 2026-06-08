package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResultEntryRequest implements Serializable {
    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private Result data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }

    public static class Result implements Serializable {

        @Expose
        @SerializedName("MasterPart")
        private MasterPart masterPart;

        @Expose
        @SerializedName("DtlsPart")
        private List<DetailsPart> detailsPart;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public List<DetailsPart> getDetailsPart() {
            return detailsPart;
        }

        public void setDetailsPart(List<DetailsPart> detailsPart) {
            this.detailsPart = detailsPart;
        }
    }

    public static class MasterPart implements Serializable {



        @Expose
        @SerializedName("COMPANY_ID")
        private String companyId;

        @SerializedName("DTLS_ID")
        @Expose
        public String DTLS_ID;

        @SerializedName("PROD_ID")
        @Expose
        public String PROD_ID;

        @SerializedName("BARCODE_NO")
        @Expose
        public String BARCODE_NO;

        @SerializedName("ROLL_ID")
        @Expose
        public String ROLL_ID;


        @Expose
        @SerializedName("ROLL_NO")
        private String ROLL_NO;

        @SerializedName("QNTY")
        @Expose
        public String QNTY;

        @SerializedName("REJECT_QNTY")
        @Expose
        public String REJECT_QNTY;

        @SerializedName("QC_DATE")
        @Expose
        public String QC_DATE;

        @SerializedName("QC_NAME")
        @Expose
        public String QC_NAME;

        @SerializedName("COMMENTS")
        @Expose
        public String COMMENTS;

        @SerializedName("FABRIC_SHADE")
        @Expose
        public String FABRIC_SHADE;

        @Expose
        @SerializedName("AC_ROLL_WIDTH")
        private String AC_ROLL_WIDTH;

        @SerializedName("ROLL_WGT")
        @Expose
        public String ROLL_WGT;

        @SerializedName("ROLL_LENGTH")
        @Expose
        public String ROLL_LENGTH;

        @SerializedName("AC_GSM")
        @Expose
        public String AC_GSM;

        @SerializedName("ROLL_STATUS")
        @Expose
        public String ROLL_STATUS;

        @Expose
        @SerializedName("TOTAL_PANALTY")
        private String TOTAL_PANALTY;

        @SerializedName("TOTAL_POINT")
        @Expose
        public String TOTAL_POINT;

        @SerializedName("FABRIC_GRADE")
        @Expose
        public String FABRIC_GRADE;

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getDTLS_ID() {
            return DTLS_ID;
        }

        public void setDTLS_ID(String DTLS_ID) {
            this.DTLS_ID = DTLS_ID;
        }

        public String getPROD_ID() {
            return PROD_ID;
        }

        public void setPROD_ID(String PROD_ID) {
            this.PROD_ID = PROD_ID;
        }

        public String getBARCODE_NO() {
            return BARCODE_NO;
        }

        public void setBARCODE_NO(String BARCODE_NO) {
            this.BARCODE_NO = BARCODE_NO;
        }

        public String getROLL_ID() {
            return ROLL_ID;
        }

        public void setROLL_ID(String ROLL_ID) {
            this.ROLL_ID = ROLL_ID;
        }

        public String getROLL_NO() {
            return ROLL_NO;
        }

        public void setROLL_NO(String ROLL_NO) {
            this.ROLL_NO = ROLL_NO;
        }

        public String getQNTY() {
            return QNTY;
        }

        public void setQNTY(String QNTY) {
            this.QNTY = QNTY;
        }

        public String getREJECT_QNTY() {
            return REJECT_QNTY;
        }

        public void setREJECT_QNTY(String REJECT_QNTY) {
            this.REJECT_QNTY = REJECT_QNTY;
        }

        public String getQC_DATE() {
            return QC_DATE;
        }

        public void setQC_DATE(String QC_DATE) {
            this.QC_DATE = QC_DATE;
        }

        public String getQC_NAME() {
            return QC_NAME;
        }

        public void setQC_NAME(String QC_NAME) {
            this.QC_NAME = QC_NAME;
        }

        public String getAC_ROLL_WIDTH() {
            return AC_ROLL_WIDTH;
        }

        public void setAC_ROLL_WIDTH(String AC_ROLL_WIDTH) {
            this.AC_ROLL_WIDTH = AC_ROLL_WIDTH;
        }

        public String getROLL_WGT() {
            return ROLL_WGT;
        }

        public void setROLL_WGT(String ROLL_WGT) {
            this.ROLL_WGT = ROLL_WGT;
        }

        public String getROLL_LENGTH() {
            return ROLL_LENGTH;
        }

        public void setROLL_LENGTH(String ROLL_LENGTH) {
            this.ROLL_LENGTH = ROLL_LENGTH;
        }

        public String getCOMMENTS() {
            return COMMENTS;
        }

        public void setCOMMENTS(String COMMENTS) {
            this.COMMENTS = COMMENTS;
        }

        public String getFABRIC_SHADE() {
            return FABRIC_SHADE;
        }

        public void setFABRIC_SHADE(String FABRIC_SHADE) {
            this.FABRIC_SHADE = FABRIC_SHADE;
        }

        public String getAC_GSM() {
            return AC_GSM;
        }

        public void setAC_GSM(String AC_GSM) {
            this.AC_GSM = AC_GSM;
        }

        public String getROLL_STATUS() {
            return ROLL_STATUS;
        }

        public void setROLL_STATUS(String ROLL_STATUS) {
            this.ROLL_STATUS = ROLL_STATUS;
        }

        public String getTOTAL_PANALTY() {
            return TOTAL_PANALTY;
        }

        public void setTOTAL_PANALTY(String TOTAL_PANALTY) {
            this.TOTAL_PANALTY = TOTAL_PANALTY;
        }

        public String getTOTAL_POINT() {
            return TOTAL_POINT;
        }

        public void setTOTAL_POINT(String TOTAL_POINT) {
            this.TOTAL_POINT = TOTAL_POINT;
        }

        public String getFABRIC_GRADE() {
            return FABRIC_GRADE;
        }

        public void setFABRIC_GRADE(String FABRIC_GRADE) {
            this.FABRIC_GRADE = FABRIC_GRADE;
        }
    }

    public static class DetailsPart implements Serializable {

        @Expose
        @SerializedName("DEFECT_ID")
        private String DEFECT_ID;

        @Expose
        @SerializedName("DEFECT_NAME")
        private String DEFECT_NAME;

        @Expose
        @SerializedName("DEFECT_COUNT")
        private String DEFECT_COUNT;

        @Expose
        @SerializedName("FOUND_IN_INCH")
        private String FOUND_IN_INCH;

        @Expose
        @SerializedName("FOUND_IN_INCH_POINT")
        private String FOUND_IN_INCH_POINT;

        @Expose
        @SerializedName("PENALTY_POINT")
        private String PENALTY_POINT;

        public String getDEFECT_ID() {
            return DEFECT_ID;
        }

        public void setDEFECT_ID(String DEFECT_ID) {
            this.DEFECT_ID = DEFECT_ID;
        }

        public String getDEFECT_NAME() {
            return DEFECT_NAME;
        }

        public void setDEFECT_NAME(String DEFECT_NAME) {
            this.DEFECT_NAME = DEFECT_NAME;
        }

        public String getDEFECT_COUNT() {
            return DEFECT_COUNT;
        }

        public void setDEFECT_COUNT(String DEFECT_COUNT) {
            this.DEFECT_COUNT = DEFECT_COUNT;
        }

        public String getFOUND_IN_INCH() {
            return FOUND_IN_INCH;
        }

        public void setFOUND_IN_INCH(String FOUND_IN_INCH) {
            this.FOUND_IN_INCH = FOUND_IN_INCH;
        }

        public String getFOUND_IN_INCH_POINT() {
            return FOUND_IN_INCH_POINT;
        }

        public void setFOUND_IN_INCH_POINT(String FOUND_IN_INCH_POINT) {
            this.FOUND_IN_INCH_POINT = FOUND_IN_INCH_POINT;
        }

        public String getPENALTY_POINT() {
            return PENALTY_POINT;
        }

        public void setPENALTY_POINT(String PENALTY_POINT) {
            this.PENALTY_POINT = PENALTY_POINT;
        }
    }
}
