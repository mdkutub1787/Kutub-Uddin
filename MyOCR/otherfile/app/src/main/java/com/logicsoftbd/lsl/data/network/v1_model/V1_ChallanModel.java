package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ChallanModel {
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

        @SerializedName("MST")
        @Expose
        private MST mST;
        @SerializedName("DTLS")
        @Expose
        private List<DTL> dTLS = null;

        public MST getMST() {
            return mST;
        }

        public void setMST(MST mST) {
            this.mST = mST;
        }

        public List<DTL> getDTLS() {
            return dTLS;
        }

        public void setDTLS(List<DTL> dTLS) {
            this.dTLS = dTLS;
        }

    }
    public class MST {

        @SerializedName("ID")
        @Expose
        private Integer iD;
        @SerializedName("SYS_NUMBER")
        @Expose
        private String sYSNUMBER;
        @SerializedName("COMPANY_ID")
        @Expose
        private Integer cOMPANYID;
        @SerializedName("LOCATION_ID")
        @Expose
        private Integer lOCATIONID;
        @SerializedName("FLOOR_ID")
        @Expose
        private Integer fLOORID;
        @SerializedName("SEWING_LINE")
        @Expose
        private Integer sEWINGLINE;
        @SerializedName("DELIVERY_DATE")
        @Expose
        private String dELIVERYDATE;
        @SerializedName("PRODUCTION_SOURCE")
        @Expose
        private Integer pRODUCTIONSOURCE;
        @SerializedName("SERVING_COMPANY")
        @Expose
        private Integer sERVINGCOMPANY;
        @SerializedName("WORKING_COMPANY_ID")
        @Expose
        private Integer wORKINGCOMPANYID;
        @SerializedName("WORKING_LOCATION_ID")
        @Expose
        private Integer wORKINGLOCATIONID;

        public Integer getID() {
            return iD;
        }

        public void setID(Integer iD) {
            this.iD = iD;
        }

        public String getSYSNUMBER() {
            return sYSNUMBER;
        }

        public void setSYSNUMBER(String sYSNUMBER) {
            this.sYSNUMBER = sYSNUMBER;
        }

        public Integer getCOMPANYID() {
            return cOMPANYID;
        }

        public void setCOMPANYID(Integer cOMPANYID) {
            this.cOMPANYID = cOMPANYID;
        }

        public Integer getLOCATIONID() {
            return lOCATIONID;
        }

        public void setLOCATIONID(Integer lOCATIONID) {
            this.lOCATIONID = lOCATIONID;
        }

        public Integer getFLOORID() {
            return fLOORID;
        }

        public void setFLOORID(Integer fLOORID) {
            this.fLOORID = fLOORID;
        }

        public Integer getSEWINGLINE() {
            return sEWINGLINE;
        }

        public void setSEWINGLINE(Integer sEWINGLINE) {
            this.sEWINGLINE = sEWINGLINE;
        }

        public String getDELIVERYDATE() {
            return dELIVERYDATE;
        }

        public void setDELIVERYDATE(String dELIVERYDATE) {
            this.dELIVERYDATE = dELIVERYDATE;
        }

        public Integer getPRODUCTIONSOURCE() {
            return pRODUCTIONSOURCE;
        }

        public void setPRODUCTIONSOURCE(Integer pRODUCTIONSOURCE) {
            this.pRODUCTIONSOURCE = pRODUCTIONSOURCE;
        }

        public Integer getSERVINGCOMPANY() {
            return sERVINGCOMPANY;
        }

        public void setSERVINGCOMPANY(Integer sERVINGCOMPANY) {
            this.sERVINGCOMPANY = sERVINGCOMPANY;
        }

        public Integer getWORKINGCOMPANYID() {
            return wORKINGCOMPANYID;
        }

        public void setWORKINGCOMPANYID(Integer wORKINGCOMPANYID) {
            this.wORKINGCOMPANYID = wORKINGCOMPANYID;
        }

        public Integer getWORKINGLOCATIONID() {
            return wORKINGLOCATIONID;
        }

        public void setWORKINGLOCATIONID(Integer wORKINGLOCATIONID) {
            this.wORKINGLOCATIONID = wORKINGLOCATIONID;
        }

    }
    public class DTL {

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
        private String qty;
        @SerializedName("is_rescan")
        @Expose
        private String isRescan;
        @SerializedName("color_type_id")
        @Expose
        private String colorTypeId;

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

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getIsRescan() {
            return isRescan;
        }

        public void setIsRescan(String isRescan) {
            this.isRescan = isRescan;
        }

        public String getColorTypeId() {
            return colorTypeId;
        }

        public void setColorTypeId(String colorTypeId) {
            this.colorTypeId = colorTypeId;
        }

    }
}
