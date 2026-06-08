/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by janisharali on 28/01/17.
 */

public class BarcodeIssueResponse implements Serializable {

    private boolean isFirst = true;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private Challan data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public Challan getData() {
        return data;
    }

    public void setData(Challan data) {
        this.data = data;
    }

    public static class Challan implements Serializable{

        @Expose
        @SerializedName("msg")
        private String msg;

        @Expose
        @SerializedName("status")
        private String status;

        @Expose
        @SerializedName("MasterPart")
        private MasterPart masterPart;

        @Expose
        @SerializedName("DtlsPart")
        private ProductBarcode productBarcodes;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public ProductBarcode getProductBarcodes() {
            return productBarcodes;
        }

        public void setProductBarcodes(ProductBarcode productBarcodes) {
            this.productBarcodes = productBarcodes;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public static class MasterPart implements Serializable {
            @Expose
            @SerializedName("COMPANY_ID")
            private int companyId;

            @Expose
            @SerializedName("DELIVERY_ID")
            private int deliveryId;

            @Expose
            @SerializedName("SYS_NUMBER_PREFIX_NUM")
            private int sysNumberPrefixNum;

            @Expose
            @SerializedName("SYS_NUMBER")
            private String sysNumber;

            @Expose
            @SerializedName("DELEVERY_DATE")
            private String deliveryDate;

            @Expose
            @SerializedName("KNITTING_SOURCE_ID")
            private int knittingSourceId;

            @Expose
            @SerializedName("KNITTING_COMPANY_ID")
            private int knittingCompanyId;

            @Expose
            @SerializedName("KNITTING_SOURCE")
            private String knittingSource;
            @Expose
            @SerializedName("KNITTING_COMPANY")
            private String knittingCompany;



            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public int getDeliveryId() {
                return deliveryId;
            }

            public void setDeliveryId(int deliveryId) {
                this.deliveryId = deliveryId;
            }

            public int getSysNumberPrefixNum() {
                return sysNumberPrefixNum;
            }

            public void setSysNumberPrefixNum(int sysNumberPrefixNum) {
                this.sysNumberPrefixNum = sysNumberPrefixNum;
            }

            public String getSysNumber() {
                return sysNumber;
            }

            public void setSysNumber(String sysNumber) {
                this.sysNumber = sysNumber;
            }

            public String getDeliveryDate() {
                return deliveryDate;
            }

            public void setDeliveryDate(String deliveryDate) {
                this.deliveryDate = deliveryDate;
            }

            public int getKnittingSourceId() {
                return knittingSourceId;
            }

            public void setKnittingSourceId(int knittingSourceId) {
                this.knittingSourceId = knittingSourceId;
            }

            public int getKnittingCompanyId() {
                return knittingCompanyId;
            }

            public void setKnittingCompanyId(int knittingCompanyId) {
                this.knittingCompanyId = knittingCompanyId;
            }

            public String getKnittingSource() {
                return knittingSource;
            }

            public void setKnittingSource(String knittingSource) {
                this.knittingSource = knittingSource;
            }

            public String getKnittingCompany() {
                return knittingCompany;
            }

            public void setKnittingCompany(String knittingCompany) {
                this.knittingCompany = knittingCompany;
            }
        }
        public static class ProductBarcode implements Serializable{
            @SerializedName("BARCODE_NO")
            @Expose
            private String barcodeNo;

            @SerializedName("BUYER_ID")
            @Expose
            private Integer buyerId;

            @SerializedName("BUYER_NAME")
            @Expose
            private String buyerName;

            @SerializedName("PRODUCTION_BASIS")
            @Expose
            private Integer productionBasis;

            @SerializedName("PRODUCTION_BASIS_NAME")
            @Expose
            private String productionBasisName;

            @SerializedName("BOOKING_NO")
            @Expose
            private Integer bookingNo;

            @SerializedName("KNITTING_COMPANY")
            @Expose
            private Object knittingCompany;

            @SerializedName("BOOKING_WITHOUT_ORDER")
            @Expose
            private Integer bookingWithoutOrder;

            @SerializedName("BOOKING_ID")
            @Expose
            private String bookingId;

            @SerializedName("SAMP_BOOKING")
            @Expose
            private String sampBooking;

            @SerializedName("BODY_PART_ID")
            @Expose
            private Integer bodyPartId;

            @SerializedName("BODY_PART_NAME")
            @Expose
            private String bodyPartName;

            @SerializedName("YARN_LOT")
            @Expose
            private String yarnLot;

            @SerializedName("BRAND_ID")
            @Expose
            private Integer brandId;

            @SerializedName("SHIFT_NAME")
            @Expose
            private Integer shiftName;

            @SerializedName("STORE_ID")
            @Expose
            private String storeId;
            @SerializedName("FLOOR_ID")
            @Expose
            private String floorId;
            @SerializedName("ROOM_ID")
            @Expose
            private String roomId;
            @SerializedName("RACK_ID")
            @Expose
            private String rackId;
            @SerializedName("SHELF_ID")
            @Expose
            private String shelfId;
            @SerializedName("BIN_BOX_ID")
            @Expose
            private String binBoxId;

            @SerializedName("MACHINE_NO_ID")
            @Expose
            private Integer machineNoId;

            @SerializedName("YARN_COUNT")
            @Expose
            private Object yarnCount;

            @SerializedName("COLOR_ID")
            @Expose
            private String colorId;

            @SerializedName("COLOR_NAME")
            @Expose
            private String colorName;

            @SerializedName("COLOR_RANGE_ID")
            @Expose
            private Integer colorRangeId;

            @SerializedName("COLOR_RANGE_NAME")
            @Expose
            private String colorRangeName;

            @SerializedName("ROLL_ID")
            @Expose
            private Integer rollId;

            @SerializedName("UOM")
            @Expose
            private Integer uom;

            @SerializedName("DTLS_ID")
            @Expose
            private Integer dtlsId;

            @SerializedName("PROD_ID")
            @Expose
            private Integer prodId;

            @SerializedName("DETER_ID")
            @Expose
            private Integer deterId;

            @SerializedName("CONSTRUCTION")
            @Expose
            private String construction;

            @SerializedName("COMPOSITION")
            @Expose
            private String composition;

            @SerializedName("GSM")
            @Expose
            private Integer gsm;

            @SerializedName("WIDTH")
            @Expose
            private Integer width;

            @SerializedName("STITCH_LENGTH")
            @Expose
            private String stitchLength;

            @SerializedName("ROLL_NO")
            @Expose
            private Integer rollNo;

            @SerializedName("PO_BREAKDOWN_ID")
            @Expose
            private Integer poBreakDownId;

            @SerializedName("PO_NUMBER")
            @Expose
            private String poNumber;

            @SerializedName("QNTY")
            @Expose
            private Integer qnty;

            @SerializedName("REJECT_QNTY")
            @Expose
            private Integer rejectQnty;

            @SerializedName("JOB_NO")
            @Expose
            private String jobNo;

            @SerializedName("YARN_RATE")
            @Expose
            private String yarnRate;
            @SerializedName("KNITING_CHARGE")
            @Expose
            private String knitingCharge;
            @SerializedName("ROLL_RATE")
            @Expose
            private String rollRate;
            @SerializedName("IS_SALES")
            @Expose
            private String isSales;

            @SerializedName("USER_ID")
            @Expose
            private String userId;


            public String getBarcodeNo() {
                return barcodeNo;
            }

            public void setBarcodeNo(String barcodeNo) {
                this.barcodeNo = barcodeNo;
            }

            public Integer getBuyerId() {
                return buyerId;
            }

            public void setBuyerId(Integer buyerId) {
                this.buyerId = buyerId;
            }

            public String getBuyerName() {
                return buyerName;
            }

            public void setBuyerName(String buyerName) {
                this.buyerName = buyerName;
            }

            public Integer getProductionBasis() {
                return productionBasis;
            }

            public void setProductionBasis(Integer productionBasis) {
                this.productionBasis = productionBasis;
            }

            public String getProductionBasisName() {
                return productionBasisName;
            }

            public void setProductionBasisName(String productionBasisName) {
                this.productionBasisName = productionBasisName;
            }

            public Integer getBookingNo() {
                return bookingNo;
            }

            public void setBookingNo(Integer bookingNo) {
                this.bookingNo = bookingNo;
            }

            public Object getKnittingCompany() {
                return knittingCompany;
            }

            public void setKnittingCompany(Object knittingCompany) {
                this.knittingCompany = knittingCompany;
            }

            public Integer getBookingWithoutOrder() {
                return bookingWithoutOrder;
            }

            public void setBookingWithoutOrder(Integer bookingWithoutOrder) {
                this.bookingWithoutOrder = bookingWithoutOrder;
            }

            public String getBookingId() {
                return bookingId;
            }

            public void setBookingId(String bookingId) {
                this.bookingId = bookingId;
            }

            public String getSampBooking() {
                return sampBooking;
            }

            public void setSampBooking(String sampBooking) {
                this.sampBooking = sampBooking;
            }

            public Integer getBodyPartId() {
                return bodyPartId;
            }

            public void setBodyPartId(Integer bodyPartId) {
                this.bodyPartId = bodyPartId;
            }

            public String getBodyPartName() {
                return bodyPartName;
            }

            public void setBodyPartName(String bodyPartName) {
                this.bodyPartName = bodyPartName;
            }

            public String getYarnLot() {
                return yarnLot;
            }

            public void setYarnLot(String yarnLot) {
                this.yarnLot = yarnLot;
            }

            public Integer getBrandId() {
                return brandId;
            }

            public void setBrandId(Integer brandId) {
                this.brandId = brandId;
            }

            public Integer getShiftName() {
                return shiftName;
            }

            public void setShiftName(Integer shiftName) {
                this.shiftName = shiftName;
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

            public String getBinBoxId() {
                return binBoxId;
            }

            public void setBinBoxId(String binBoxId) {
                this.binBoxId = binBoxId;
            }

            public Integer getMachineNoId() {
                return machineNoId;
            }

            public void setMachineNoId(Integer machineNoId) {
                this.machineNoId = machineNoId;
            }

            public Object getYarnCount() {
                return yarnCount;
            }

            public void setYarnCount(Object yarnCount) {
                this.yarnCount = yarnCount;
            }

            public String getColorId() {
                return colorId;
            }

            public void setColorId(String colorId) {
                this.colorId = colorId;
            }

            public String getColorName() {
                return colorName;
            }

            public void setColorName(String colorName) {
                this.colorName = colorName;
            }

            public Integer getColorRangeId() {
                return colorRangeId;
            }

            public void setColorRangeId(Integer colorRangeId) {
                this.colorRangeId = colorRangeId;
            }

            public String getColorRangeName() {
                return colorRangeName;
            }

            public void setColorRangeName(String colorRangeName) {
                this.colorRangeName = colorRangeName;
            }

            public Integer getRollId() {
                return rollId;
            }

            public void setRollId(Integer rollId) {
                this.rollId = rollId;
            }

            public Integer getUom() {
                return uom;
            }

            public void setUom(Integer uom) {
                this.uom = uom;
            }

            public Integer getDtlsId() {
                return dtlsId;
            }

            public void setDtlsId(Integer dtlsId) {
                this.dtlsId = dtlsId;
            }

            public Integer getProdId() {
                return prodId;
            }

            public void setProdId(Integer prodId) {
                this.prodId = prodId;
            }

            public Integer getDeterId() {
                return deterId;
            }

            public void setDeterId(Integer deterId) {
                this.deterId = deterId;
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

            public Integer getGsm() {
                return gsm;
            }

            public void setGsm(Integer gsm) {
                this.gsm = gsm;
            }

            public Integer getWidth() {
                return width;
            }

            public void setWidth(Integer width) {
                this.width = width;
            }

            public String getStitchLength() {
                return stitchLength;
            }

            public void setStitchLength(String stitchLength) {
                this.stitchLength = stitchLength;
            }

            public Integer getRollNo() {
                return rollNo;
            }

            public void setRollNo(Integer rollNo) {
                this.rollNo = rollNo;
            }

            public Integer getPoBreakDownId() {
                return poBreakDownId;
            }

            public void setPoBreakDownId(Integer poBreakDownId) {
                this.poBreakDownId = poBreakDownId;
            }

            public String getPoNumber() {
                return poNumber;
            }

            public void setPoNumber(String poNumber) {
                this.poNumber = poNumber;
            }

            public Integer getQnty() {
                return qnty;
            }

            public void setQnty(Integer qnty) {
                this.qnty = qnty;
            }

            public Integer getRejectQnty() {
                return rejectQnty;
            }

            public void setRejectQnty(Integer rejectQnty) {
                this.rejectQnty = rejectQnty;
            }

            public String getJobNo() {
                return jobNo;
            }

            public void setJobNo(String jobNo) {
                this.jobNo = jobNo;
            }

            public String getYarnRate() {
                return yarnRate;
            }

            public void setYarnRate(String yarnRate) {
                this.yarnRate = yarnRate;
            }

            public String getKnitingCharge() {
                return knitingCharge;
            }

            public void setKnitingCharge(String knitingCharge) {
                this.knitingCharge = knitingCharge;
            }

            public String getRollRate() {
                return rollRate;
            }

            public void setRollRate(String rollRate) {
                this.rollRate = rollRate;
            }

            public String getIsSales() {
                return isSales;
            }

            public void setIsSales(String isSales) {
                this.isSales = isSales;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

    }
}
