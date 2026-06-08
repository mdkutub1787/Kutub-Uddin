package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_FinishingQCModelResponse {
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

    public class StyleRefNo {

        @SerializedName("style_name")
        @Expose
        private String styleName;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("job_id")
        @Expose
        private String job_id;
        @SerializedName("job_no")
        @Expose
        private String job_no;

        public String getStyleName() {
            return styleName;
        }

        public void setStyleName(String styleName) {
            this.styleName = styleName;
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

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getJob_id() {
            return job_id;
        }

        public void setJob_id(String job_id) {
            this.job_id = job_id;
        }

        public String getJob_no() {
            return job_no;
        }

        public void setJob_no(String job_no) {
            this.job_no = job_no;
        }
    }

    public class StickerType {

        @SerializedName("sticker_id")
        @Expose
        private String stickerId;
        @SerializedName("sticker_name")
        @Expose
        private String stickerName;

        public String getStickerId() {
            return stickerId;
        }

        public void setStickerId(String stickerId) {
            this.stickerId = stickerId;
        }

        public String getStickerName() {
            return stickerName;
        }

        public void setStickerName(String stickerName) {
            this.stickerName = stickerName;
        }

    }
    public class ResultSet {

        @SerializedName("batch_id")
        @Expose
        private String batchId;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("color_id")
        @Expose
        private String colorId;
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
        @SerializedName("style_ref_no")
        @Expose
        private List<StyleRefNo> styleRefNo;

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

        public String getColorId() {
            return colorId;
        }

        public void setColorId(String colorId) {
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

        public List<StyleRefNo> getStyleRefNo() {
            return styleRefNo;
        }

        public void setStyleRefNo(List<StyleRefNo> styleRefNo) {
            this.styleRefNo = styleRefNo;
        }

    }
    public class Dtl {

        @SerializedName("item_description")
        @Expose
        private String itemDescription;
        @SerializedName("fabric_type")
        @Expose
        private String fabricType;
        @SerializedName("fabric_composition")
        @Expose
        private String fabricComposition;
        @SerializedName("prod_id")
        @Expose
        private String prodId;
        @SerializedName("detar_id")
        @Expose
        private String detarId;
        @SerializedName("booking_gsm")
        @Expose
        private String booking_gsm;
        @SerializedName("booking_dia")
        @Expose
        private String booking_dia;


        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getFabricType() {
            return fabricType;
        }

        public void setFabricType(String fabricType) {
            this.fabricType = fabricType;
        }

        public String getFabricComposition() {
            return fabricComposition;
        }

        public void setFabricComposition(String fabricComposition) {
            this.fabricComposition = fabricComposition;
        }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getDetarId() {
            return detarId;
        }

        public void setDetarId(String detarId) {
            this.detarId = detarId;
        }

        public String getBooking_gsm() {
            return booking_gsm;
        }

        public void setBooking_gsm(String booking_gsm) {
            this.booking_gsm = booking_gsm;
        }

        public String getBooking_dia() {
            return booking_dia;
        }

        public void setBooking_dia(String booking_dia) {
            this.booking_dia = booking_dia;
        }
    }

}
