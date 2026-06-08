package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class V1_AOPBagKeepingResponse {
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

        @SerializedName("bag_status")
        @Expose
        private String bagStatus;
        @SerializedName("item_category")
        @Expose
        private String itemCategory;
        @SerializedName("bag_creation_id")
        @Expose
        private String bagCreationId;
        @SerializedName("bag_receive_mst_id")
        @Expose
        private String bagReceiveMstId;
        @SerializedName("system_no")
        @Expose
        private String systemNo;
        @SerializedName("sys_number_prefix_num")
        @Expose
        private String sysNumberPrefixNum;
        @SerializedName("bag_receive_details_id")
        @Expose
        private String bagReceiveDetailsId;
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
        private List<FabricType> fabricType;
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
        @SerializedName("issue_date")
        @Expose
        private String issue_date;
        @SerializedName("issue_no")
        @Expose
        private String issue_no;
        @SerializedName("iso_port")
        @Expose
        private String iso_port;
        private String aopWeight;
        private String solidWeight;
        private Boolean saveStatus;
        private String fabType;
        private String fabTypeName;
        private String bagColorName;
        private String bagColorId;
        private Boolean printingStatus;
        private String processLoss;

        public String getBagStatus() {
            return bagStatus;
        }

        public void setBagStatus(String bagStatus) {
            this.bagStatus = bagStatus;
        }

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        public String getBagCreationId() {
            return bagCreationId;
        }

        public void setBagCreationId(String bagCreationId) {
            this.bagCreationId = bagCreationId;
        }

        public String getProcessLoss() {
            return processLoss;
        }

        public void setProcessLoss(String processLoss) {
            this.processLoss = processLoss;
        }

        public String getBagReceiveMstId() {
            return bagReceiveMstId;
        }

        public void setBagReceiveMstId(String bagReceiveMstId) {
            this.bagReceiveMstId = bagReceiveMstId;
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

        public String getBagReceiveDetailsId() {
            return bagReceiveDetailsId;
        }

        public void setBagReceiveDetailsId(String bagReceiveDetailsId) {
            this.bagReceiveDetailsId = bagReceiveDetailsId;
        }

        public String getIssue_date() {
            return issue_date;
        }

        public void setIssue_date(String issue_date) {
            this.issue_date = issue_date;
        }

        public String getIssue_no() {
            return issue_no;
        }

        public void setIssue_no(String issue_no) {
            this.issue_no = issue_no;
        }

        public String getIso_port() {
            return iso_port;
        }

        public void setIso_port(String iso_port) {
            this.iso_port = iso_port;
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

        public List<FabricType> getFabricType() {
            return fabricType;
        }

        public void setFabricType(List<FabricType> fabricType) {
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

        public String getAopWeight() {
            return aopWeight;
        }

        public void setAopWeight(String aopWeight) {
            this.aopWeight = aopWeight;
        }

        public String getSolidWeight() {
            return solidWeight;
        }

        public void setSolidWeight(String solidWeight) {
            this.solidWeight = solidWeight;
        }

        public Boolean getSaveStatus() {
            return saveStatus;
        }

        public void setSaveStatus(Boolean saveStatus) {
            this.saveStatus = saveStatus;
        }

        public String getFabType() {
            return fabType;
        }

        public void setFabType(String fabType) {
            this.fabType = fabType;
        }

        public String getFabTypeName() {
            return fabTypeName;
        }

        public void setFabTypeName(String fabTypeName) {
            this.fabTypeName = fabTypeName;
        }

        public String getBagColorName() {
            return bagColorName;
        }

        public void setBagColorName(String bagColorName) {
            this.bagColorName = bagColorName;
        }

        public String getBagColorId() {
            return bagColorId;
        }

        public void setBagColorId(String bagColorId) {
            this.bagColorId = bagColorId;
        }

        public Boolean getPrintingStatus() {
            return printingStatus;
        }

        public void setPrintingStatus(Boolean printingStatus) {
            this.printingStatus = printingStatus;
        }
    }
    public class FabricType implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("gsm")
        @Expose
        private String gsm;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

    }
}
