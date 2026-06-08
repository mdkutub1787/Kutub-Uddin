package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_SewingInputClass {
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

        @SerializedName("bundle_no")
        @Expose
        private String bundleNo;
        @SerializedName("barcode_no")
        @Expose
        private Long barcodeNo;
        @SerializedName("year")
        @Expose
        private Integer year;
        @SerializedName("color_size_id")
        @Expose
        private Integer colorSizeId;
        @SerializedName("order_id")
        @Expose
        private Integer orderId;
        @SerializedName("item_id")
        @Expose
        private Integer itemId;
        @SerializedName("country_id")
        @Expose
        private Integer countryId;
        @SerializedName("size_id")
        @Expose
        private Integer sizeId;
        @SerializedName("color_id")
        @Expose
        private Integer colorId;
        @SerializedName("cut_no")
        @Expose
        private String cutNo;
        @SerializedName("job_no")
        @Expose
        private Integer jobNo;
        @SerializedName("buyer")
        @Expose
        private String buyer;
        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("item")
        @Expose
        private String item;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("color")
        @Expose
        private String color;
        @SerializedName("size")
        @Expose
        private String size;
        @SerializedName("qty")
        @Expose
        private Integer qty;
        @SerializedName("is_rescan")
        @Expose
        private Integer isRescan;
        @SerializedName("color_type_id")
        @Expose
        private Integer colorTypeId;
        @SerializedName("message_bng")
        @Expose
        private String messageBng;
        @SerializedName("message_eng")
        @Expose
        private String messageEng;

        public String getBundleNo() {
            return bundleNo;
        }

        public void setBundleNo(String bundleNo) {
            this.bundleNo = bundleNo;
        }

        public Long getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(Long barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getColorSizeId() {
            return colorSizeId;
        }

        public void setColorSizeId(Integer colorSizeId) {
            this.colorSizeId = colorSizeId;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public void setOrderId(Integer orderId) {
            this.orderId = orderId;
        }

        public Integer getItemId() {
            return itemId;
        }

        public void setItemId(Integer itemId) {
            this.itemId = itemId;
        }

        public Integer getCountryId() {
            return countryId;
        }

        public void setCountryId(Integer countryId) {
            this.countryId = countryId;
        }

        public Integer getSizeId() {
            return sizeId;
        }

        public void setSizeId(Integer sizeId) {
            this.sizeId = sizeId;
        }

        public Integer getColorId() {
            return colorId;
        }

        public void setColorId(Integer colorId) {
            this.colorId = colorId;
        }

        public String getCutNo() {
            return cutNo;
        }

        public void setCutNo(String cutNo) {
            this.cutNo = cutNo;
        }

        public Integer getJobNo() {
            return jobNo;
        }

        public void setJobNo(Integer jobNo) {
            this.jobNo = jobNo;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getItem() {
            return item;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public Integer getQty() {
            return qty;
        }

        public void setQty(Integer qty) {
            this.qty = qty;
        }

        public Integer getIsRescan() {
            return isRescan;
        }

        public void setIsRescan(Integer isRescan) {
            this.isRescan = isRescan;
        }

        public Integer getColorTypeId() {
            return colorTypeId;
        }

        public void setColorTypeId(Integer colorTypeId) {
            this.colorTypeId = colorTypeId;
        }

        public String getMessageBng() {
            return messageBng;
        }

        public void setMessageBng(String messageBng) {
            this.messageBng = messageBng;
        }

        public String getMessageEng() {
            return messageEng;
        }

        public void setMessageEng(String messageEng) {
            this.messageEng = messageEng;
        }

    }
}
