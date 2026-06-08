package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class V1_BagPrintResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("result_set")
    @Expose
    private List<ResultSet> resultSet;
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

    public List<ResultSet> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<ResultSet> resultSet) {
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
        @SerializedName("qr_no")
        @Expose
        private String qrNo;
        @SerializedName("rfid_no")
        @Expose
        private String rfidNo;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("booking_no")
        @Expose
        private String bookingNo;
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
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("fabric_type")
        @Expose
        private String fabricType;
        @SerializedName("aop")
        @Expose
        private String aop;
        @SerializedName("issue_date")
        @Expose
        private String issueDate;
        @SerializedName("issue_no")
        @Expose
        private String issueNo;
        @SerializedName("iso_port")
        @Expose
        private String isoPort;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("dia")
        @Expose
        private String dia;
        @SerializedName("gsm")
        @Expose
        private String gsm;
        @SerializedName("fabric_type_name")
        @Expose
        private String fabricTypeName;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("Grey_weight")
        @Expose
        private String greyWeight;
        @SerializedName("finish_weight")
        @Expose
        private String finishWeight;
        @SerializedName("packing_list")
        @Expose
        private String packingList;

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

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
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

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
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

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getIssueNo() {
            return issueNo;
        }

        public void setIssueNo(String issueNo) {
            this.issueNo = issueNo;
        }

        public String getIsoPort() {
            return isoPort;
        }

        public void setIsoPort(String isoPort) {
            this.isoPort = isoPort;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
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

        public String getFabricTypeName() {
            return fabricTypeName;
        }

        public void setFabricTypeName(String fabricTypeName) {
            this.fabricTypeName = fabricTypeName;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getGreyWeight() {
            return greyWeight;
        }

        public void setGreyWeight(String greyWeight) {
            this.greyWeight = greyWeight;
        }

        public String getFinishWeight() {
            return finishWeight;
        }

        public void setFinishWeight(String finishWeight) {
            this.finishWeight = finishWeight;
        }

        public String getPackingList() {
            return packingList;
        }

        public void setPackingList(String packingList) {
            this.packingList = packingList;
        }

    }
}
