package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_RollWiseGreyFabricDeliveryToStoreResponse {
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

    public class Data {

        @SerializedName("BARCODE_NO")
        @Expose
        private String barcodeNo;
        @SerializedName("COMPANY_ID")
        @Expose
        private String companyId;
        @SerializedName("COMPANY_NAME")
        @Expose
        private String companyName;
        @SerializedName("CHALLAN_NO")
        @Expose
        private Object challanNo;
        @SerializedName("KNIT_COMPANY_ID")
        @Expose
        private String knitCompanyId;
        @SerializedName("KNIT_COMPANY")
        @Expose
        private String knitCompany;
        @SerializedName("KNIT_SOURCE_ID")
        @Expose
        private String knitSourceId;
        @SerializedName("KNIT_SOURCE_NAME")
        @Expose
        private String knitSourceName;
        @SerializedName("LOCATION_ID")
        @Expose
        private String locationId;
        @SerializedName("LOCATION_NAME")
        @Expose
        private String locationName;
        @SerializedName("FLOOR_ID")
        @Expose
        private String floorId;
        @SerializedName("FLOOR_NAME")
        @Expose
        private String floorName;
        @SerializedName("WEIGHT")
        @Expose
        private String weight;
        @SerializedName("PRODUCTIONID")
        @Expose
        private String productionid;
        @SerializedName("PRODUCTIONDTLSID")
        @Expose
        private String productiondtlsid;
        @SerializedName("PRODUCTID")
        @Expose
        private String productid;
        @SerializedName("ORDERID")
        @Expose
        private String orderid;
        @SerializedName("DETERID")
        @Expose
        private String deterid;
        @SerializedName("ROLLID")
        @Expose
        private String rollid;
        @SerializedName("CURRENTDELIVERY")
        @Expose
        private String currentdelivery;
        @SerializedName("ROLLNO")
        @Expose
        private String rollno;
        @SerializedName("BOOKINGWITHOUTORDER")
        @Expose
        private String bookingwithoutorder;
        @SerializedName("SMNBOOKINGNO")
        @Expose
        private String smnbookingno;
        @SerializedName("ISSALES")
        @Expose
        private String issales;

        public String getBarcodeNo() {
            return barcodeNo;
        }

        public void setBarcodeNo(String barcodeNo) {
            this.barcodeNo = barcodeNo;
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

        public Object getChallanNo() {
            return challanNo;
        }

        public void setChallanNo(Object challanNo) {
            this.challanNo = challanNo;
        }

        public String getKnitCompanyId() {
            return knitCompanyId;
        }

        public void setKnitCompanyId(String knitCompanyId) {
            this.knitCompanyId = knitCompanyId;
        }

        public String getKnitCompany() {
            return knitCompany;
        }

        public void setKnitCompany(String knitCompany) {
            this.knitCompany = knitCompany;
        }

        public String getKnitSourceId() {
            return knitSourceId;
        }

        public void setKnitSourceId(String knitSourceId) {
            this.knitSourceId = knitSourceId;
        }

        public String getKnitSourceName() {
            return knitSourceName;
        }

        public void setKnitSourceName(String knitSourceName) {
            this.knitSourceName = knitSourceName;
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

        public String getFloorId() {
            return floorId;
        }

        public void setFloorId(String floorId) {
            this.floorId = floorId;
        }

        public String getFloorName() {
            return floorName;
        }

        public void setFloorName(String floorName) {
            this.floorName = floorName;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getProductionid() {
            return productionid;
        }

        public void setProductionid(String productionid) {
            this.productionid = productionid;
        }

        public String getProductiondtlsid() {
            return productiondtlsid;
        }

        public void setProductiondtlsid(String productiondtlsid) {
            this.productiondtlsid = productiondtlsid;
        }

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getDeterid() {
            return deterid;
        }

        public void setDeterid(String deterid) {
            this.deterid = deterid;
        }

        public String getRollid() {
            return rollid;
        }

        public void setRollid(String rollid) {
            this.rollid = rollid;
        }

        public String getCurrentdelivery() {
            return currentdelivery;
        }

        public void setCurrentdelivery(String currentdelivery) {
            this.currentdelivery = currentdelivery;
        }

        public String getRollno() {
            return rollno;
        }

        public void setRollno(String rollno) {
            this.rollno = rollno;
        }

        public String getBookingwithoutorder() {
            return bookingwithoutorder;
        }

        public void setBookingwithoutorder(String bookingwithoutorder) {
            this.bookingwithoutorder = bookingwithoutorder;
        }

        public String getSmnbookingno() {
            return smnbookingno;
        }

        public void setSmnbookingno(String smnbookingno) {
            this.smnbookingno = smnbookingno;
        }

        public String getIssales() {
            return issales;
        }

        public void setIssales(String issales) {
            this.issales = issales;
        }

    }
}
