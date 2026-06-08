package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_BundeWiseSewingInputPCSResponse {
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

        @SerializedName("message_bng")
        @Expose
        private String messageBng;
        @SerializedName("message_eng")
        @Expose
        private String messageEng;
        @SerializedName("bundle_no")
        @Expose
        private String bundleNo;
        @SerializedName("barcode_no")
        @Expose
        private String barcodeNo;
        @SerializedName("year")
        @Expose
        private String year;
        @SerializedName("color_size_id")
        @Expose
        private String colorSizeId;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("country_id")
        @Expose
        private String countryId;
        @SerializedName("size_id")
        @Expose
        private String sizeId;
        @SerializedName("color_id")
        @Expose
        private String colorId;
        @SerializedName("cut_no")
        @Expose
        private String cutNo;
        @SerializedName("job_no")
        @Expose
        private String jobNo;
        @SerializedName("order_no")
        @Expose
        private String orderNo;
        @SerializedName("int_ref")
        @Expose
        private String intRef;
        @SerializedName("is_rescan")
        @Expose
        private String isRescan;
        @SerializedName("scanned_qty")
        @Expose
        private String scannedQty;
        @SerializedName("qty")
        @Expose
        private String qty;
        @SerializedName("sew_in_qty")
        @Expose
        private String SewingInQty;
        @SerializedName("buyer")
        @Expose
        private String buyer;
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
        @SerializedName("color_type_id")
        @Expose
        private String colorTypeId;
        @SerializedName("sewing_input_line")
        @Expose
        private String sewingInputLine;
        @SerializedName("replace_field_disable")
        @Expose
        private String replaceFieldDisable;

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

        public String getBundleNo() {
            return bundleNo;
        }

        public void setBundleNo(String bundleNo) {
            this.bundleNo = bundleNo;
        }

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getColorSizeId() {
            return colorSizeId;
        }

        public void setColorSizeId(String colorSizeId) {
            this.colorSizeId = colorSizeId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getSizeId() {
            return sizeId;
        }

        public void setSizeId(String sizeId) {
            this.sizeId = sizeId;
        }

        public String getColorId() {
            return colorId;
        }

        public void setColorId(String colorId) {
            this.colorId = colorId;
        }

        public String getCutNo() {
            return cutNo;
        }

        public void setCutNo(String cutNo) {
            this.cutNo = cutNo;
        }

        public String getJobNo() {
            return jobNo;
        }

        public void setJobNo(String jobNo) {
            this.jobNo = jobNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getIntRef() {
            return intRef;
        }

        public void setIntRef(String intRef) {
            this.intRef = intRef;
        }

        public String getIsRescan() {
            return isRescan;
        }

        public void setIsRescan(String isRescan) {
            this.isRescan = isRescan;
        }

        public String getScannedQty() {
            return scannedQty;
        }

        public void setScannedQty(String scannedQty) {
            this.scannedQty = scannedQty;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getSewingInQty() {
            return SewingInQty;
        }

        public void setSewingInQty(String sewingInQty) {
            SewingInQty = sewingInQty;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
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

        public String getColorTypeId() {
            return colorTypeId;
        }

        public void setColorTypeId(String colorTypeId) {
            this.colorTypeId = colorTypeId;
        }

        public String getSewingInputLine() {
            return sewingInputLine;
        }

        public void setSewingInputLine(String sewingInputLine) {
            this.sewingInputLine = sewingInputLine;
        }

        public String getReplaceFieldDisable() {
            return replaceFieldDisable;
        }

        public void setReplaceFieldDisable(String replaceFieldDisable) {
            this.replaceFieldDisable = replaceFieldDisable;
        }

    }
}
