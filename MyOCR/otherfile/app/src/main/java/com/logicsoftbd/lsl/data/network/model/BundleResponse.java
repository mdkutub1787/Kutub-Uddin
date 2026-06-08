package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BundleResponse  implements Serializable {
    private  String title;

    @Expose
    @SerializedName("status_code")
    private String statusCode;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("barcode")
    private String barcode;

    @Expose
    @SerializedName("page_param")
    private String pageParam;

    @Expose
    @SerializedName("type_param")
    private String typeParam;

    @Expose
    @SerializedName("data")
    private BundleResponse.Bundle data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public String getTypeParam() {
        return typeParam;
    }

    public void setTypeParam(String typeParam) {
        this.typeParam = typeParam;
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public static class Bundle implements Serializable{

        @Expose
        @SerializedName("date_time")
        private String DateTime;

        @Expose
        @SerializedName("cutting_no")
        private String cuttingNo;

        @Expose
        @SerializedName("cutting_floor")
        private String cuttingFloor;

        @Expose
        @SerializedName("bundle_no")
        private String bundleNo;

        @Expose
        @SerializedName("bundle_qty")
        private Integer bundleQuantity;

        @Expose
        @SerializedName("reject_qty")
        private Integer rejectQty;

        @Expose
        @SerializedName("replace_qty")
        private Integer replaceQuantity;

        @Expose
        @SerializedName("qc_pass_qty")
        private Integer qcPassQuantity;

        public String getDateTime() {
            return DateTime;
        }

        public void setDateTime(String dateTime) {
            DateTime = dateTime;
        }

        public String getBundleNo() {
            return bundleNo;
        }

        public void setBundleNo(String bundleNo) {
            this.bundleNo = bundleNo;
        }

        public Integer getBundleQuantity() {
            return bundleQuantity;
        }

        public void setBundleQuantity(Integer bundleQuantity) {
            this.bundleQuantity = bundleQuantity;
        }

        public Integer getRejectQty() {
            return rejectQty;
        }

        public void setRejectQty(Integer rejectQty) {
            this.rejectQty = rejectQty;
        }

        public Integer getReplaceQuantity() {
            return replaceQuantity;
        }

        public void setReplaceQuantity(Integer replaceQuantity) {
            this.replaceQuantity = replaceQuantity;
        }

        public Integer getQcPassQuantity() {
            return qcPassQuantity;
        }

        public void setQcPassQuantity(Integer qcPassQuantity) {
            this.qcPassQuantity = qcPassQuantity;
        }

        public String getCuttingNo() {
            return cuttingNo;
        }

        public void setCuttingNo(String cuttingNo) {
            this.cuttingNo = cuttingNo;
        }

        public String getCuttingFloor() {
            return cuttingFloor;
        }

        public void setCuttingFloor(String cuttingFloor) {
            this.cuttingFloor = cuttingFloor;
        }
    }
}
