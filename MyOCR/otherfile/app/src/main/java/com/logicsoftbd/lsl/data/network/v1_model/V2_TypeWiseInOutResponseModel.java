package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V2_TypeWiseInOutResponseModel {
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

    public class SizeQnty {

        @SerializedName("size_id")
        @Expose
        private String sizeId;
        @SerializedName("good")
        @Expose
        private String good;
        @SerializedName("alter")
        @Expose
        private String alter;
        @SerializedName("spot")
        @Expose
        private String spot;
        @SerializedName("reject")
        @Expose
        private String reject;
        @SerializedName("rectified")
        @Expose
        private String rectified;

        public String getSizeId() {
            return sizeId;
        }

        public void setSizeId(String sizeId) {
            this.sizeId = sizeId;
        }

        public String getGood() {
            return good;
        }

        public void setGood(String good) {
            this.good = good;
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

        public String getReject() {
            return reject;
        }

        public void setReject(String reject) {
            this.reject = reject;
        }

        public String getRectified() {
            return rectified;
        }

        public void setRectified(String rectified) {
            this.rectified = rectified;
        }

    }

    public class Resultset {

        @SerializedName("in_qnty")
        @Expose
        private String inQnty;
        @SerializedName("total_out_qnty")
        @Expose
        private String totalOutQnty;
        @SerializedName("out_qnty")
        @Expose
        private String outQnty;
        @SerializedName("size_qnty")
        @Expose
        private List<SizeQnty> sizeQnty;

        public String getInQnty() {
            return inQnty;
        }

        public void setInQnty(String inQnty) {
            this.inQnty = inQnty;
        }

        public String getTotalOutQnty() {
            return totalOutQnty;
        }

        public void setTotalOutQnty(String totalOutQnty) {
            this.totalOutQnty = totalOutQnty;
        }

        public String getOutQnty() {
            return outQnty;
        }

        public void setOutQnty(String outQnty) {
            this.outQnty = outQnty;
        }

        public List<SizeQnty> getSizeQnty() {
            return sizeQnty;
        }

        public void setSizeQnty(List<SizeQnty> sizeQnty) {
            this.sizeQnty = sizeQnty;
        }

    }
}
