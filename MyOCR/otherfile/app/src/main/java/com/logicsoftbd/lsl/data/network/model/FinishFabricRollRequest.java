package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FinishFabricRollRequest implements Serializable {
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

        public Barcode getBarcodeNos() {
            return BarcodeNos;
        }

        public void setBarcodeNos(Barcode barcodeNos) {
            BarcodeNos = barcodeNos;
        }

        @Expose
        @SerializedName("BarcodeNos")
        private Barcode BarcodeNos;

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
    public static class Barcode implements Serializable {
        @SerializedName("BARCODE_NO")
        @Expose
        public String BARCODE_NO;

    }
    public static class MasterPart implements Serializable {



        @Expose
        @SerializedName("COMPANY_ID")
        private int companyId;

        public Integer getISSUE_PURPOSE() {
            return ISSUE_PURPOSE;
        }

        public void setISSUE_PURPOSE(Integer ISSUE_PURPOSE) {
            this.ISSUE_PURPOSE = ISSUE_PURPOSE;
        }

        public String getISSUE_DATE() {
            return ISSUE_DATE;
        }

        public void setISSUE_DATE(String ISSUE_DATE) {
            this.ISSUE_DATE = ISSUE_DATE;
        }

        public Integer getINSERTED_BY() {
            return INSERTED_BY;
        }

        public void setINSERTED_BY(Integer INSERTED_BY) {
            this.INSERTED_BY = INSERTED_BY;
        }

        public Integer getBATCH_ID() {
            return BATCH_ID;
        }

        public void setBATCH_ID(Integer BATCH_ID) {
            this.BATCH_ID = BATCH_ID;
        }

        @SerializedName("ISSUE_PURPOSE")
        @Expose
        public Integer ISSUE_PURPOSE;

        @SerializedName("ISSUE_DATE")
        @Expose
        public String ISSUE_DATE;

        @SerializedName("INSERTED_BY")
        @Expose
        public Integer INSERTED_BY;

        @SerializedName("BATCH_ID")
        @Expose
        public Integer BATCH_ID;

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }


    }

    public static class DetailsPart implements Serializable {

        public String getRECEIVE_BASIS() {
            return RECEIVE_BASIS;
        }

        public void setRECEIVE_BASIS(String RECEIVE_BASIS) {
            this.RECEIVE_BASIS = RECEIVE_BASIS;
        }

        public String getPI_WO_BATCH_NO() {
            return PI_WO_BATCH_NO;
        }

        public void setPI_WO_BATCH_NO(String PI_WO_BATCH_NO) {
            this.PI_WO_BATCH_NO = PI_WO_BATCH_NO;
        }

        public String getBOOKING_NO() {
            return BOOKING_NO;
        }

        public void setBOOKING_NO(String BOOKING_NO) {
            this.BOOKING_NO = BOOKING_NO;
        }

        public String getCOMPANY_ID() {
            return COMPANY_ID;
        }

        public void setCOMPANY_ID(String COMPANY_ID) {
            this.COMPANY_ID = COMPANY_ID;
        }

        public String getPROD_ID() {
            return PROD_ID;
        }

        public void setPROD_ID(String PROD_ID) {
            this.PROD_ID = PROD_ID;
        }

        public String getGMT_ITEM_ID() {
            return GMT_ITEM_ID;
        }

        public void setGMT_ITEM_ID(String GMT_ITEM_ID) {
            this.GMT_ITEM_ID = GMT_ITEM_ID;
        }

        public String getBODY_PART_ID() {
            return BODY_PART_ID;
        }

        public void setBODY_PART_ID(String BODY_PART_ID) {
            this.BODY_PART_ID = BODY_PART_ID;
        }

        public String getPO_ID() {
            return PO_ID;
        }

        public void setPO_ID(String PO_ID) {
            this.PO_ID = PO_ID;
        }

        public String getITEM_CATEGORY() {
            return ITEM_CATEGORY;
        }

        public void setITEM_CATEGORY(String ITEM_CATEGORY) {
            this.ITEM_CATEGORY = ITEM_CATEGORY;
        }

        public String getTRANSACTION_TYPE() {
            return TRANSACTION_TYPE;
        }

        public void setTRANSACTION_TYPE(String TRANSACTION_TYPE) {
            this.TRANSACTION_TYPE = TRANSACTION_TYPE;
        }

        public String getTRANSACTION_DATE() {
            return TRANSACTION_DATE;
        }

        public void setTRANSACTION_DATE(String TRANSACTION_DATE) {
            this.TRANSACTION_DATE = TRANSACTION_DATE;
        }

        public String getSTORE_ID() {
            return STORE_ID;
        }

        public void setSTORE_ID(String STORE_ID) {
            this.STORE_ID = STORE_ID;
        }

        public String getCONS_QUANTITY() {
            return QNTY;
        }

        public void setCONS_QUANTITY(String CONS_QUANTITY) {
            this.QNTY = CONS_QUANTITY;
        }

        public String getRATE() {
            return RATE;
        }

        public void setRATE(String RATE) {
            this.RATE = RATE;
        }

        public String getINSERTED_BY() {
            return INSERTED_BY;
        }

        public void setINSERTED_BY(String INSERTED_BY) {
            this.INSERTED_BY = INSERTED_BY;
        }

        public String getINSERT_DATE() {
            return INSERT_DATE;
        }

        public void setINSERT_DATE(String INSERT_DATE) {
            this.INSERT_DATE = INSERT_DATE;
        }

        @Expose
        @SerializedName("BARCODE_NO")
        private String BARCODE_NO;

        @Expose
        @SerializedName("RECEIVE_BASIS")
        private String RECEIVE_BASIS;

        @Expose
        @SerializedName("COLOR_ID")
        private String COLOR_ID;

        public String getBARCODE_NO() {
            return BARCODE_NO;
        }

        public void setBARCODE_NO(String BARCODE_NO) {
            this.BARCODE_NO = BARCODE_NO;
        }

        public String getCOLOR_ID() {
            return COLOR_ID;
        }

        public void setCOLOR_ID(String COLOR_ID) {
            this.COLOR_ID = COLOR_ID;
        }

        @Expose
        @SerializedName("PI_WO_BATCH_NO")
        private String PI_WO_BATCH_NO;

        @Expose
        @SerializedName("BOOKING_NO")
        private String BOOKING_NO;

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
        @SerializedName("COMPANY_ID")
        private String COMPANY_ID;

        @Expose
        @SerializedName("PROD_ID")
        private String PROD_ID;

        @Expose
        @SerializedName("GMT_ITEM_ID")
        private String GMT_ITEM_ID;

        @Expose
        @SerializedName("BODY_PART_ID")
        private String BODY_PART_ID;

        @Expose
        @SerializedName("PO_ID")
        private String PO_ID;

        @Expose
        @SerializedName("ITEM_CATEGORY")
        private String ITEM_CATEGORY;


        @Expose
        @SerializedName("TRANSACTION_TYPE")
        private String TRANSACTION_TYPE;

        @Expose
        @SerializedName("TRANSACTION_DATE")
        private String TRANSACTION_DATE;

        @Expose
        @SerializedName("STORE_ID")
        private String STORE_ID;

        @SerializedName("QNTY")
        @Expose
        public String QNTY;

        @SerializedName("RATE")
        @Expose
        public String RATE;
        @Expose
        @SerializedName("INSERTED_BY")
        private String INSERTED_BY;

        @Expose
        @SerializedName("INSERT_DATE")
        private String INSERT_DATE;


    }
}
