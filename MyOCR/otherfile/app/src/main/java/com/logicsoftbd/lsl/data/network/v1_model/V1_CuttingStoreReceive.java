package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_CuttingStoreReceive {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("msg")
    @Expose
    private String msg;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class Data {

        @SerializedName("BUNDLE_NO")
        @Expose
        private String bundleNo;
        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("PO_BREAKDOWN_ID")
        @Expose
        private String poBreakdownId;
        @SerializedName("PO_NUMBER")
        @Expose
        private String poNumber;
        @SerializedName("BUYER_ID")
        @Expose
        private String buyerId;
        @SerializedName("BUYER_NAME")
        @Expose
        private String buyerName;
        @SerializedName("QC_PASS_QNTY")
        @Expose
        private String qcPassQnty;
        @SerializedName("COMPANY_ID")
        @Expose
        private String companyId;
        @SerializedName("COMPANY_NAME")
        @Expose
        private String companyName;
        @SerializedName("SIZE_ID")
        @Expose
        private String sizeId;
        @SerializedName("SIZE_NAME")
        @Expose
        private String sizeName;
        @SerializedName("COLOR_NUMBER_ID")
        @Expose
        private String colorNumberId;
        @SerializedName("COLOR_NAME")
        @Expose
        private String colorName;
        @SerializedName("CUTTING_FLOOR_ID")
        @Expose
        private String cuttingFloorId;
        @SerializedName("CUTTING_FLOOR_NAME")
        @Expose
        private String cuttingFloorName;
        @SerializedName("PRODUCTION_QNTY")
        @Expose
        private String productionQnty;
        @SerializedName("PUB_MSG")
        @Expose
        private String pubMsg;
        @SerializedName("SUCCESS_STATUS")
        @Expose
        private String success_status;

        public String getBundleNo() {
            return bundleNo;
        }

        public void setBundleNo(String bundleNo) {
            this.bundleNo = bundleNo;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getPoBreakdownId() {
            return poBreakdownId;
        }

        public void setPoBreakdownId(String poBreakdownId) {
            this.poBreakdownId = poBreakdownId;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
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

        public String getQcPassQnty() {
            return qcPassQnty;
        }

        public void setQcPassQnty(String qcPassQnty) {
            this.qcPassQnty = qcPassQnty;
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

        public String getSizeId() {
            return sizeId;
        }

        public void setSizeId(String sizeId) {
            this.sizeId = sizeId;
        }

        public String getSizeName() {
            return sizeName;
        }

        public void setSizeName(String sizeName) {
            this.sizeName = sizeName;
        }

        public String getColorNumberId() {
            return colorNumberId;
        }

        public void setColorNumberId(String colorNumberId) {
            this.colorNumberId = colorNumberId;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getCuttingFloorId() {
            return cuttingFloorId;
        }

        public void setCuttingFloorId(String cuttingFloorId) {
            this.cuttingFloorId = cuttingFloorId;
        }

        public String getCuttingFloorName() {
            return cuttingFloorName;
        }

        public void setCuttingFloorName(String cuttingFloorName) {
            this.cuttingFloorName = cuttingFloorName;
        }

        public String getProductionQnty() {
            return productionQnty;
        }

        public void setProductionQnty(String productionQnty) {
            this.productionQnty = productionQnty;
        }

        public String getPubMsg() {
            return pubMsg;
        }

        public void setPubMsg(String pubMsg) {
            this.pubMsg = pubMsg;
        }

        public String getSuccess_status() {
            return success_status;
        }

        public void setSuccess_status(String success_status) {
            this.success_status = success_status;
        }
    }
}
