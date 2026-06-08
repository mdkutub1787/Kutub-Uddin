package com.logicsoftbd.lsl.data.network.v1_model;
import java.util.Comparator;

public class V1_DefectedRectifiedModel_V4 {
    private String poBreakDownId;
    private String poNumber;
    private String mstId;
    private String dtlsId;
    private String operationName;
    private String alterQty;
    private String spotQty;
    private String rectifiedQty;
    private String productionDate;
    private String colSizeId;
    private String defectNames;
    private String colorName;
    private String sizeName;
    private String defectCount;
    private Boolean select;

    public String getPoBreakDownId() {
        return poBreakDownId;
    }

    public void setPoBreakDownId(String poBreakDownId) {
        this.poBreakDownId = poBreakDownId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getMstId() {
        return mstId;
    }

    public void setMstId(String mstId) {
        this.mstId = mstId;
    }

    public String getDtlsId() {
        return dtlsId;
    }

    public void setDtlsId(String dtlsId) {
        this.dtlsId = dtlsId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getAlterQty() {
        return alterQty;
    }

    public void setAlterQty(String alterQty) {
        this.alterQty = alterQty;
    }

    public String getSpotQty() {
        return spotQty;
    }

    public void setSpotQty(String spotQty) {
        this.spotQty = spotQty;
    }

    public String getRectifiedQty() {
        return rectifiedQty;
    }

    public void setRectifiedQty(String rectifiedQty) {
        this.rectifiedQty = rectifiedQty;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getColSizeId() {
        return colSizeId;
    }

    public void setColSizeId(String colSizeId) {
        this.colSizeId = colSizeId;
    }

    public String getDefectNames() {
        return defectNames;
    }

    public void setDefectNames(String defectNames) {
        this.defectNames = defectNames;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getSizeName() {
        return sizeName;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public String getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(String defectCount) {
        this.defectCount = defectCount;
    }

    public static Comparator<V1_DefectedRectifiedModel_V4> nameComparator = (obj1, obj2) -> obj1.getSpotQty().compareTo(obj2.getSpotQty());
}
