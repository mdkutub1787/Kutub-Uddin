package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_ColorBreakDownResponse {
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

        @SerializedName("in_qnty")
        @Expose
        private String inQnty;
        @SerializedName("total_out_qnty")
        @Expose
        private String total_out_qnty;
        @SerializedName("out_qnty")
        @Expose
        private String outQnty;
        @SerializedName("good")
        @Expose
        private String good;
        @SerializedName("reject")
        @Expose
        private String reject;
        @SerializedName("alter")
        @Expose
        private String alter;
        @SerializedName("spot")
        @Expose
        private String spot;
        @SerializedName("rectified")
        @Expose
        private String rectified;

        public String getInQnty() {
            return inQnty;
        }

        public void setInQnty(String inQnty) {
            this.inQnty = inQnty;
        }

        public String getOutQnty() {
            return outQnty;
        }

        public void setOutQnty(String outQnty) {
            this.outQnty = outQnty;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
        }

        public String getReject() {
            return reject;
        }

        public void setReject(String reject) {
            this.reject = reject;
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

        public String getTotal_out_qnty() {
            return total_out_qnty;
        }

        public void setTotal_out_qnty(String total_out_qnty) {
            this.total_out_qnty = total_out_qnty;
        }
    }
}
