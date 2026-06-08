package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_SewingOutputModelClass {
    private int id;
    private String cut_no;
    private String bundle_no;
    private String barcode_no;
    private int order_id;
    private String item_id;
    private String country_id;
    private int color_id;
    private int size_id;
    private int color_size;
    private int quantity;
    private int replace_qty;
    private int is_rescan;
    private int replace_field_disable;

    //show
    private String yearNo;
    private String jobNo;
    private String buyer;
    private String orderNo;
    private String itemNo;
    private String country;
    private String colorNo;
    private String sizeNo;

    private int reject, alter, spot, replace, qc_qty;

    public V1_SewingOutputModelClass() {
    }

    public V1_SewingOutputModelClass(int id, String cut_no, String bundle_no, String barcode_no, int order_id, String item_id, String country_id, int color_id, int size_id, int color_size, int quantity, int replace_qty, int is_rescan, int replace_field_disable, String yearNo, String jobNo, String buyer, String orderNo, String itemNo, String country, String colorNo, String sizeNo, int reject, int alter, int spot, int replace, int qc_qty) {
        this.id = id;
        this.cut_no = cut_no;
        this.bundle_no = bundle_no;
        this.barcode_no = barcode_no;
        this.order_id = order_id;
        this.item_id = item_id;
        this.country_id = country_id;
        this.color_id = color_id;
        this.size_id = size_id;
        this.color_size = color_size;
        this.quantity = quantity;
        this.replace_qty = replace_qty;
        this.is_rescan = is_rescan;
        this.replace_field_disable = replace_field_disable;
        this.yearNo = yearNo;
        this.jobNo = jobNo;
        this.buyer = buyer;
        this.orderNo = orderNo;
        this.itemNo = itemNo;
        this.country = country;
        this.colorNo = colorNo;
        this.sizeNo = sizeNo;
        this.reject = reject;
        this.alter = alter;
        this.spot = spot;
        this.replace = replace;
        this.qc_qty = qc_qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCut_no() {
        return cut_no;
    }

    public void setCut_no(String cut_no) {
        this.cut_no = cut_no;
    }

    public String getBundle_no() {
        return bundle_no;
    }

    public void setBundle_no(String bundle_no) {
        this.bundle_no = bundle_no;
    }

    public String getBarcode_no() {
        return barcode_no;
    }

    public void setBarcode_no(String barcode_no) {
        this.barcode_no = barcode_no;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getColor_size() {
        return color_size;
    }

    public void setColor_size(int color_size) {
        this.color_size = color_size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReplace_qty() {
        return replace_qty;
    }

    public void setReplace_qty(int replace_qty) {
        this.replace_qty = replace_qty;
    }

    public int getIs_rescan() {
        return is_rescan;
    }

    public void setIs_rescan(int is_rescan) {
        this.is_rescan = is_rescan;
    }

    public int getReplace_field_disable() {
        return replace_field_disable;
    }

    public void setReplace_field_disable(int replace_field_disable) {
        this.replace_field_disable = replace_field_disable;
    }

    public String getYearNo() {
        return yearNo;
    }

    public void setYearNo(String yearNo) {
        this.yearNo = yearNo;
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

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getColorNo() {
        return colorNo;
    }

    public void setColorNo(String colorNo) {
        this.colorNo = colorNo;
    }

    public String getSizeNo() {
        return sizeNo;
    }

    public void setSizeNo(String sizeNo) {
        this.sizeNo = sizeNo;
    }

    public int getReject() {
        return reject;
    }

    public void setReject(int reject) {
        this.reject = reject;
    }

    public int getAlter() {
        return alter;
    }

    public void setAlter(int alter) {
        this.alter = alter;
    }

    public int getSpot() {
        return spot;
    }

    public void setSpot(int spot) {
        this.spot = spot;
    }

    public int getReplace() {
        return replace;
    }

    public void setReplace(int replace) {
        this.replace = replace;
    }

    public int getQc_qty() {
        return qc_qty;
    }

    public void setQc_qty(int qc_qty) {
        this.qc_qty = qc_qty;
    }
}
