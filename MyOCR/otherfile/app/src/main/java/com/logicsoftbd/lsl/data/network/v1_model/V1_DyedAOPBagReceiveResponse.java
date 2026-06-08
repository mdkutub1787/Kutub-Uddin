package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class V1_DyedAOPBagReceiveResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private ResultSet resultSet;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class ResultSet implements Serializable {

        @SerializedName("bag_keeping_mst_id")
        @Expose
        private String bagKeepingMstId;
        @SerializedName("system_no")
        @Expose
        private String systemNo;
        @SerializedName("sys_number_prefix_num")
        @Expose
        private String sysNumberPrefixNum;
        @SerializedName("bag_keeping_details_id")
        @Expose
        private String bagKeepingDetailsId;
        @SerializedName("bag_no")
        @Expose
        private String bagNo;
        @SerializedName("rfid_no")
        @Expose
        private String rfidNo;
        @SerializedName("qr_no")
        @Expose
        private String qrNo;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("internal_ref")
        @Expose
        private String internalRef;
        @SerializedName("fab_color_id")
        @Expose
        private String fabColorId;
        @SerializedName("uom")
        @Expose
        private String uom;
        @SerializedName("fab_color_name")
        @Expose
        private String fabColorName;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("roll_qnty")
        @Expose
        private String rollQnty;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("dia")
        @Expose
        private String dia;
        @SerializedName("gsm")
        @Expose
        private String gsm;
        @SerializedName("fabric_type")
        @Expose
        private String fabricType;
        @SerializedName("aop")
        @Expose
        private String aop;
        @SerializedName("reject")
        @Expose
        private String reject;
        @SerializedName("qc_done")
        @Expose
        private String qcDone;
        @SerializedName("search_type")
        @Expose
        private String searchType;
        @SerializedName("entry_form")
        @Expose
        private String entryForm;
        @SerializedName("store_id")
        @Expose
        private String storeId;
        @SerializedName("store_name")
        @Expose
        private String storeName;

        public String getBagKeepingMstId() {
            return bagKeepingMstId;
        }

        public void setBagKeepingMstId(String bagKeepingMstId) {
            this.bagKeepingMstId = bagKeepingMstId;
        }

        public String getSystemNo() {
            return systemNo;
        }

        public void setSystemNo(String systemNo) {
            this.systemNo = systemNo;
        }

        public String getSysNumberPrefixNum() {
            return sysNumberPrefixNum;
        }

        public void setSysNumberPrefixNum(String sysNumberPrefixNum) {
            this.sysNumberPrefixNum = sysNumberPrefixNum;
        }

        public String getBagKeepingDetailsId() {
            return bagKeepingDetailsId;
        }

        public void setBagKeepingDetailsId(String bagKeepingDetailsId) {
            this.bagKeepingDetailsId = bagKeepingDetailsId;
        }

        public String getBagNo() {
            return bagNo;
        }

        public void setBagNo(String bagNo) {
            this.bagNo = bagNo;
        }

        public String getRfidNo() {
            return rfidNo;
        }

        public void setRfidNo(String rfidNo) {
            this.rfidNo = rfidNo;
        }

        public String getQrNo() {
            return qrNo;
        }

        public void setQrNo(String qrNo) {
            this.qrNo = qrNo;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getInternalRef() {
            return internalRef;
        }

        public void setInternalRef(String internalRef) {
            this.internalRef = internalRef;
        }

        public String getFabColorId() {
            return fabColorId;
        }

        public void setFabColorId(String fabColorId) {
            this.fabColorId = fabColorId;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }

        public String getFabColorName() {
            return fabColorName;
        }

        public void setFabColorName(String fabColorName) {
            this.fabColorName = fabColorName;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getRollQnty() {
            return rollQnty;
        }

        public void setRollQnty(String rollQnty) {
            this.rollQnty = rollQnty;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getDia() {
            return dia;
        }

        public void setDia(String dia) {
            this.dia = dia;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

        public String getFabricType() {
            return fabricType;
        }

        public void setFabricType(String fabricType) {
            this.fabricType = fabricType;
        }

        public String getAop() {
            return aop;
        }

        public void setAop(String aop) {
            this.aop = aop;
        }

        public String getReject() {
            return reject;
        }

        public void setReject(String reject) {
            this.reject = reject;
        }

        public String getQcDone() {
            return qcDone;
        }

        public void setQcDone(String qcDone) {
            this.qcDone = qcDone;
        }

        public String getSearchType() {
            return searchType;
        }

        public void setSearchType(String searchType) {
            this.searchType = searchType;
        }

        public String getEntryForm() {
            return entryForm;
        }

        public void setEntryForm(String entryForm) {
            this.entryForm = entryForm;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

    }
}
