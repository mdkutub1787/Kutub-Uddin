package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_FabricFinishQCUpdateModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
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
    public class ResultSet {

        @SerializedName("ID")
        @Expose
        private String id;
        @SerializedName("STICKER_TYPE")
        @Expose
        private String stickerType;
        @SerializedName("BATCH_ID")
        @Expose
        private String batchId;
        @SerializedName("BATCH_NO")
        @Expose
        private String batchNo;
        @SerializedName("JOB_ID")
        @Expose
        private String jobId;
        @SerializedName("STYLE_REF_NO")
        @Expose
        private String styleRefNo;
        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("ROLL_NO")
        @Expose
        private String rollNo;
        @SerializedName("GSM")
        @Expose
        private String gsm;
        @SerializedName("DIA")
        @Expose
        private String dia;
        @SerializedName("BOOKING_GSM")
        @Expose
        private String bookingGsm;
        @SerializedName("BOOKING_DIA")
        @Expose
        private String bookingDia;
        @SerializedName("MACHINE_ID")
        @Expose
        private String machineId;
        @SerializedName("DETERMINATION_ID")
        @Expose
        private String determinationId;
        @SerializedName("FAB_DESC")
        @Expose
        private String fabDesc;
        @SerializedName("fab_weight")
        @Expose
        private String fabWeight;
        @SerializedName("FAB_COLOR_ID")
        @Expose
        private String fabColorId;
        @SerializedName("FAB_COLOR")
        @Expose
        private String fabColor;
        @SerializedName("GREY_USED")
        @Expose
        private String greyUsed;
        @SerializedName("COMMENTS")
        @Expose
        private String comments;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStickerType() {
            return stickerType;
        }

        public void setStickerType(String stickerType) {
            this.stickerType = stickerType;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public String getStyleRefNo() {
            return styleRefNo;
        }

        public void setStyleRefNo(String styleRefNo) {
            this.styleRefNo = styleRefNo;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
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

        public String getBookingGsm() {
            return bookingGsm;
        }

        public void setBookingGsm(String bookingGsm) {
            this.bookingGsm = bookingGsm;
        }

        public String getBookingDia() {
            return bookingDia;
        }

        public void setBookingDia(String bookingDia) {
            this.bookingDia = bookingDia;
        }

        public String getMachineId() {
            return machineId;
        }

        public void setMachineId(String machineId) {
            this.machineId = machineId;
        }

        public String getDeterminationId() {
            return determinationId;
        }

        public void setDeterminationId(String determinationId) {
            this.determinationId = determinationId;
        }

        public String getFabDesc() {
            return fabDesc;
        }

        public void setFabDesc(String fabDesc) {
            this.fabDesc = fabDesc;
        }

        public String getFabColorId() {
            return fabColorId;
        }

        public void setFabColorId(String fabColorId) {
            this.fabColorId = fabColorId;
        }

        public String getFabColor() {
            return fabColor;
        }

        public void setFabColor(String fabColor) {
            this.fabColor = fabColor;
        }

        public String getGreyUsed() {
            return greyUsed;
        }

        public void setGreyUsed(String greyUsed) {
            this.greyUsed = greyUsed;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getFabWeight() {
            return fabWeight;
        }

        public void setFabWeight(String fabWeight) {
            this.fabWeight = fabWeight;
        }
    }

}


