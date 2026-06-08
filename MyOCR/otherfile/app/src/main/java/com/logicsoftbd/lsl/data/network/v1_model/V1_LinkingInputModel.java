package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_LinkingInputModel {
    private int id;
    private String cut_no;
    private String bundle_no;
    private String barcode_no;
    private int order_id;
    private int item_id;
    private int country_id;
    private int color_id;
    private int size_id;
    private int color_size;
    private int quantity;
    private int is_rescan;

    //show
    private String yearNo;
    private String jobNo;
    private String buyer;
    private String orderNo;
    private String itemNo;
    private String country;
    private String colorNo;
    private String sizeNo;

    public V1_LinkingInputModel() {
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

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
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

    public int getIs_rescan() {
        return is_rescan;
    }

    public void setIs_rescan(int is_rescan) {
        this.is_rescan = is_rescan;
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

    public V1_LinkingInputModel(String cut_no, String bundle_no, String barcode_no, int order_id, int item_id, int country_id, int color_id, int size_id, int color_size, int quantity, int is_rescan, String yearNo, String jobNo, String buyer, String orderNo, String itemNo, String country, String colorNo, String sizeNo) {
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
        this.is_rescan = is_rescan;
        this.yearNo = yearNo;
        this.jobNo = jobNo;
        this.buyer = buyer;
        this.orderNo = orderNo;
        this.itemNo = itemNo;
        this.country = country;
        this.colorNo = colorNo;
        this.sizeNo = sizeNo;
    }
}
