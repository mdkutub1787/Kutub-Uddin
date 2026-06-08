package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FinishFabricIssueSet implements Serializable {


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
    public DetailsSet data;

    public DetailsSet getData() {
        return data;
    }

    public void setData(DetailsSet data) {
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

    public static class DetailsSet implements Serializable {

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

        @Expose
        @SerializedName("BOOKING_ID")
        private String BOOKING_ID;
        @Expose
        @SerializedName("JOB_NO")
        private String JOB_NO;

        @Expose
        @SerializedName("BUYER_NAME")
        private String BUYER_NAME;

        @Expose
        @SerializedName("PO_NUMBER")
        private String PO_NUMBER;
        @Expose
        @SerializedName("PO_ID")
        private String PO_ID;
        @Expose
        @SerializedName("STYLE_REF_NO")
        private String STYLE_REF_NO;

        @Expose
        @SerializedName("PO_YEAR")
        private String PO_YEAR;


        @Expose
        @SerializedName("COLOR_NAME")
        private String COLOR_NAME;

        @Expose
        @SerializedName("BODY_PART_NAME")
        private String BODY_PART_NAME;

        @Expose
        @SerializedName("BWO")
        private String BWO;

        @Expose
        @SerializedName("RECEIVE_BASIS_ID")
        private String RECEIVE_BASIS_ID;

        @Expose
        @SerializedName("RECEIVE_BASIS")
        private String RECEIVE_BASIS;

        @Expose
        @SerializedName("KNITTING_SOURCE")
        private String KNITTING_SOURCE;

        @Expose
        @SerializedName("KNITTING_SOURCE_NAME")
        private String KNITTING_SOURCE_NAME;

        @Expose
        @SerializedName("KNITTING_COMPANY")
        private String KNITTING_COMPANY;


        @Expose
        @SerializedName("KNITTING_COMPANY_NAME")
        private String KNITTING_COMPANY_NAME;

        @Expose
        @SerializedName("DIA_WIDTH_TYPE")
        private String DIA_WIDTH_TYPE;

        @Expose
        @SerializedName("DIA_WIDTH_TYPE_NAME")
        private String DIA_WIDTH_TYPE_NAME;

        @Expose
        @SerializedName("BATCH_ID")
        private String BATCH_ID;

        @Expose
        @SerializedName("PROD_ID")
        private String PROD_ID;

        @Expose
        @SerializedName("FABRIC_DESCRIPTION_ID")
        private String FABRIC_DESCRIPTION_ID;



        @Expose
        @SerializedName("FINISH_PRODUCTION_SOURCE")
        private String FINISH_PRODUCTION_SOURCE;

        @Expose
        @SerializedName("FINISH_PRODUCTION_COMPANY")
        private String FINISH_PRODUCTION_COMPANY;
        @Expose
        @SerializedName("DTLS_ID")
        private String DTLS_ID;

        @Expose
        @SerializedName("GMT_ITEM_ID")
        private String GMT_ITEM_ID;

        @Expose
        @SerializedName("QC_PASS_QNTY")
        private int QC_PASS_QNTY;

        @Expose
        @SerializedName("RATE")
        private String RATE;

        @Expose
        @SerializedName("REPROCESS")
        private String REPROCESS;

        public String getJOB_NO() {
            return JOB_NO;
        }

        public void setJOB_NO(String JOB_NO) {
            this.JOB_NO = JOB_NO;
        }

        public String getBUYER_NAME() {
            return BUYER_NAME;
        }

        public void setBUYER_NAME(String BUYER_NAME) {
            this.BUYER_NAME = BUYER_NAME;
        }

        public String getPO_NUMBER() {
            return PO_NUMBER;
        }

        public void setPO_NUMBER(String PO_NUMBER) {
            this.PO_NUMBER = PO_NUMBER;
        }

        public String getPO_ID() {
            return PO_ID;
        }

        public void setPO_ID(String PO_ID) {
            this.PO_ID = PO_ID;
        }

        public String getSTYLE_REF_NO() {
            return STYLE_REF_NO;
        }

        public void setSTYLE_REF_NO(String STYLE_REF_NO) {
            this.STYLE_REF_NO = STYLE_REF_NO;
        }

        public String getPO_YEAR() {
            return PO_YEAR;
        }

        public void setPO_YEAR(String PO_YEAR) {
            this.PO_YEAR = PO_YEAR;
        }

        public String getCOLOR_NAME() {
            return COLOR_NAME;
        }

        public void setCOLOR_NAME(String COLOR_NAME) {
            this.COLOR_NAME = COLOR_NAME;
        }

        public String getBODY_PART_NAME() {
            return BODY_PART_NAME;
        }

        public void setBODY_PART_NAME(String BODY_PART_NAME) {
            this.BODY_PART_NAME = BODY_PART_NAME;
        }

        public String getBWO() {
            return BWO;
        }

        public void setBWO(String BWO) {
            this.BWO = BWO;
        }

        public String getRECEIVE_BASIS_ID() {
            return RECEIVE_BASIS_ID;
        }

        public void setRECEIVE_BASIS_ID(String RECEIVE_BASIS_ID) {
            this.RECEIVE_BASIS_ID = RECEIVE_BASIS_ID;
        }

        public String getRECEIVE_BASIS() {
            return RECEIVE_BASIS;
        }

        public void setRECEIVE_BASIS(String RECEIVE_BASIS) {
            this.RECEIVE_BASIS = RECEIVE_BASIS;
        }

        public String getKNITTING_SOURCE() {
            return KNITTING_SOURCE;
        }

        public void setKNITTING_SOURCE(String KNITTING_SOURCE) {
            this.KNITTING_SOURCE = KNITTING_SOURCE;
        }

        public String getKNITTING_SOURCE_NAME() {
            return KNITTING_SOURCE_NAME;
        }

        public void setKNITTING_SOURCE_NAME(String KNITTING_SOURCE_NAME) {
            this.KNITTING_SOURCE_NAME = KNITTING_SOURCE_NAME;
        }

        public String getKNITTING_COMPANY() {
            return KNITTING_COMPANY;
        }

        public void setKNITTING_COMPANY(String KNITTING_COMPANY) {
            this.KNITTING_COMPANY = KNITTING_COMPANY;
        }

        public String getKNITTING_COMPANY_NAME() {
            return KNITTING_COMPANY_NAME;
        }

        public void setKNITTING_COMPANY_NAME(String KNITTING_COMPANY_NAME) {
            this.KNITTING_COMPANY_NAME = KNITTING_COMPANY_NAME;
        }

        public String getDIA_WIDTH_TYPE() {
            return DIA_WIDTH_TYPE;
        }

        public void setDIA_WIDTH_TYPE(String DIA_WIDTH_TYPE) {
            this.DIA_WIDTH_TYPE = DIA_WIDTH_TYPE;
        }

        public String getDIA_WIDTH_TYPE_NAME() {
            return DIA_WIDTH_TYPE_NAME;
        }

        public void setDIA_WIDTH_TYPE_NAME(String DIA_WIDTH_TYPE_NAME) {
            this.DIA_WIDTH_TYPE_NAME = DIA_WIDTH_TYPE_NAME;
        }

        public String getBATCH_ID() {
            return BATCH_ID;
        }

        public void setBATCH_ID(String BATCH_ID) {
            this.BATCH_ID = BATCH_ID;
        }

        public String getFABRIC_DESCRIPTION_ID() {
            return FABRIC_DESCRIPTION_ID;
        }

        public void setFABRIC_DESCRIPTION_ID(String FABRIC_DESCRIPTION_ID) {
            this.FABRIC_DESCRIPTION_ID = FABRIC_DESCRIPTION_ID;
        }

        public String getGMT_ITEM_ID() {
            return GMT_ITEM_ID;
        }

        public void setGMT_ITEM_ID(String GMT_ITEM_ID) {
            this.GMT_ITEM_ID = GMT_ITEM_ID;
        }

        public int getQC_PASS_QNTY() {
            return QC_PASS_QNTY;
        }

        public void setQC_PASS_QNTY(int QC_PASS_QNTY) {
            this.QC_PASS_QNTY = QC_PASS_QNTY;
        }

        public String getRATE() {
            return RATE;
        }

        public void setRATE(String RATE) {
            this.RATE = RATE;
        }

        public String getREPROCESS() {
            return REPROCESS;
        }

        public void setREPROCESS(String REPROCESS) {
            this.REPROCESS = REPROCESS;
        }

        public String getBOOKING_WITHOUT_ORDER() {
            return BOOKING_WITHOUT_ORDER;
        }

        public void setBOOKING_WITHOUT_ORDER(String BOOKING_WITHOUT_ORDER) {
            this.BOOKING_WITHOUT_ORDER = BOOKING_WITHOUT_ORDER;
        }

        @Expose
        @SerializedName("BOOKING_WITHOUT_ORDER")
        private String BOOKING_WITHOUT_ORDER;

        @Expose
        @SerializedName("BODY_PART_ID")
        private String BODY_PART_ID;

        @Expose
        @SerializedName("FEBRIC_DESCRIPTION_ID")
        private String FEBRIC_DESCRIPTION_ID;


        @Expose
        @SerializedName("MACHINE_NO_ID")
        private String MACHINE_NO_ID;

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

        @Expose
        @SerializedName("ROLL_NO")
        private String ROLL_NO;

        @Expose
        @SerializedName("QNTY")
        private String QNTY;

    }

    public static class ResultSet implements Serializable {
        public DetailsSet getDtlsPart() {
            return dtlsPart;
        }

        public void setDtlsPart(DetailsSet dtlsPart) {
            this.dtlsPart = dtlsPart;
        }
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
        @SerializedName("dtlsPart")
        public DetailsSet dtlsPart;

    }
}

