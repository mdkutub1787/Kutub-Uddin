package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_CuttingStoreReceivePost {
    @SerializedName("BUNDLE_NO")
    @Expose
    private String bundleNo;
    @SerializedName("BARCODE_NO")
    @Expose
    private String barcodeNo;
    @SerializedName("PO_BREAKDOWN_ID")
    @Expose
    private String poBreakdownId;
    @SerializedName("PO_NUMBER")
    @Expose
    private String poNumber;
    @SerializedName("BUYER_ID")
    @Expose
    private String buyerId;
    @SerializedName("COMPANY_ID")
    @Expose
    private String companyId;
    @SerializedName("QC_PASS_QNTY")
    @Expose
    private String qcPassQnty;
    @SerializedName("SIZE_ID")
    @Expose
    private String sizeId;
    @SerializedName("COLOR_NUMBER_ID")
    @Expose
    private String colorNumberId;
    @SerializedName("COLOR_NAME")
    @Expose
    private String colorName;
    @SerializedName("CUTTING_FLOOR_ID")
    @Expose
    private String cuttingFloorId;
    @SerializedName("FL_RO_RACK_DTL_ID")
    @Expose
    private String flRoRackDtlId;
    @SerializedName("PRODUCTION_QNTY")
    @Expose
    private String productionQnty;
    @SerializedName("RECEIVE_QNTY_KG")
    @Expose
    private String receiveQntyKg;
    @SerializedName("USER_ID")
    @Expose
    private String userId;

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

    public String getPoBreakdownId() {
        return poBreakdownId;
    }

    public void setPoBreakdownId(String poBreakdownId) {
        this.poBreakdownId = poBreakdownId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getQcPassQnty() {
        return qcPassQnty;
    }

    public void setQcPassQnty(String qcPassQnty) {
        this.qcPassQnty = qcPassQnty;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getColorNumberId() {
        return colorNumberId;
    }

    public void setColorNumberId(String colorNumberId) {
        this.colorNumberId = colorNumberId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getCuttingFloorId() {
        return cuttingFloorId;
    }

    public void setCuttingFloorId(String cuttingFloorId) {
        this.cuttingFloorId = cuttingFloorId;
    }

    public String getFlRoRackDtlId() {
        return flRoRackDtlId;
    }

    public void setFlRoRackDtlId(String flRoRackDtlId) {
        this.flRoRackDtlId = flRoRackDtlId;
    }

    public String getProductionQnty() {
        return productionQnty;
    }

    public void setProductionQnty(String productionQnty) {
        this.productionQnty = productionQnty;
    }

    public String getReceiveQntyKg() {
        return receiveQntyKg;
    }

    public void setReceiveQntyKg(String receiveQntyKg) {
        this.receiveQntyKg = receiveQntyKg;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
