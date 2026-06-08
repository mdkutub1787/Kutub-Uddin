package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class V1_GMTFinishReceiveResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public class Data implements Serializable {

        @SerializedName("BUNDLE_NO")
        @Expose
        private String bundleNo;
        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("BUNDLE_QNTY")
        @Expose
        private String bundleQnty;
        @SerializedName("PRODUCTION_QNTY")
        @Expose
        private String productionQnty;
        @SerializedName("PRODUCTION_SOURCE")
        @Expose
        private String productionSource;
        @SerializedName("PRODUCTION_DATE")
        @Expose
        private String productionDate;
        @SerializedName("PRODUCTION_HOUR")
        @Expose
        private String productionHour;
        @SerializedName("PO_BREAK_DOWN_ID")
        @Expose
        private String poBreakDownId;
        @SerializedName("CHALLAN_NO")
        @Expose
        private String challanNo;
        @SerializedName("PO_COMPANY_ID")
        @Expose
        private String poCompanyId;
        @SerializedName("PO_COMPANY_NAME")
        @Expose
        private String poCompanyName;
        @SerializedName("PO_COMPANY_LOCATION_ID")
        @Expose
        private String poCompanyLocationId;
        @SerializedName("PO_COMPANY_LOCATION_NAME")
        @Expose
        private String poCompanyLocationName;
        @SerializedName("SWEING_COMPANY_ID")
        @Expose
        private String sweingCompanyId;
        @SerializedName("SWEING_COMPANY_NAME")
        @Expose
        private String sweingCompanyName;
        @SerializedName("SWEING_LOCATION")
        @Expose
        private String sweingLocation;
        @SerializedName("SWEING_LOCATION_NAME")
        @Expose
        private String sweingLocationName;
        @SerializedName("SWEING_FLOOR_ID")
        @Expose
        private String sweingFloorId;
        @SerializedName("SWEING_FLOOR_NAME")
        @Expose
        private String sweingFloorName;
        @SerializedName("GROUPING")
        @Expose
        private String grouping;
        @SerializedName("STYLE_REF_NO")
        @Expose
        private String styleRefNo;
        @SerializedName("SEWING_LINE")
        @Expose
        private String sewingLine;
        @SerializedName("SEWING_LINE_NAME")
        @Expose
        private String sewingLineName;
        @SerializedName("ITEM_NUMBER_ID")
        @Expose
        private String itemNumberId;
        @SerializedName("ITEM_NUMBER_NAME")
        @Expose
        private String itemNumberName;
        @SerializedName("COLOR_TYPE_ID")
        @Expose
        private String colorTypeId;
        @SerializedName("COLOR_NUMBER_ID")
        @Expose
        private String colorNumberId;
        @SerializedName("COLOR_NUMBER_NAME")
        @Expose
        private String colorNumberName;
        @SerializedName("COUNTRY_ID")
        @Expose
        private String countryId;
        @SerializedName("SIZE_NUMBER_ID")
        @Expose
        private String sizeNumberId;
        @SerializedName("SIZE_NUMBER_NAME")
        @Expose
        private String sizeNumberName;
        @SerializedName("BUYER_ID")
        @Expose
        private String buyerId;

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

        public String getBundleQnty() {
            return bundleQnty;
        }

        public void setBundleQnty(String bundleQnty) {
            this.bundleQnty = bundleQnty;
        }

        public String getProductionQnty() {
            return productionQnty;
        }

        public void setProductionQnty(String productionQnty) {
            this.productionQnty = productionQnty;
        }

        public String getProductionSource() {
            return productionSource;
        }

        public void setProductionSource(String productionSource) {
            this.productionSource = productionSource;
        }

        public String getProductionDate() {
            return productionDate;
        }

        public void setProductionDate(String productionDate) {
            this.productionDate = productionDate;
        }

        public String getProductionHour() {
            return productionHour;
        }

        public void setProductionHour(String productionHour) {
            this.productionHour = productionHour;
        }

        public String getPoBreakDownId() {
            return poBreakDownId;
        }

        public void setPoBreakDownId(String poBreakDownId) {
            this.poBreakDownId = poBreakDownId;
        }

        public String getChallanNo() {
            return challanNo;
        }

        public void setChallanNo(String challanNo) {
            this.challanNo = challanNo;
        }

        public String getPoCompanyId() {
            return poCompanyId;
        }

        public void setPoCompanyId(String poCompanyId) {
            this.poCompanyId = poCompanyId;
        }

        public String getPoCompanyName() {
            return poCompanyName;
        }

        public void setPoCompanyName(String poCompanyName) {
            this.poCompanyName = poCompanyName;
        }

        public String getPoCompanyLocationId() {
            return poCompanyLocationId;
        }

        public void setPoCompanyLocationId(String poCompanyLocationId) {
            this.poCompanyLocationId = poCompanyLocationId;
        }

        public String getPoCompanyLocationName() {
            return poCompanyLocationName;
        }

        public void setPoCompanyLocationName(String poCompanyLocationName) {
            this.poCompanyLocationName = poCompanyLocationName;
        }

        public String getSweingCompanyId() {
            return sweingCompanyId;
        }

        public void setSweingCompanyId(String sweingCompanyId) {
            this.sweingCompanyId = sweingCompanyId;
        }

        public String getSweingCompanyName() {
            return sweingCompanyName;
        }

        public void setSweingCompanyName(String sweingCompanyName) {
            this.sweingCompanyName = sweingCompanyName;
        }

        public String getSweingLocation() {
            return sweingLocation;
        }

        public void setSweingLocation(String sweingLocation) {
            this.sweingLocation = sweingLocation;
        }

        public String getSweingLocationName() {
            return sweingLocationName;
        }

        public void setSweingLocationName(String sweingLocationName) {
            this.sweingLocationName = sweingLocationName;
        }

        public String getSweingFloorId() {
            return sweingFloorId;
        }

        public void setSweingFloorId(String sweingFloorId) {
            this.sweingFloorId = sweingFloorId;
        }

        public String getSweingFloorName() {
            return sweingFloorName;
        }

        public void setSweingFloorName(String sweingFloorName) {
            this.sweingFloorName = sweingFloorName;
        }

        public String getGrouping() {
            return grouping;
        }

        public void setGrouping(String grouping) {
            this.grouping = grouping;
        }

        public String getStyleRefNo() {
            return styleRefNo;
        }

        public void setStyleRefNo(String styleRefNo) {
            this.styleRefNo = styleRefNo;
        }

        public String getSewingLine() {
            return sewingLine;
        }

        public void setSewingLine(String sewingLine) {
            this.sewingLine = sewingLine;
        }

        public String getSewingLineName() {
            return sewingLineName;
        }

        public void setSewingLineName(String sewingLineName) {
            this.sewingLineName = sewingLineName;
        }

        public String getItemNumberId() {
            return itemNumberId;
        }

        public void setItemNumberId(String itemNumberId) {
            this.itemNumberId = itemNumberId;
        }

        public String getItemNumberName() {
            return itemNumberName;
        }

        public void setItemNumberName(String itemNumberName) {
            this.itemNumberName = itemNumberName;
        }

        public String getColorTypeId() {
            return colorTypeId;
        }

        public void setColorTypeId(String colorTypeId) {
            this.colorTypeId = colorTypeId;
        }

        public String getColorNumberId() {
            return colorNumberId;
        }

        public void setColorNumberId(String colorNumberId) {
            this.colorNumberId = colorNumberId;
        }

        public String getColorNumberName() {
            return colorNumberName;
        }

        public void setColorNumberName(String colorNumberName) {
            this.colorNumberName = colorNumberName;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getSizeNumberId() {
            return sizeNumberId;
        }

        public void setSizeNumberId(String sizeNumberId) {
            this.sizeNumberId = sizeNumberId;
        }

        public String getSizeNumberName() {
            return sizeNumberName;
        }

        public void setSizeNumberName(String sizeNumberName) {
            this.sizeNumberName = sizeNumberName;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
        }

    }
}
