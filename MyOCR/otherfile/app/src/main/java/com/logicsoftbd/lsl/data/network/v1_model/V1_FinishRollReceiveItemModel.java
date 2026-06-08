package com.logicsoftbd.lsl.data.network.v1_model;

import java.io.Serializable;

public class V1_FinishRollReceiveItemModel implements Serializable {
    private String status;

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

    private String BARCODE_NO;

    private String GREY_SYS_ID;

    private String GREY_SYS_NUMBER;

    private String DTLS_ID;

    private String BOOKING_NO;

    private String BOOKING_ID;
    private String JOB_NO;

    private String STYLE;

    private String PO_NUMBER;
    private String PO_ID;
    private String STYLE_REF_NO;


    public String getSTYLE() {
        return STYLE;
    }

    public void setSTYLE(String STYLE) {
        this.STYLE = STYLE;
    }

    public String getPO_BREAKDOWN_ID() {
        return PO_BREAKDOWN_ID;
    }

    public void setPO_BREAKDOWN_ID(String PO_BREAKDOWN_ID) {
        this.PO_BREAKDOWN_ID = PO_BREAKDOWN_ID;
    }

    public String getDETARMINATION_ID() {
        return DETARMINATION_ID;
    }

    public void setDETARMINATION_ID(String DETARMINATION_ID) {
        this.DETARMINATION_ID = DETARMINATION_ID;
    }

    public String getITEM_DESCRIPTION() {
        return ITEM_DESCRIPTION;
    }

    public void setITEM_DESCRIPTION(String ITEM_DESCRIPTION) {
        this.ITEM_DESCRIPTION = ITEM_DESCRIPTION;
    }

    public String getWIDTH_DIA_TYPE() {
        return WIDTH_DIA_TYPE;
    }

    public void setWIDTH_DIA_TYPE(String WIDTH_DIA_TYPE) {
        this.WIDTH_DIA_TYPE = WIDTH_DIA_TYPE;
    }

    private String DETERMINATION_ID;


    private String COLOR_NAME;

    private String BODY_PART_NAME;

    private String BWO;

    private String RECEIVE_BASIS_ID;

    private String BUYER_NAME;

    private String KNITTING_SOURCE;

    private String PO_BREAKDOWN_ID;

    private String KNITTING_COMPANY;


    private String KNITTING_COMPANY_NAME;

    private String DIA_WIDTH_TYPE;

    private String DIA_WIDTH_TYPE_NAME;

    private String BATCH_ID;

    private String PROD_ID;

    private String DETARMINATION_ID;


    private String ITEM_DESCRIPTION;

    private String FINISH_PRODUCTION_COMPANY;

    private String GMT_ITEM_ID;

    private int QC_PASS_QNTY;

    private String RATE;

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


    public String getKNITTING_SOURCE() {
        return KNITTING_SOURCE;
    }

    public void setKNITTING_SOURCE(String KNITTING_SOURCE) {
        this.KNITTING_SOURCE = KNITTING_SOURCE;
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

    private String BOOKING_WITHOUT_ORDER;
    private String DYEING_CHARGE;
    private String PREV_REPROCESS;
    private String GREY_RATE;
    private String BODY_PART_ID;
    private String FEBRIC_DESCRIPTION_ID;


    private String MACHINE_NO_ID;

    private String GSM;

    private String WIDTH;

    private String COLOR_ID;

    private String WIDTH_DIA_TYPE;

    private String YARN_COUNT;

    private String QNTY2;
    private String CONSTRUCTION;

    private String COMPOSITION;
    private String ROLL_ID;

    private String DIA;
    private String BODYPART_ID;
    private String REJECT_QNTY;

    public String getBARCODE_NO() {
        return BARCODE_NO;
    }

    public void setBARCODE_NO(String BARCODE_NO) {
        this.BARCODE_NO = BARCODE_NO;
    }


    public String getGREY_SYS_ID() {
        return GREY_SYS_ID;
    }

    public void setGREY_SYS_ID(String GREY_SYS_ID) {
        this.GREY_SYS_ID = GREY_SYS_ID;
    }

    public String getGREY_SYS_NUMBER() {
        return GREY_SYS_NUMBER;
    }

    public void setGREY_SYS_NUMBER(String GREY_SYS_NUMBER) {
        this.GREY_SYS_NUMBER = GREY_SYS_NUMBER;
    }

    public String getDETERMINATION_ID() {
        return DETERMINATION_ID;
    }

    public void setDETERMINATION_ID(String DETERMINATION_ID) {
        this.DETERMINATION_ID = DETERMINATION_ID;
    }

    public String getDYEING_CHARGE() {
        return DYEING_CHARGE;
    }

    public void setDYEING_CHARGE(String DYEING_CHARGE) {
        this.DYEING_CHARGE = DYEING_CHARGE;
    }

    public String getPREV_REPROCESS() {
        return PREV_REPROCESS;
    }

    public void setPREV_REPROCESS(String PREV_REPROCESS) {
        this.PREV_REPROCESS = PREV_REPROCESS;
    }

    public String getGREY_RATE() {
        return GREY_RATE;
    }

    public void setGREY_RATE(String GREY_RATE) {
        this.GREY_RATE = GREY_RATE;
    }

    public String getCONSTRUCTION() {
        return CONSTRUCTION;
    }

    public void setCONSTRUCTION(String CONSTRUCTION) {
        this.CONSTRUCTION = CONSTRUCTION;
    }

    public String getCOMPOSITION() {
        return COMPOSITION;
    }

    public void setCOMPOSITION(String COMPOSITION) {
        this.COMPOSITION = COMPOSITION;
    }

    public String getDIA() {
        return DIA;
    }

    public void setDIA(String DIA) {
        this.DIA = DIA;
    }

    public String getBODYPART_ID() {
        return BODYPART_ID;
    }

    public void setBODYPART_ID(String BODYPART_ID) {
        this.BODYPART_ID = BODYPART_ID;
    }

    public String getREJECT_QNTY() {
        return REJECT_QNTY;
    }

    public void setREJECT_QNTY(String REJECT_QNTY) {
        this.REJECT_QNTY = REJECT_QNTY;
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

    private String ROLL_NO;

    private String QNTY;
}
