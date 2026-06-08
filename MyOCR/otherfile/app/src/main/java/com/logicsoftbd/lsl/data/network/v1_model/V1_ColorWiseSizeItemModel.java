package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_ColorWiseSizeItemModel {
    private String colorSizeBreakdownId;
    private String colourId;
    private String sizeId;
    private String sizeName;
    private String inputQnty;
    private String outputQnty;
    private String good;
    private String reject;
    private String alter;
    private String spot;
    private String rectified;
    private Boolean selectedItem;

    public String getColorSizeBreakdownId() {
        return colorSizeBreakdownId;
    }

    public void setColorSizeBreakdownId(String colorSizeBreakdownId) {
        this.colorSizeBreakdownId = colorSizeBreakdownId;
    }

    public String getColourId() {
        return colourId;
    }

    public void setColourId(String colourId) {
        this.colourId = colourId;
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

    public String getInputQnty() {
        return inputQnty;
    }

    public void setInputQnty(String inputQnty) {
        this.inputQnty = inputQnty;
    }

    public String getOutputQnty() {
        return outputQnty;
    }

    public void setOutputQnty(String outputQnty) {
        this.outputQnty = outputQnty;
    }

    public String getGood() {
        return good;
    }

    public void setGood(String good) {
        this.good = good;
    }

    public String getReject() {
        return reject;
    }

    public void setReject(String reject) {
        this.reject = reject;
    }

    public String getAlter() {
        return alter;
    }

    public void setAlter(String alter) {
        this.alter = alter;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getRectified() {
        return rectified;
    }

    public void setRectified(String rectified) {
        this.rectified = rectified;
    }

    public Boolean getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Boolean selectedItem) {
        this.selectedItem = selectedItem;
    }
}
