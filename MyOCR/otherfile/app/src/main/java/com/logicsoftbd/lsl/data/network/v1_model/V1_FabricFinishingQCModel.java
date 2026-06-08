package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_FabricFinishingQCModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private ResultSet resultSet;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public class ResultSet {

        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("batch_id")
        @Expose
        private Integer batchId;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("color_id")
        @Expose
        private Integer colorId;
        @SerializedName("color_name")
        @Expose
        private String colorName;
        @SerializedName("booking_no")
        @Expose
        private String bookingNo;
        @SerializedName("sticker_type")
        @Expose
        private List<StickerType> stickerType;
        @SerializedName("dtls")
        @Expose
        private List<Dtl> dtls;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public Integer getBatchId() {
            return batchId;
        }

        public void setBatchId(Integer batchId) {
            this.batchId = batchId;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public Integer getColorId() {
            return colorId;
        }

        public void setColorId(Integer colorId) {
            this.colorId = colorId;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public List<StickerType> getStickerType() {
            return stickerType;
        }

        public void setStickerType(List<StickerType> stickerType) {
            this.stickerType = stickerType;
        }

        public List<Dtl> getDtls() {
            return dtls;
        }

        public void setDtls(List<Dtl> dtls) {
            this.dtls = dtls;
        }

    }

    public class Dtl {

        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("item_description")
        @Expose
        private String itemDescription;
        @SerializedName("prod_id")
        @Expose
        private Integer prodId;
        @SerializedName("job_id")
        @Expose
        private Integer jobId;
        @SerializedName("job_no")
        @Expose
        private String jobNo;
        @SerializedName("detar_id")
        @Expose
        private Integer detarId;
        @SerializedName("style_ref_no")
        @Expose
        private List<StyleRefNo> styleRefNo;

        public String getBuyerName() {
            return buyerName;
        }

        public void setBuyerName(String buyerName) {
            this.buyerName = buyerName;
        }

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public Integer getProdId() {
            return prodId;
        }

        public void setProdId(Integer prodId) {
            this.prodId = prodId;
        }

        public Integer getJobId() {
            return jobId;
        }

        public void setJobId(Integer jobId) {
            this.jobId = jobId;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public Integer getDetarId() {
            return detarId;
        }

        public void setDetarId(Integer detarId) {
            this.detarId = detarId;
        }

        public List<StyleRefNo> getStyleRefNo() {
            return styleRefNo;
        }

        public void setStyleRefNo(List<StyleRefNo> styleRefNo) {
            this.styleRefNo = styleRefNo;
        }

    }

    public class StickerType {

        @SerializedName("sticker_id")
        @Expose
        private Integer stickerId;
        @SerializedName("sticker_name")
        @Expose
        private String stickerName;

        public Integer getStickerId() {
            return stickerId;
        }

        public void setStickerId(Integer stickerId) {
            this.stickerId = stickerId;
        }

        public String getStickerName() {
            return stickerName;
        }

        public void setStickerName(String stickerName) {
            this.stickerName = stickerName;
        }

    }

    public class StyleRefNo {

        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

}