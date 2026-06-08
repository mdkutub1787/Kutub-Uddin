package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GmtsBarcodeResponse implements Serializable{

    private boolean isFirst = true;
    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private BodyPart data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BodyPart getData() {
        return data;
    }

    public void setData(BodyPart data) {
        this.data = data;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public static class BodyPart implements Serializable {

        @Expose
        @SerializedName("message_bng")
        private String messageBng;

        @Expose
        @SerializedName("message_eng")
        private String messageEng;

        @Expose
        @SerializedName("bundle_no")
        private String bundleNo;

        @Expose
        @SerializedName("barcode_no")
        private String barcodeNo;

        @Expose
        @SerializedName("year")
        private int year;

        @Expose
        @SerializedName("color_size_id")
        private String colorSizeId;

        @Expose
        @SerializedName("order_id")
        private int orderId;

        @Expose
        @SerializedName("item_id")
        private int itemId;

        @Expose
        @SerializedName("country_id")
        private int countryId;

        @Expose
        @SerializedName("size_id")
        private int sizeId;

        @Expose
        @SerializedName("color_id")
        private int colorId;

        @Expose
        @SerializedName("cut_no")
        private String cutNo;

        @Expose
        @SerializedName("job_no")
        private String jobNo;

        @Expose
        @SerializedName("buyer")
        private String buyer;

        @Expose
        @SerializedName("order_no")
        private String orderNo;

        @Expose
        @SerializedName("item")
        private String item;

        @Expose
        @SerializedName("country")
        private String country;

        @Expose
        @SerializedName("color")
        private String color;

        @Expose
        @SerializedName("size")
        private String size;

        @Expose
        @SerializedName("qty")
        private int qty;

        @Expose
        @SerializedName("is_rescan")
        private int isRescan;

        @Expose
        @SerializedName("color_type_id")
        private int color_type_id;

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

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getColorSizeId() {
            return colorSizeId;
        }

        public void setColorSizeId(String colorSizeId) {
            this.colorSizeId = colorSizeId;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getCountryId() {
            return countryId;
        }

        public void setCountryId(int countryId) {
            this.countryId = countryId;
        }

        public int getSizeId() {
            return sizeId;
        }

        public void setSizeId(int sizeId) {
            this.sizeId = sizeId;
        }

        public int getColorId() {
            return colorId;
        }

        public void setColorId(int colorId) {
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

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public int getIsRescan() {
            return isRescan;
        }

        public void setIsRescan(int isRescan) {
            this.isRescan = isRescan;
        }

        public int getColor_type_id() {
            return color_type_id;
        }

        public void setColor_type_id(int color_type_id) {
            this.color_type_id = color_type_id;
        }
    }
}
