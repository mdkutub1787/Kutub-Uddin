package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlitteringSequzFunctionalbatchResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private Resultset resultset;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Resultset getResultset() {
        return resultset;
    }

    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }
    public class Resultset {

        @SerializedName("dtls_index")
        @Expose
        private List<DtlsIndex> dtlsIndex = null;

        public List<DtlsIndex> getDtlsIndex() {
            return dtlsIndex;
        }

        public void setDtlsIndex(List<DtlsIndex> dtlsIndex) {
            this.dtlsIndex = dtlsIndex;
        }

    }
    public class DtlsIndex {

        @SerializedName("mst_id")
        @Expose
        private String mstId;
        @SerializedName("roll_no")
        @Expose
        private String rollNo;
        @SerializedName("const_composition")
        @Expose
        private String constComposition;
        @SerializedName("dia_width")
        @Expose
        private String diaWidth;
        @SerializedName("width_dia_type")
        @Expose
        private String widthDiaType;
        @SerializedName("barcode")
        @Expose
        private String barcode;
        @SerializedName("batch_qty")
        @Expose
        private String batchQty;
        @SerializedName("prod_qty")
        @Expose
        private String prodQty;

        private Boolean barcode_status;

        public String getMstId() {
            return mstId;
        }

        public void setMstId(String mstId) {
            this.mstId = mstId;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getConstComposition() {
            return constComposition;
        }

        public void setConstComposition(String constComposition) {
            this.constComposition = constComposition;
        }

        public String getDiaWidth() {
            return diaWidth;
        }

        public void setDiaWidth(String diaWidth) {
            this.diaWidth = diaWidth;
        }

        public String getWidthDiaType() {
            return widthDiaType;
        }

        public void setWidthDiaType(String widthDiaType) {
            this.widthDiaType = widthDiaType;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getBatchQty() {
            return batchQty;
        }

        public void setBatchQty(String batchQty) {
            this.batchQty = batchQty;
        }

        public String getProdQty() {
            return prodQty;
        }

        public void setProdQty(String prodQty) {
            this.prodQty = prodQty;
        }

        public Boolean getBarcode_status() {
            return barcode_status;
        }

        public void setBarcode_status(Boolean barcode_status) {
            this.barcode_status = barcode_status;
        }

    }
}