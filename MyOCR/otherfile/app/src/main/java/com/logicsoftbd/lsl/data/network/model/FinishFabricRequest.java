package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FinishFabricRequest implements Serializable{
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
        private List<FinishFabricResponse.ResultSet> detailsPart;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public List<FinishFabricResponse.ResultSet> getDetailsPart() {
            return detailsPart;
        }

        public void setDetailsPart(List<FinishFabricResponse.ResultSet> detailsPart) {
            this.detailsPart = detailsPart;
        }
    }

    public static class MasterPart implements Serializable {



        @Expose
        @SerializedName("COMPANY_ID")
        private int companyId;

        @SerializedName("SERVICE_SOURCE")
        @Expose
        public Integer serviceSource;

        @SerializedName("SERVICE_COMPANY")
        @Expose
        public Integer serviceCompany;

        @SerializedName("RECEIVE_DATE")
        @Expose
        public String receiveDate;

        @SerializedName("USER_ID")
        @Expose
        public Integer userId;

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public Integer getServiceSource() {
            return serviceSource;
        }

        public void setServiceSource(Integer serviceSource) {
            this.serviceSource = serviceSource;
        }

        public Integer getServiceCompany() {
            return serviceCompany;
        }

        public void setServiceCompany(Integer serviceCompany) {
            this.serviceCompany = serviceCompany;
        }

        public String getReceiveDate() {
            return receiveDate;
        }

        public void setReceiveDate(String receiveDate) {
            this.receiveDate = receiveDate;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }
    }

    public static class DetailsPart implements Serializable {

        @Expose
        @SerializedName("BARCODE_NO")
        private String BARCODE_NO;

        @Expose
        @SerializedName("BATCH_ID")
        private String BATCH_ID;

        @Expose
        @SerializedName("COLOR_ID")
        private String COLOR_ID;

        public String getBARCODE_NO() {
            return BARCODE_NO;
        }

        public void setBARCODE_NO(String BARCODE_NO) {
            this.BARCODE_NO = BARCODE_NO;
        }

        public String getBATCH_ID() {
            return BATCH_ID;
        }

        public void setBATCH_ID(String BATCH_ID) {
            this.BATCH_ID = BATCH_ID;
        }

        public String getCOLOR_ID() {
            return COLOR_ID;
        }

        public void setCOLOR_ID(String COLOR_ID) {
            this.COLOR_ID = COLOR_ID;
        }

        public String getCOLOR_NAME() {
            return COLOR_NAME;
        }

        public void setCOLOR_NAME(String COLOR_NAME) {
            this.COLOR_NAME = COLOR_NAME;
        }

        public String getBATCH_NO() {
            return BATCH_NO;
        }

        public void setBATCH_NO(String BATCH_NO) {
            this.BATCH_NO = BATCH_NO;
        }

        public String getPROD_ID() {
            return PROD_ID;
        }

        public void setPROD_ID(String PROD_ID) {
            this.PROD_ID = PROD_ID;
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

        public String getWIDTH_DIA_TYPE() {
            return WIDTH_DIA_TYPE;
        }

        public void setWIDTH_DIA_TYPE(String WIDTH_DIA_TYPE) {
            this.WIDTH_DIA_TYPE = WIDTH_DIA_TYPE;
        }

        public String getBODY_PART_ID() {
            return BODY_PART_ID;
        }

        public void setBODY_PART_ID(String BODY_PART_ID) {
            this.BODY_PART_ID = BODY_PART_ID;
        }

        public String getBODY_PART_NAME() {
            return BODY_PART_NAME;
        }

        public void setBODY_PART_NAME(String BODY_PART_NAME) {
            this.BODY_PART_NAME = BODY_PART_NAME;
        }

        public String getPO_BREAKDOWN_ID() {
            return PO_BREAKDOWN_ID;
        }

        public void setPO_BREAKDOWN_ID(String PO_BREAKDOWN_ID) {
            this.PO_BREAKDOWN_ID = PO_BREAKDOWN_ID;
        }

        public Integer getSHIFT_ID() {
            return SHIFT_ID;
        }

        public void setSHIFT_ID(Integer SHIFT_ID) {
            this.SHIFT_ID = SHIFT_ID;
        }

        public Integer getMACHINE_ID() {
            return MACHINE_ID;
        }

        public void setMACHINE_ID(Integer MACHINE_ID) {
            this.MACHINE_ID = MACHINE_ID;
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

        public String getBOOKING_WITHOUT_ORDER() {
            return BOOKING_WITHOUT_ORDER;
        }

        public void setBOOKING_WITHOUT_ORDER(String BOOKING_WITHOUT_ORDER) {
            this.BOOKING_WITHOUT_ORDER = BOOKING_WITHOUT_ORDER;
        }

        public int getQNTY() {
            return QNTY;
        }

        public void setQNTY(int QNTY) {
            this.QNTY = QNTY;
        }

        public Integer getQcQuantity() {
            return qcQuantity;
        }

        public void setQcQuantity(Integer qcQuantity) {
            this.qcQuantity = qcQuantity;
        }

        public Integer getRejectQuantity() {
            return rejectQuantity;
        }

        public void setRejectQuantity(Integer rejectQuantity) {
            this.rejectQuantity = rejectQuantity;
        }

        public String getPO_ID() {
            return PO_ID;
        }

        public void setPO_ID(String PO_ID) {
            this.PO_ID = PO_ID;
        }

        public String getPO_NUMBER() {
            return PO_NUMBER;
        }

        public void setPO_NUMBER(String PO_NUMBER) {
            this.PO_NUMBER = PO_NUMBER;
        }

        public String getJOB_NO() {
            return JOB_NO;
        }

        public void setJOB_NO(String JOB_NO) {
            this.JOB_NO = JOB_NO;
        }

        public String getSTYLE() {
            return STYLE;
        }

        public void setSTYLE(String STYLE) {
            this.STYLE = STYLE;
        }

        @Expose
        @SerializedName("COLOR_NAME")
        private String COLOR_NAME;

        @Expose
        @SerializedName("BATCH_NO")
        private String BATCH_NO;

        @Expose
        @SerializedName("PROD_ID")
        private String PROD_ID;

        @Expose
        @SerializedName("DETARMINATION_ID")
        private String DETARMINATION_ID;

        @Expose
        @SerializedName("ITEM_DESCRIPTION")
        private String ITEM_DESCRIPTION;

        @Expose
        @SerializedName("GSM")
        private String GSM;

        @Expose
        @SerializedName("WIDTH")
        private String WIDTH;

        @Expose
        @SerializedName("WIDTH_DIA_TYPE")
        private String WIDTH_DIA_TYPE;


        @Expose
        @SerializedName("BODY_PART_ID")
        private String BODY_PART_ID;

        @Expose
        @SerializedName("BODY_PART_NAME")
        private String BODY_PART_NAME;

        @Expose
        @SerializedName("PO_BREAKDOWN_ID")
        private String PO_BREAKDOWN_ID;

        @SerializedName("SHIFT_NAME")
        @Expose
        public Integer SHIFT_ID;

        @SerializedName("MACHINE_NO_ID")
        @Expose
        public Integer MACHINE_ID;
        @Expose
        @SerializedName("ROLL_ID")
        private String ROLL_ID;

        @Expose
        @SerializedName("ROLL_NO")
        private String ROLL_NO;

        @Expose
        @SerializedName("BOOKING_WITHOUT_ORDER")
        private String BOOKING_WITHOUT_ORDER;

        @Expose
        @SerializedName("PRODUCTION_QTY")
        private int QNTY;

        @SerializedName("QC_PASS_QNTY")
        @Expose
        public Integer qcQuantity;

        @SerializedName("REJECT_QNTY")
        @Expose
        public Integer rejectQuantity;
        @Expose
        @SerializedName("PO_ID")
        private String PO_ID;

        @Expose
        @SerializedName("PO_NUMBER")
        private String PO_NUMBER;

        @Expose
        @SerializedName("JOB_NO")
        private String JOB_NO;
        @Expose
        @SerializedName("STYLE")
        private String STYLE;
    }
}