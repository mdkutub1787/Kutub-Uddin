package com.logicsoftbd.lsl.data.network.v1_model;

import java.util.Comparator;

public class V1_DefectedRectifiedModel {
    private String mstId;
    private String dtlsId;
    private String dtls_piece_id;
    private String operationName;
    private String operationId;
    private String color_type_id;
    private String alterQty;
    private String spotQty;
    private String productionDate;
    private String colorSizedId;
    private String defectNames;
    private String defectTypeNames;
    private String colorName;
    private String sizeName;
    private String sizeId;
    private String colorId;
    private String countryId;
    private Boolean select;

    public String getMstId() {
        return mstId;
    }

    public String getDtls_piece_id() {
        return dtls_piece_id;
    }

    public String getColor_type_id() {
        return color_type_id;
    }

    public void setColor_type_id(String color_type_id) {
        this.color_type_id = color_type_id;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getSizeId() {
        return sizeId;
    }

    public void setSizeId(String sizeId) {
        this.sizeId = sizeId;
    }

    public void setDtls_piece_id(String dtls_piece_id) {
        this.dtls_piece_id = dtls_piece_id;
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

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getColorSizedId() {
        return colorSizedId;
    }

    public void setColorSizedId(String colorSizedId) {
        this.colorSizedId = colorSizedId;
    }

    public String getDefectNames() {
        return defectNames;
    }

    public void setDefectNames(String defectNames) {
        this.defectNames = defectNames;
    }

    public String getDefectTypeNames() {
        return defectTypeNames;
    }

    public void setDefectTypeNames(String defectTypeNames) {
        this.defectTypeNames = defectTypeNames;
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

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public static Comparator<V1_DefectedRectifiedModel> nameComparator = (obj1, obj2) -> obj1.getSpotQty().compareTo(obj2.getSpotQty());
}
