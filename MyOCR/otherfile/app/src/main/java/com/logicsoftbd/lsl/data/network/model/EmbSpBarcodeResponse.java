package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EmbSpBarcodeResponse implements Serializable {
    private boolean isFirst = true;
    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private BodyPart data;

    @Expose
    @SerializedName("msg")
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

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

    public static class BodyPart implements  Serializable{
        @Expose
        @SerializedName("dtlsPart")
        private DetailsPart details;

        @Expose
        @SerializedName("MasterPart")
        private MasterPart master;

        @Expose
        @SerializedName("msg")
        private String msg;

        @Expose
        @SerializedName("status")
        private String status;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public DetailsPart getDetails() {
            return details;
        }

        public void setDetails(DetailsPart details) {
            this.details = details;
        }

        public MasterPart getMaster() {
            return master;
        }

        public void setMaster(MasterPart master) {
            this.master = master;
        }

        public  static class MasterPart implements Serializable {

            @SerializedName("CHALLAN_ID")
            @Expose
            public Integer challanId;

            @SerializedName("CHALLAN_NO")
            @Expose
            public String bundleNo;

            @SerializedName("LOCATION_ID")
            @Expose
            public Integer locationId;

            @SerializedName("COMPANY_ID")
            @Expose
            public Integer companyId;

            @SerializedName("EMBEL_NAME")
            @Expose
            public Integer emblId;

            @SerializedName("SERVING_COMPANY")
            @Expose
            public Integer servingCompany;

            @SerializedName("FLOOR_ID")
            @Expose
            public Integer floorId;

            @SerializedName("PRODUCTION_SOURCE")
            @Expose
            public Integer productionSource;

            public Integer getEmblId() {
                return emblId;
            }

            public void setEmblId(Integer emblId) {
                this.emblId = emblId;
            }

            public Integer getServingCompany() {
                return servingCompany;
            }

            public void setServingCompany(Integer servingCompany) {
                this.servingCompany = servingCompany;
            }

            public Integer getChallanId() {
                return challanId;
            }

            public void setChallanId(Integer challanId) {
                this.challanId = challanId;
            }

            public String getBundleNo() {
                return bundleNo;
            }

            public void setBundleNo(String bundleNo) {
                this.bundleNo = bundleNo;
            }

            public Integer getLocationId() {
                return locationId;
            }

            public void setLocationId(Integer locationId) {
                this.locationId = locationId;
            }

            public Integer getCompanyId() {
                return companyId;
            }

            public void setCompanyId(Integer companyId) {
                this.companyId = companyId;
            }

            public Integer getFloorId() {
                return floorId;
            }

            public void setFloorId(Integer floorId) {
                this.floorId = floorId;
            }

            public Integer getProductionSource() {
                return productionSource;
            }

            public void setProductionSource(Integer productionSource) {
                this.productionSource = productionSource;
            }
        }

        public static class DetailsPart implements Serializable{

            @SerializedName("COMPANY_ID")
            @Expose
            public Integer companyId;

            @SerializedName("PRODUCTION_TYPE")
            @Expose
            public Integer productionType;


            @Expose
            @SerializedName("BUNDLE_NO")
            private String bundleNo;

            @Expose
            @SerializedName("BARCODE_NO")
            private String barcodeNo;

            @Expose
            @SerializedName("YEAR")
            private int year;

            @Expose
            @SerializedName("COLOR_SIZE_ID")
            private String colorSizeId;

            @Expose
            @SerializedName("ORDER_ID")
            private int orderId;

            @Expose
            @SerializedName("ITEM_ID")
            private int itemId;

            @Expose
            @SerializedName("COUNTRY_ID")
            private int countryId;

            @Expose
            @SerializedName("SIZE_ID")
            private int sizeId;

            @Expose
            @SerializedName("COLOR_ID")
            private int colorId;

            @Expose
            @SerializedName("CUT_NO")
            private String cutNo;

            @Expose
            @SerializedName("JOB_NO")
            private String jobNo;

            @Expose
            @SerializedName("BUYER")
            private String buyer;

            @Expose
            @SerializedName("ORDER_NO")
            private String orderNo;

            @Expose
            @SerializedName("ITEM_NAME")
            private String item;


            @Expose
            @SerializedName("QNTY")
            private int qty;

            @Expose
            @SerializedName("RE_SCAN")
            private int isRescan;

            @Expose
            @SerializedName("COLOR_TYPE_ID")
            private String color_type_id;

            @SerializedName("REJECT")
            @Expose
            public Integer reject;

            @SerializedName("REPLACE")
            @Expose
            public Integer replace;

            @SerializedName("QC_QNTY")
            @Expose
            public Integer qcQuantity;

            @Expose
            @SerializedName("DEFECT_STR")
            private String defectStr;

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

            public String getColor_type_id() {
                return color_type_id;
            }

            public void setColor_type_id(String color_type_id) {
                this.color_type_id = color_type_id;
            }


            public Integer getReject() {
                return reject;
            }

            public void setReject(Integer reject) {
                this.reject = reject;
            }

            public Integer getReplace() {
                return replace;
            }

            public void setReplace(Integer replace) {
                this.replace = replace;
            }

            public Integer getQcQuantity() {
                return qcQuantity;
            }

            public void setQcQuantity(Integer qcQuantity) {
                this.qcQuantity = qcQuantity;
            }

            public Integer getCompanyId() {
                return companyId;
            }

            public void setCompanyId(Integer companyId) {
                this.companyId = companyId;
            }

            public Integer getProductionType() {
                return productionType;
            }

            public void setProductionType(Integer productionType) {
                this.productionType = productionType;
            }

            public String getDefectStr() {
                return defectStr;
            }

            public void setDefectStr(String defectStr) {
                this.defectStr = defectStr;
            }
        }

    }

}
