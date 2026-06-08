package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_DefectListOFRectifiedModel_V4 {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }
    public class Datum {

        @SerializedName("PO_BREAK_DOWN_ID")
        @Expose
        private String poBreakDownId;
        @SerializedName("PO_NUMBER")
        @Expose
        private String poNumber;
        @SerializedName("MST_ID")
        @Expose
        private String mstId;
        @SerializedName("DTLS_ID")
        @Expose
        private String dtlsId;
        @SerializedName("OPERATION_NAME")
        @Expose
        private String operationName;
        @SerializedName("ALTER_QTY")
        @Expose
        private String alterQty;
        @SerializedName("SPOT_QTY")
        @Expose
        private String spotQty;
        @SerializedName("RECTIFIED_QTY")
        @Expose
        private String rectifiedQty;
        @SerializedName("PRODUCTION_DATE")
        @Expose
        private String productionDate;
        @SerializedName("COL_SIZE_ID")
        @Expose
        private String colSizeId;
        @SerializedName("DEFECT_NAMES")
        @Expose
        private String defectNames;
        @SerializedName("COLOR_NAME")
        @Expose
        private String colorName;
        @SerializedName("SIZE_NAME")
        @Expose
        private String sizeName;

        public String getPoBreakDownId() {
            return poBreakDownId;
        }

        public void setPoBreakDownId(String poBreakDownId) {
            this.poBreakDownId = poBreakDownId;
        }

        public String getPoNumber() {
            return poNumber;
        }

        public void setPoNumber(String poNumber) {
            this.poNumber = poNumber;
        }

        public String getMstId() {
            return mstId;
        }

        public void setMstId(String mstId) {
            this.mstId = mstId;
        }

        public String getDtlsId() {
            return dtlsId;
        }

        public void setDtlsId(String dtlsId) {
            this.dtlsId = dtlsId;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

        public String getAlterQty() {
            return alterQty;
        }

        public void setAlterQty(String alterQty) {
            this.alterQty = alterQty;
        }

        public String getSpotQty() {
            return spotQty;
        }

        public void setSpotQty(String spotQty) {
            this.spotQty = spotQty;
        }

        public String getRectifiedQty() {
            return rectifiedQty;
        }

        public void setRectifiedQty(String rectifiedQty) {
            this.rectifiedQty = rectifiedQty;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getColSizeId() {
            return colSizeId;
        }

        public void setColSizeId(String colSizeId) {
            this.colSizeId = colSizeId;
        }

        public String getDefectNames() {
            return defectNames;
        }

        public void setDefectNames(String defectNames) {
            this.defectNames = defectNames;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getSizeName() {
            return sizeName;
        }

        public void setSizeName(String sizeName) {
            this.sizeName = sizeName;
        }

    }
}
