package com.logicsoftbd.lsl.data.network.v1_model;

import java.io.Serializable;

public class V1_CuttingStoreIssueModel implements Serializable {
    private String bundleNo;
    private String barcodeNo;
    private String poBreakdownId;
    private String poNumber;
    private String buyerId;
    private String buyerName;
    private String qcPassQnty;
    private String companyId;
    private String companyName;
    private String sizeId;
    private String sizeName;
    private String colorNumberId;
    private String colorName;
    private String cuttingFloorId;
    private String cuttingFloorName;
    private String recvRackId;
    private String recvRackName;
    private String productionQnty;
    private String pubMsg;
    private String successStatus;

    private boolean status;

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

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getQcPassQnty() {
        return qcPassQnty;
    }

    public void setQcPassQnty(String qcPassQnty) {
        this.qcPassQnty = qcPassQnty;
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

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
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

    public String getCuttingFloorName() {
        return cuttingFloorName;
    }

    public void setCuttingFloorName(String cuttingFloorName) {
        this.cuttingFloorName = cuttingFloorName;
    }

    public String getRecvRackId() {
        return recvRackId;
    }

    public void setRecvRackId(String recvRackId) {
        this.recvRackId = recvRackId;
    }

    public String getRecvRackName() {
        return recvRackName;
    }

    public void setRecvRackName(String recvRackName) {
        this.recvRackName = recvRackName;
    }

    public String getProductionQnty() {
        return productionQnty;
    }

    public void setProductionQnty(String productionQnty) {
        this.productionQnty = productionQnty;
    }

    public String getPubMsg() {
        return pubMsg;
    }

    public void setPubMsg(String pubMsg) {
        this.pubMsg = pubMsg;
    }

    public String getSuccessStatus() {
        return successStatus;
    }

    public void setSuccessStatus(String successStatus) {
        this.successStatus = successStatus;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
