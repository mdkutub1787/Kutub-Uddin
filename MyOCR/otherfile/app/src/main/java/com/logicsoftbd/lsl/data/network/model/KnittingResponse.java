package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class KnittingResponse implements Serializable {


    private boolean isFirst = true;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Expose
    @SerializedName("resultset")
    private ResultSet data;

    public ResultSet getData() {
        return data;
    }

    public void setData(ResultSet data) {
        this.data = data;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public static class ResultSet implements Serializable{

        @Expose
        @SerializedName("status")
        private String status;
        @Expose
        @SerializedName("msg")
        private String msg;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        @Expose
        @SerializedName("BARCODE_NO")
        private String BARCODE_NO;

        @Expose
        @SerializedName("ID")
        private String ID;

        @Expose
        @SerializedName("ENTRY_FORM")
        private String ENTRY_FORM;

        @Expose
        @SerializedName("COMPANY_ID")
        private String COMPANY_ID;

        @Expose
        @SerializedName("BOOKING_NO")
        private String BOOKING_NO;

        public String getBARCODE_NO() {
            return BARCODE_NO;
        }

        public void setBARCODE_NO(String BARCODE_NO) {
            this.BARCODE_NO = BARCODE_NO;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getENTRY_FORM() {
            return ENTRY_FORM;
        }

        public void setENTRY_FORM(String ENTRY_FORM) {
            this.ENTRY_FORM = ENTRY_FORM;
        }

        public String getCOMPANY_ID() {
            return COMPANY_ID;
        }

        public void setCOMPANY_ID(String COMPANY_ID) {
            this.COMPANY_ID = COMPANY_ID;
        }

        public String getBOOKING_NO() {
            return BOOKING_NO;
        }

        public void setBOOKING_NO(String BOOKING_NO) {
            this.BOOKING_NO = BOOKING_NO;
        }

        public String getBOOKING_ID() {
            return BOOKING_ID;
        }

        public void setBOOKING_ID(String BOOKING_ID) {
            this.BOOKING_ID = BOOKING_ID;
        }

        public String getFINISH_PRODUCTION_SOURCE() {
            return FINISH_PRODUCTION_SOURCE;
        }

        public void setFINISH_PRODUCTION_SOURCE(String FINISH_PRODUCTION_SOURCE) {
            this.FINISH_PRODUCTION_SOURCE = FINISH_PRODUCTION_SOURCE;
        }

        public String getFINISH_PRODUCTION_COMPANY() {
            return FINISH_PRODUCTION_COMPANY;
        }

        public void setFINISH_PRODUCTION_COMPANY(String FINISH_PRODUCTION_COMPANY) {
            this.FINISH_PRODUCTION_COMPANY = FINISH_PRODUCTION_COMPANY;
        }

        public String getRECEIVE_BASIS() {
            return RECEIVE_BASIS;
        }

        public void setRECEIVE_BASIS(String RECEIVE_BASIS) {
            this.RECEIVE_BASIS = RECEIVE_BASIS;
        }

        public String getPROD_ID() {
            return PROD_ID;
        }

        public void setPROD_ID(String PROD_ID) {
            this.PROD_ID = PROD_ID;
        }

        public String getDTLS_ID() {
            return DTLS_ID;
        }

        public void setDTLS_ID(String DTLS_ID) {
            this.DTLS_ID = DTLS_ID;
        }

        public String getKNITTING_COMPANY() {
            return KNITTING_COMPANY;
        }

        public void setKNITTING_COMPANY(String KNITTING_COMPANY) {
            this.KNITTING_COMPANY = KNITTING_COMPANY;
        }

        public String getKNITTING_SOURCE() {
            return KNITTING_SOURCE;
        }

        public void setKNITTING_SOURCE(String KNITTING_SOURCE) {
            this.KNITTING_SOURCE = KNITTING_SOURCE;
        }

        public String getGSM() {
            return GSM;
        }

        public void setGSM(String GSM) {
            this.GSM = GSM;
        }

        public String getWIDTH() {
            return WIDTH;
        }

        public void setWIDTH(String WIDTH) {
            this.WIDTH = WIDTH;
        }

        public String getCOLOR_ID() {
            return COLOR_ID;
        }

        public void setCOLOR_ID(String COLOR_ID) {
            this.COLOR_ID = COLOR_ID;
        }

        public String getYARN_LOT() {
            return YARN_LOT;
        }

        public void setYARN_LOT(String YARN_LOT) {
            this.YARN_LOT = YARN_LOT;
        }

        public String getYARN_COUNT() {
            return YARN_COUNT;
        }

        public void setYARN_COUNT(String YARN_COUNT) {
            this.YARN_COUNT = YARN_COUNT;
        }

        public String getQNTY2() {
            return QNTY2;
        }

        public void setQNTY2(String QNTY2) {
            this.QNTY2 = QNTY2;
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

        public String getBODY_PART_ID() {
            return BODY_PART_ID;
        }

        public void setBODY_PART_ID(String BODY_PART_ID) {
            this.BODY_PART_ID = BODY_PART_ID;
        }

        public String getFEBRIC_DESCRIPTION_ID() {
            return FEBRIC_DESCRIPTION_ID;
        }

        public void setFEBRIC_DESCRIPTION_ID(String FEBRIC_DESCRIPTION_ID) {
            this.FEBRIC_DESCRIPTION_ID = FEBRIC_DESCRIPTION_ID;
        }

        public String getMACHINE_NO_ID() {
            return MACHINE_NO_ID;
        }

        public void setMACHINE_NO_ID(String MACHINE_NO_ID) {
            this.MACHINE_NO_ID = MACHINE_NO_ID;
        }

        @Expose
        @SerializedName("BOOKING_ID")
        private String BOOKING_ID;

        @Expose
        @SerializedName("FINISH_PRODUCTION_SOURCE")
        private String FINISH_PRODUCTION_SOURCE;

        @Expose
        @SerializedName("FINISH_PRODUCTION_COMPANY")
        private String FINISH_PRODUCTION_COMPANY;
        @Expose
        @SerializedName("RECEIVE_BASIS")
        private String RECEIVE_BASIS;

        @Expose
        @SerializedName("PROD_ID")
        private String PROD_ID;

        @Expose
        @SerializedName("DTLS_ID")
        private String DTLS_ID;

        @Expose
        @SerializedName("KNITTING_COMPANY")
        private String KNITTING_COMPANY;


        @Expose
        @SerializedName("KNITTING_SOURCE")
        private String KNITTING_SOURCE;

        @Expose
        @SerializedName("GSM")
        private String GSM;

        @Expose
        @SerializedName("WIDTH")
        private String WIDTH;

        @Expose
        @SerializedName("COLOR_ID")
        private String COLOR_ID;

        @Expose
        @SerializedName("YARN_LOT")
        private String YARN_LOT;

        @Expose
        @SerializedName("YARN_COUNT")
        private String YARN_COUNT;

        @Expose
        @SerializedName("QNTY2")
        private String QNTY2;

        @Expose
        @SerializedName("ROLL_ID")
        private String ROLL_ID;


        @Expose
        @SerializedName("ROLL_NO")
        private String ROLL_NO;

        @Expose
        @SerializedName("QNTY")
        private String QNTY;

        @Expose
        @SerializedName("BODY_PART_ID")
        private String BODY_PART_ID;


        @Expose
        @SerializedName("FEBRIC_DESCRIPTION_ID")
        private String FEBRIC_DESCRIPTION_ID;

        @Expose
        @SerializedName("MACHINE_NO_ID")
        private String MACHINE_NO_ID;

    }
}
