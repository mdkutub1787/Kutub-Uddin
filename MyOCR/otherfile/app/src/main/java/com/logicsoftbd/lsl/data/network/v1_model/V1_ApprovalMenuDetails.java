package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ApprovalMenuDetails {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class ApproveDatum {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DATE")
        @Expose
        private String date;
        @SerializedName("DELIVERY_DATE")
        @Expose
        private String deliveryDate;
        @SerializedName("COMPANY")
        @Expose
        private String company;
        @SerializedName("BUYER")
        @Expose
        private String buyer;
        @SerializedName("SYS_NUMBER")
        @Expose
        private String sysNumber;
        @SerializedName("SYS_DEF")
        @Expose
        private String sysDef;
        @SerializedName("DESC")
        @Expose
        private String desc;
        @SerializedName("IS_SEEN")
        @Expose
        private String isSeen;
        @SerializedName("INSERT_DATE")
        @Expose
        private String insertDate;
        @SerializedName("APPROVED_DATE")
        @Expose
        private String approvedDate;
        @SerializedName("APPROVED_BY")
        @Expose
        private String approvedBy;
        @SerializedName("UNAPPROVED")
        @Expose
        private Boolean markedItem;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getSysNumber() {
            return sysNumber;
        }

        public void setSysNumber(String sysNumber) {
            this.sysNumber = sysNumber;
        }

        public String getSysDef() {
            return sysDef;
        }

        public void setSysDef(String sysDef) {
            this.sysDef = sysDef;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIsSeen() {
            return isSeen;
        }

        public void setIsSeen(String isSeen) {
            this.isSeen = isSeen;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(String insertDate) {
            this.insertDate = insertDate;
        }

        public String getApprovedDate() {
            return approvedDate;
        }

        public void setApprovedDate(String approvedDate) {
            this.approvedDate = approvedDate;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public Boolean getMarkedItem() {
            return markedItem;
        }

        public void setMarkedItem(Boolean markedItem) {
            this.markedItem = markedItem;
        }
    }
    public class Data {

        @SerializedName("approve_data")
        @Expose
        private List<ApproveDatum> approveData;
        @SerializedName("unapprove_data")
        @Expose
        private List<UnapproveDatum> unapproveData;

        public List<ApproveDatum> getApproveData() {
            return approveData;
        }

        public void setApproveData(List<ApproveDatum> approveData) {
            this.approveData = approveData;
        }

        public List<UnapproveDatum> getUnapproveData() {
            return unapproveData;
        }

        public void setUnapproveData(List<UnapproveDatum> unapproveData) {
            this.unapproveData = unapproveData;
        }

    }
    public class UnapproveDatum {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("DATE")
        @Expose
        private String date;
        @SerializedName("DELIVERY_DATE")
        @Expose
        private String deliveryDate;
        @SerializedName("COMPANY")
        @Expose
        private String company;
        @SerializedName("BUYER")
        @Expose
        private String buyer;
        @SerializedName("SYS_NUMBER")
        @Expose
        private String sysNumber;
        @SerializedName("SYS_DEF")
        @Expose
        private String sysDef;
        @SerializedName("DESC")
        @Expose
        private String desc;
        @SerializedName("IS_SEEN")
        @Expose
        private String isSeen;
        @SerializedName("INSERT_DATE")
        @Expose
        private String insertDate;
        @SerializedName("APPROVED_DATE")
        @Expose
        private String approvedDate;
        @SerializedName("APPROVED_BY")
        @Expose
        private String approvedBy;
        @SerializedName("UNAPPROVED")
        @Expose
        private Boolean markedItem;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getSysNumber() {
            return sysNumber;
        }

        public void setSysNumber(String sysNumber) {
            this.sysNumber = sysNumber;
        }

        public String getSysDef() {
            return sysDef;
        }

        public void setSysDef(String sysDef) {
            this.sysDef = sysDef;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getIsSeen() {
            return isSeen;
        }

        public void setIsSeen(String isSeen) {
            this.isSeen = isSeen;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(String insertDate) {
            this.insertDate = insertDate;
        }

        public String getApprovedDate() {
            return approvedDate;
        }

        public void setApprovedDate(String approvedDate) {
            this.approvedDate = approvedDate;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public Boolean getMarkedItem() {
            return markedItem;
        }

        public void setMarkedItem(Boolean markedItem) {
            this.markedItem = markedItem;
        }
    }
}
