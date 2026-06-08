package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class V1_OperationSaveEntryRequest implements Serializable {
    @SerializedName("company_id")
    @Expose
    private Integer companyId;
    @SerializedName("cut_no")
    @Expose
    private String cutNo;
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("bundle_no")
    @Expose
    private String bundleNo;
    @SerializedName("barcode_no")
    @Expose
    private String barcodeNo;
    @SerializedName("color_size_id")
    @Expose
    private Integer colorSizeId;
    @SerializedName("color_id")
    @Expose
    private Integer colorId;
    @SerializedName("size_id")
    @Expose
    private Integer sizeId;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("job_id")
    @Expose
    private Integer jobId;
    @SerializedName("job_no")
    @Expose
    private String jobNo;
    @SerializedName("buyer_id")
    @Expose
    private Integer buyerId;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("operator_id")
    @Expose
    private String operatorId;
    @SerializedName("lib_operation_id")
    @Expose
    private Integer libOperationId;
    @SerializedName("operation_start")
    @Expose
    private String operationStart;
    @SerializedName("operation_end")
    @Expose
    private String operationEnd;
    @SerializedName("line_id")
    @Expose
    private Integer lineId;
    @SerializedName("ws_id")
    @Expose
    private Integer wsId;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCutNo() {
        return cutNo;
    }

    public void setCutNo(String cutNo) {
        this.cutNo = cutNo;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
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

    public Integer getColorSizeId() {
        return colorSizeId;
    }

    public void setColorSizeId(Integer colorSizeId) {
        this.colorSizeId = colorSizeId;
    }

    public Integer getColorId() {
        return colorId;
    }

    public void setColorId(Integer colorId) {
        this.colorId = colorId;
    }

    public Integer getSizeId() {
        return sizeId;
    }

    public void setSizeId(Integer sizeId) {
        this.sizeId = sizeId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
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

    public Integer getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Integer buyerId) {
        this.buyerId = buyerId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getLibOperationId() {
        return libOperationId;
    }

    public void setLibOperationId(Integer libOperationId) {
        this.libOperationId = libOperationId;
    }

    public String getOperationStart() {
        return operationStart;
    }

    public void setOperationStart(String operationStart) {
        this.operationStart = operationStart;
    }

    public String getOperationEnd() {
        return operationEnd;
    }

    public void setOperationEnd(String operationEnd) {
        this.operationEnd = operationEnd;
    }

    public Integer getLineId() {
        return lineId;
    }

    public void setLineId(Integer lineId) {
        this.lineId = lineId;
    }

    public Integer getWsId() {
        return wsId;
    }

    public void setWsId(Integer wsId) {
        this.wsId = wsId;
    }
}
