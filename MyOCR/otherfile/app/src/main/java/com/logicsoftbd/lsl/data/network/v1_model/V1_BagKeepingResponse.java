package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class V1_BagKeepingResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("result_set")
    @Expose
    private ResultSet resultSet;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class ResultSet implements Serializable {

        @SerializedName("bag_no")
        @Expose
        private String bagNo;
        @SerializedName("booking_no")
        @Expose
        private String booking_no;
        @SerializedName("company_name")
        @Expose
        private String company_name;
        @SerializedName("qr_no")
        @Expose
        private String qrNo;
        @SerializedName("rfid_no")
        @Expose
        private String rfidNo;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("roll_qnty")
        @Expose
        private String rollQnty;
        @SerializedName("ir/ib")
        @Expose
        private String irIb;
        @SerializedName("fab_color_id")
        @Expose
        private String fabColorId;
        @SerializedName("fab_color_name")
        @Expose
        private String fabColorName;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("issue_date")
        @Expose
        private String issue_date;
        @SerializedName("issue_no")
        @Expose
        private String issue_no;
        @SerializedName("iso_port")
        @Expose
        private String iso_port;
        @SerializedName("location_id")
        @Expose
        private String location_id;
        @SerializedName("location_name")
        @Expose
        private String location_name;
        private String finishWeight;
        private String greyWeight;
        private String gsm;
        private String dia;
        private Boolean saveStatus;
        private String fabType;
        private String fabTypeName;
        private String bagColorName;
        private String bagColorId;
        private Boolean printingStatus;
        @SerializedName("fabric_type")
        @Expose
        private List<FabricType> fabricType;

        @SerializedName("aop")
        @Expose
        private String aop;


        public String getBooking_no() {
            return booking_no;
        }

        public void setBooking_no(String booking_no) {
            this.booking_no = booking_no;
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

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getBagNo() {
            return bagNo;
        }

        public void setBagNo(String bagNo) {
            this.bagNo = bagNo;
        }

        public String getQrNo() {
            return qrNo;
        }

        public void setQrNo(String qrNo) {
            this.qrNo = qrNo;
        }

        public String getRfidNo() {
            return rfidNo;
        }

        public void setRfidNo(String rfidNo) {
            this.rfidNo = rfidNo;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getRollQnty() {
            return rollQnty;
        }

        public void setRollQnty(String rollQnty) {
            this.rollQnty = rollQnty;
        }

        public String getIrIb() {
            return irIb;
        }

        public void setIrIb(String irIb) {
            this.irIb = irIb;
        }

        public String getFabColorId() {
            return fabColorId;
        }

        public void setFabColorId(String fabColorId) {
            this.fabColorId = fabColorId;
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

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getFinishWeight() {
            return finishWeight;
        }

        public void setFinishWeight(String finishWeight) {
            this.finishWeight = finishWeight;
        }

        public String getGreyWeight() {
            return greyWeight;
        }

        public void setGreyWeight(String greyWeight) {
            this.greyWeight = greyWeight;
        }

        public String getGsm() {
            return gsm;
        }

        public void setGsm(String gsm) {
            this.gsm = gsm;
        }

        public String getDia() {
            return dia;
        }

        public void setDia(String dia) {
            this.dia = dia;
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

        public Boolean getPrintingStatus() {
            return printingStatus;
        }

        public void setPrintingStatus(Boolean printingStatus) {
            this.printingStatus = printingStatus;
        }

        public String getAop() {
            return aop;
        }

        public void setAop(String aop) {
            this.aop = aop;
        }

        public List<FabricType> getFabricType() {
            return fabricType;
        }

        public void setFabricType(List<FabricType> fabricType) {
            this.fabricType = fabricType;
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

        public String getLocation_id() {
            return location_id;
        }

        public void setLocation_id(String location_id) {
            this.location_id = location_id;
        }

        public String getLocation_name() {
            return location_name;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
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
