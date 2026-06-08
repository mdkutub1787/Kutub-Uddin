package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class V1_BagKeepingDataBySystemResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("result_set")
    @Expose
    private ArrayList<ResultSet> resultSet;
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

    public ArrayList<ResultSet> getResultSet() {
        return resultSet;
    }

    public void setResultSet(ArrayList<ResultSet> resultSet) {
        this.resultSet = resultSet;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public class ResultSet implements Serializable {

        @SerializedName("system_no")
        @Expose
        private String systemNo;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("bag_no")
        @Expose
        private String bagNo;
        @SerializedName("qr_no")
        @Expose
        private String qrNo;
        @SerializedName("rfid_no")
        @Expose
        private String rfidNo;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("grey_weight")
        @Expose
        private String greyWeight;
        @SerializedName("ir/ib")
        @Expose
        private String irIb;
        @SerializedName("fab_color_name")
        @Expose
        private String fabColorName;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("reject")
        @Expose
        private String reject;
        @SerializedName("qc_done")
        @Expose
        private String qcDone;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("fabric_type_name")
        @Expose
        private String fabricTypeName;
        @SerializedName("gsm")
        @Expose
        private String gsm;
        @SerializedName("dia")
        @Expose
        private String dia;
        @SerializedName("issue_date")
        @Expose
        private String issue_date;
        @SerializedName("issue_no")
        @Expose
        private String issue_no;
        @SerializedName("iso_port")
        @Expose
        private String iso_port;
        private String bagColorId;
        private String isRejecting;

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

        public String getSystemNo() {
            return systemNo;
        }

        public void setSystemNo(String systemNo) {
            this.systemNo = systemNo;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
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

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getGreyWeight() {
            return greyWeight;
        }

        public void setGreyWeight(String greyWeight) {
            this.greyWeight = greyWeight;
        }

        public String getIrIb() {
            return irIb;
        }

        public void setIrIb(String irIb) {
            this.irIb = irIb;
        }

        public String getFabColorName() {
            return fabColorName;
        }

        public void setFabColorName(String fabColorName) {
            this.fabColorName = fabColorName;
        }

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
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

        public String getBagColorId() {
            return bagColorId;
        }

        public void setBagColorId(String bagColorId) {
            this.bagColorId = bagColorId;
        }

        public String getIsRejecting() {
            return isRejecting;
        }

        public void setIsRejecting(String isRejecting) {
            this.isRejecting = isRejecting;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getFabricTypeName() {
            return fabricTypeName;
        }

        public void setFabricTypeName(String fabricTypeName) {
            this.fabricTypeName = fabricTypeName;
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
    }
}
