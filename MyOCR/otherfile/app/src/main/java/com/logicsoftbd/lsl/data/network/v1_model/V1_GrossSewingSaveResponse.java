package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_GrossSewingSaveResponse {
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

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("update_id")
        @Expose
        private String updateId;
        @SerializedName("update_dtls_id")
        @Expose
        private String updateDtlsId;
        @SerializedName("update_dtls_piece_id")
        @Expose
        private String updateDtlsPieceId;
        @SerializedName("input_qnty")
        @Expose
        private String inputQnty;
        @SerializedName("country_id")
        @Expose
        private String country_id;
        @SerializedName("country_status")
        @Expose
        private Boolean country_status;
        @SerializedName("order_id")
        @Expose
        private String order_id;
        @SerializedName("order_status")
        @Expose
        private Boolean order_status;
        @SerializedName("color_wise_chk_qnty")
        @Expose
        private String colorWiseChkQnty;
        @SerializedName("ok")
        @Expose
        private String ok;
        @SerializedName("rej")
        @Expose
        private String rej;
        @SerializedName("alter")
        @Expose
        private String alter;
        @SerializedName("spot")
        @Expose
        private String spot;
        @SerializedName("rectified")
        @Expose
        private String rectified;
        @SerializedName("total_out_qnty")
        @Expose
        private String totalOutQnty;
        @SerializedName("output_qnty")
        @Expose
        private String outputQnty;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateId() {
            return updateId;
        }

        public void setUpdateId(String updateId) {
            this.updateId = updateId;
        }

        public String getUpdateDtlsId() {
            return updateDtlsId;
        }

        public void setUpdateDtlsId(String updateDtlsId) {
            this.updateDtlsId = updateDtlsId;
        }

        public String getUpdateDtlsPieceId() {
            return updateDtlsPieceId;
        }

        public void setUpdateDtlsPieceId(String updateDtlsPieceId) {
            this.updateDtlsPieceId = updateDtlsPieceId;
        }

        public String getInputQnty() {
            return inputQnty;
        }

        public void setInputQnty(String inputQnty) {
            this.inputQnty = inputQnty;
        }

        public String getColorWiseChkQnty() {
            return colorWiseChkQnty;
        }

        public void setColorWiseChkQnty(String colorWiseChkQnty) {
            this.colorWiseChkQnty = colorWiseChkQnty;
        }

        public String getOk() {
            return ok;
        }

        public void setOk(String ok) {
            this.ok = ok;
        }

        public String getRej() {
            return rej;
        }

        public void setRej(String rej) {
            this.rej = rej;
        }

        public String getAlter() {
            return alter;
        }

        public void setAlter(String alter) {
            this.alter = alter;
        }

        public String getSpot() {
            return spot;
        }

        public void setSpot(String spot) {
            this.spot = spot;
        }

        public String getRectified() {
            return rectified;
        }

        public void setRectified(String rectified) {
            this.rectified = rectified;
        }

        public String getTotalOutQnty() {
            return totalOutQnty;
        }

        public void setTotalOutQnty(String totalOutQnty) {
            this.totalOutQnty = totalOutQnty;
        }

        public String getOutputQnty() {
            return outputQnty;
        }

        public void setOutputQnty(String outputQnty) {
            this.outputQnty = outputQnty;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public boolean isCountry_status() {
            return country_status;
        }

        public void setCountry_status(boolean country_status) {
            this.country_status = country_status;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public Boolean getOrder_status() {
            return order_status;
        }

        public void setOrder_status(Boolean order_status) {
            this.order_status = order_status;
        }
    }
}
