package com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FFRData implements Serializable {
    @SerializedName("is_sales")
    @Expose
    private String isSales;
    @SerializedName("barcode_no")
    @Expose
    private String barcodeNo;
    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("buyer_id")
    @Expose
    private String buyerId;
    @SerializedName("roll_no")
    @Expose
    private String rollNo;
    @SerializedName("roll_id")
    @Expose
    private String rollId;
    @SerializedName("batch_id")
    @Expose
    private String batchId;
    @SerializedName("batch_no")
    @Expose
    private String batchNo;
    @SerializedName("determination_id")
    @Expose
    private String determinationId;
    @SerializedName("bodypart_id")
    @Expose
    private String bodypartId;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("construction")
    @Expose
    private String construction;
    @SerializedName("composition")
    @Expose
    private String composition;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("gsm")
    @Expose
    private String gsm;
    @SerializedName("dia")
    @Expose
    private String dia;
    @SerializedName("qnty")
    @Expose
    private String qnty;
    @SerializedName("qty_in_pcs")
    @Expose
    private String qtyInPcs;
    @SerializedName("item_size")
    @Expose
    private String itemSize;
    @SerializedName("reject_qnty")
    @Expose
    private String rejectQnty;
    @SerializedName("reprocess")
    @Expose
    private String reprocess;
    @SerializedName("prev_reprocess")
    @Expose
    private String prevReprocess;
    @SerializedName("grey_wgt.")
    @Expose
    private String greyWgt;
    @SerializedName("width_type")
    @Expose
    private String widthType;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("grey_sys_number")
    @Expose
    private String greySysNumber;
    @SerializedName("bookingNumber")
    @Expose
    private String bookingNumber;
    @SerializedName("booking_without_order")
    @Expose
    private String bookingWithoutOrder;
    @SerializedName("cbo_knitting_source")
    @Expose
    private String cboKnittingSource;
    @SerializedName("knitting_company")
    @Expose
    private String knittingCompany;
    @SerializedName("location_id")
    @Expose
    private String locationId;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("floor_id")
    @Expose
    private String floorId;
    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("rack_id")
    @Expose
    private String rackId;
    @SerializedName("shelf_id")
    @Expose
    private String shelfId;
    @SerializedName("bin_id")
    @Expose
    private String binId;


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

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getRollId() {
        return rollId;
    }

    public void setRollId(String rollId) {
        this.rollId = rollId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getDeterminationId() {
        return determinationId;
    }

    public void setDeterminationId(String determinationId) {
        this.determinationId = determinationId;
    }

    public String getBodypartId() {
        return bodypartId;
    }

    public void setBodypartId(String bodypartId) {
        this.bodypartId = bodypartId;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getQnty() {
        return qnty;
    }

    public void setQnty(String qnty) {
        this.qnty = qnty;
    }

    public String getQtyInPcs() {
        return qtyInPcs;
    }

    public void setQtyInPcs(String qtyInPcs) {
        this.qtyInPcs = qtyInPcs;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getRejectQnty() {
        return rejectQnty;
    }

    public void setRejectQnty(String rejectQnty) {
        this.rejectQnty = rejectQnty;
    }

    public String getReprocess() {
        return reprocess;
    }

    public void setReprocess(String reprocess) {
        this.reprocess = reprocess;
    }

    public String getGreyWgt() {
        return greyWgt;
    }

    public void setGreyWgt(String greyWgt) {
        this.greyWgt = greyWgt;
    }

    public String getWidthType() {
        return widthType;
    }

    public void setWidthType(String widthType) {
        this.widthType = widthType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getGreySysNumber() {
        return greySysNumber;
    }

    public void setGreySysNumber(String greySysNumber) {
        this.greySysNumber = greySysNumber;
    }

    public String getIsSales() {
        return isSales;
    }

    public void setIsSales(String isSales) {
        this.isSales = isSales;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPrevReprocess() {
        return prevReprocess;
    }

    public void setPrevReprocess(String prevReprocess) {
        this.prevReprocess = prevReprocess;
    }

    public String getBookingNumber() {
        return bookingNumber;
    }

    public void setBookingNumber(String bookingNumber) {
        this.bookingNumber = bookingNumber;
    }

    public String getBookingWithoutOrder() {
        return bookingWithoutOrder;
    }

    public void setBookingWithoutOrder(String bookingWithoutOrder) {
        this.bookingWithoutOrder = bookingWithoutOrder;
    }

    public String getCboKnittingSource() {
        return cboKnittingSource;
    }

    public void setCboKnittingSource(String cboKnittingSource) {
        this.cboKnittingSource = cboKnittingSource;
    }

    public String getKnittingCompany() {
        return knittingCompany;
    }

    public void setKnittingCompany(String knittingCompany) {
        this.knittingCompany = knittingCompany;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    public String getShelfId() {
        return shelfId;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public FFRData(String isSales, String barcodeNo, String companyId, String buyerId, String rollNo, String rollId, String batchId, String batchNo, String determinationId, String bodypartId, String orderId, String construction, String composition, String color, String gsm, String dia, String qnty, String qtyInPcs, String itemSize, String rejectQnty, String reprocess, String prevReprocess, String greyWgt, String widthType, String productId, String greySysNumber, String bookingNumber, String bookingWithoutOrder, String cboKnittingSource, String knittingCompany, String locationId, String storeId, String floorId, String roomId, String rackId, String shelfId, String binId) {
        this.isSales = isSales;
        this.barcodeNo = barcodeNo;
        this.companyId = companyId;
        this.buyerId = buyerId;
        this.rollNo = rollNo;
        this.rollId = rollId;
        this.batchId = batchId;
        this.batchNo = batchNo;
        this.determinationId = determinationId;
        this.bodypartId = bodypartId;
        this.orderId = orderId;
        this.construction = construction;
        this.composition = composition;
        this.color = color;
        this.gsm = gsm;
        this.dia = dia;
        this.qnty = qnty;
        this.qtyInPcs = qtyInPcs;
        this.itemSize = itemSize;
        this.rejectQnty = rejectQnty;
        this.reprocess = reprocess;
        this.prevReprocess = prevReprocess;
        this.greyWgt = greyWgt;
        this.widthType = widthType;
        this.productId = productId;
        this.greySysNumber = greySysNumber;
        this.bookingNumber = bookingNumber;
        this.bookingWithoutOrder = bookingWithoutOrder;
        this.cboKnittingSource = cboKnittingSource;
        this.knittingCompany = knittingCompany;
        this.locationId = locationId;
        this.storeId = storeId;
        this.floorId = floorId;
        this.roomId = roomId;
        this.rackId = rackId;
        this.shelfId = shelfId;
        this.binId = binId;
    }
}
