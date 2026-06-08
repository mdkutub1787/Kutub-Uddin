package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FinishFabricRollReceiveRequest implements Serializable {
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

        public void setDetailsPart(ArrayList<DetailsPart> detailsPart) {
            this.detailsPart = detailsPart;
        }
    }
    public static class Barcode implements Serializable {
        @SerializedName("BARCODE_NO")
        @Expose
        public String BARCODE_NO;

    }
    public static class MasterPart implements Serializable {


        public int getCOMPANY_ID() {
            return COMPANY_ID;
        }

        public void setCOMPANY_ID(int COMPANY_ID) {
            this.COMPANY_ID = COMPANY_ID;
        }

        public String getLOCATION_ID() {
            return LOCATION_ID;
        }

        public void setLOCATION_ID(String LOCATION_ID) {
            this.LOCATION_ID = LOCATION_ID;
        }

        public String getPRODUCT_IDS() {
            return PRODUCT_IDS;
        }

        public void setPRODUCT_IDS(String PRODUCT_IDS) {
            this.PRODUCT_IDS = PRODUCT_IDS;
        }

        public String getSTORE_ID() {
            return STORE_ID;
        }

        public void setSTORE_ID(String STORE_ID) {
            this.STORE_ID = STORE_ID;
        }

        public String getRECV_DATE() {
            return RECV_DATE;
        }

        public void setRECV_DATE(String RECV_DATE) {
            this.RECV_DATE = RECV_DATE;
        }

        public String getCHALLAN_NO() {
            return CHALLAN_NO;
        }

        public void setCHALLAN_NO(String CHALLAN_NO) {
            this.CHALLAN_NO = CHALLAN_NO;
        }

        @Expose
        @SerializedName("COMPANY_ID")
        private int COMPANY_ID;

        public Integer getINSERTED_BY() {
            return INSERTED_BY;
        }

        public void setINSERTED_BY(Integer INSERTED_BY) {
            this.INSERTED_BY = INSERTED_BY;
        }


        @SerializedName("LOCATION_ID")
        @Expose
        public String LOCATION_ID;
        @SerializedName("PRODUCT_IDS")
        @Expose
        public String PRODUCT_IDS;
        @SerializedName("STORE_ID")
        @Expose
        public String STORE_ID;
        @SerializedName("RECV_DATE")
        @Expose
        public String RECV_DATE;

        @SerializedName("INSERTED_BY")
        @Expose
        public Integer INSERTED_BY;

        @SerializedName("CHALLAN_NO")
        @Expose
        public String CHALLAN_NO;



    }

    public static class DetailsPart implements Serializable {

        @Expose
        @SerializedName("BARCODE_NO")
        private String BARCODE_NO;

        @Expose
        @SerializedName("COLOR_ID")
        private String COLOR_ID;
        @Expose
        @SerializedName("COLOR_NAME")
        private String COLOR_NAME;
        @Expose
        @SerializedName("BATCH_ID")
        private String BATCH_ID;
        @Expose
        @SerializedName("GSM")
        private String GSM;

        @Expose
        @SerializedName("DETERMINATION_ID")
        private String DETERMINATION_ID;
        @Expose
        @SerializedName("BOOKING_NO")
        private String BOOKING_NO;

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
        @SerializedName("BODYPART_ID")
        private String BODYPART_ID;

        public String getBODYPART_ID() {
            return BODYPART_ID;
        }

        public void setBODYPART_ID(String BODYPART_ID) {
            this.BODYPART_ID = BODYPART_ID;
        }

        @Expose
        @SerializedName("DIA")
        private String DIA;

        @Expose
        @SerializedName("ROLL_ID")
        private String ROLL_ID;

        @Expose
        @SerializedName("PO_ID")
        private String PO_ID;

        @Expose
        @SerializedName("ROLL_NO")
        private String ROLL_NO;


        @Expose
        @SerializedName("TRANSACTION_TYPE")
        private String TRANSACTION_TYPE;

        @Expose
        @SerializedName("TRANSACTION_DATE")
        private String TRANSACTION_DATE;

        @Expose
        @SerializedName("ITEM_CATEGORY")
        private String ITEM_CATEGORY;

        @SerializedName("CONS_QUANTITY")
        @Expose
        public String CONS_QUANTITY;

        @SerializedName("CURRENT_WEIGHT")
        @Expose
        public String CURRENT_WEIGHT;

        @SerializedName("QNTY")
        @Expose
        public String QNTY;

        @SerializedName("REJECT_QNTY")
        @Expose
        public String REJECT_QNTY;


        @SerializedName("GREY_RATE")
        @Expose
        public String GREY_RATE;

        @SerializedName("DYEING_CHARGE")
        @Expose
        public String DYEING_CHARGE;

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

        public String getCOLOR_NAME() {
            return COLOR_NAME;
        }

        public void setCOLOR_NAME(String COLOR_NAME) {
            this.COLOR_NAME = COLOR_NAME;
        }

        public String getBATCH_ID() {
            return BATCH_ID;
        }

        public void setBATCH_ID(String BATCH_ID) {
            this.BATCH_ID = BATCH_ID;
        }

        public String getGSM() {
            return GSM;
        }

        public void setGSM(String GSM) {
            this.GSM = GSM;
        }

        public String getDETERMINATION_ID() {
            return DETERMINATION_ID;
        }

        public void setDETERMINATION_ID(String DETERMINATION_ID) {
            this.DETERMINATION_ID = DETERMINATION_ID;
        }

        public String getBOOKING_NO() {
            return BOOKING_NO;
        }

        public void setBOOKING_NO(String BOOKING_NO) {
            this.BOOKING_NO = BOOKING_NO;
        }

        public String getBOOKING_WITHOUT_ORDER() {
            return BOOKING_WITHOUT_ORDER;
        }

        public void setBOOKING_WITHOUT_ORDER(String BOOKING_WITHOUT_ORDER) {
            this.BOOKING_WITHOUT_ORDER = BOOKING_WITHOUT_ORDER;
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

        public String getDIA() {
            return DIA;
        }

        public void setDIA(String DIA) {
            this.DIA = DIA;
        }

        public String getROLL_ID() {
            return ROLL_ID;
        }

        public void setROLL_ID(String ROLL_ID) {
            this.ROLL_ID = ROLL_ID;
        }

        public String getPO_ID() {
            return PO_ID;
        }

        public void setPO_ID(String PO_ID) {
            this.PO_ID = PO_ID;
        }

        public String getROLL_NO() {
            return ROLL_NO;
        }

        public void setROLL_NO(String ROLL_NO) {
            this.ROLL_NO = ROLL_NO;
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

        public String getITEM_CATEGORY() {
            return ITEM_CATEGORY;
        }

        public void setITEM_CATEGORY(String ITEM_CATEGORY) {
            this.ITEM_CATEGORY = ITEM_CATEGORY;
        }

        public String getCONS_QUANTITY() {
            return CONS_QUANTITY;
        }

        public void setCONS_QUANTITY(String CONS_QUANTITY) {
            this.CONS_QUANTITY = CONS_QUANTITY;
        }

        public String getCURRENT_WEIGHT() {
            return CURRENT_WEIGHT;
        }

        public void setCURRENT_WEIGHT(String CURRENT_WEIGHT) {
            this.CURRENT_WEIGHT = CURRENT_WEIGHT;
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

        public String getGREY_RATE() {
            return GREY_RATE;
        }

        public void setGREY_RATE(String GREY_RATE) {
            this.GREY_RATE = GREY_RATE;
        }

        public String getDYEING_CHARGE() {
            return DYEING_CHARGE;
        }

        public void setDYEING_CHARGE(String DYEING_CHARGE) {
            this.DYEING_CHARGE = DYEING_CHARGE;
        }

        public String getREPROCESS() {
            return REPROCESS;
        }

        public void setREPROCESS(String REPROCESS) {
            this.REPROCESS = REPROCESS;
        }

        public String getPREV_REPROCESS() {
            return PREV_REPROCESS;
        }

        public void setPREV_REPROCESS(String PREV_REPROCESS) {
            this.PREV_REPROCESS = PREV_REPROCESS;
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

        @SerializedName("REPROCESS")
        @Expose
        public String REPROCESS;

        @SerializedName("PREV_REPROCESS")
        @Expose
        public String PREV_REPROCESS;
        @Expose
        @SerializedName("INSERTED_BY")
        private String INSERTED_BY;

        @Expose
        @SerializedName("INSERT_DATE")
        private String INSERT_DATE;


    }
}

