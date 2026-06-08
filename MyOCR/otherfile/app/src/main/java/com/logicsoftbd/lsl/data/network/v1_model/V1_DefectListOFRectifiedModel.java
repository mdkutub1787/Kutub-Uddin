package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_DefectListOFRectifiedModel {
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

        @SerializedName("MST_ID")
        @Expose
        private String mstId;
        @SerializedName("DTLS_ID")
        @Expose
        private String dtlsId;
        @SerializedName("COUNTRY_ID")
        @Expose
        private String countryId;
        @SerializedName("OPERATION_NAME")
        @Expose
        private String operationName;
        @SerializedName("ALTER_QTY")
        @Expose
        private String alterQty;
        @SerializedName("SPOT_QTY")
        @Expose
        private String spotQty;
        @SerializedName("PRODUCTION_DATE")
        @Expose
        private String productionDate;
        @SerializedName("COL_SIZE_ID")
        @Expose
        private String COL_SIZE_ID;
        @SerializedName("DEFECT_NAMES")
        @Expose
        private String defectNames;
        @SerializedName("COLOR_NAME")
        @Expose
        private String colorName;
        @SerializedName("SIZE_NAME")
        @Expose
        private String sizeName;

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

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getCOL_SIZE_ID() {
            return COL_SIZE_ID;
        }

        public void setCOL_SIZE_ID(String COL_SIZE_ID) {
            this.COL_SIZE_ID = COL_SIZE_ID;
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
