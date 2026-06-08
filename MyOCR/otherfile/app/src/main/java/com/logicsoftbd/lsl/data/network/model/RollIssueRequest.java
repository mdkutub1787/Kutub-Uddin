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
import java.util.List;

/**
 * Created by janisharali on 28/01/17.
 */

public class RollIssueRequest implements Serializable {


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


    public Challan getData() {
        return data;
    }

    public void setData(Challan data) {
        this.data = data;
    }

    public static class Challan implements Serializable{

        @Expose
        @SerializedName("MasterPart")
        private MasterPart masterPart;

        @Expose
        @SerializedName("DtlsPart")
        private List<ProductBarcode> productBarcodes;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public List<ProductBarcode> getProductBarcodes() {
            return productBarcodes;
        }

        public void setProductBarcodes(List<ProductBarcode> productBarcodes) {
            this.productBarcodes = productBarcodes;
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
            @SerializedName("STORE_ID")
            private Integer storeId;

            @Expose
            @SerializedName("ISSUE_PURPOSE")
            private Integer issuePurpose;

            @Expose
            @SerializedName("LOCATION_ID")
            private int locationId;



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

            public Integer getStoreId() {
                return storeId;
            }

            public void setStoreId(Integer storeId) {
                this.storeId = storeId;
            }

            public Integer getIssuePurpose() {
                return issuePurpose;
            }

            public void setIssuePurpose(Integer issuePurpose) {
                this.issuePurpose = issuePurpose;
            }

            public int getLocationId() {
                return locationId;
            }

            public void setLocationId(int locationId) {
                this.locationId = locationId;
            }
        }
        public static class ProductBarcode implements Serializable{
            @SerializedName("BARCODE_NO")
            @Expose
            private String barcodeNo;
            @SerializedName("BUYER_ID")
            @Expose
            private String buyerId;
            @SerializedName("BUYER_NAME")
            @Expose
            private String buyerName;
            @SerializedName("PRODUCTION_BASIS")
            @Expose
            public String productionBasis;

            @SerializedName("PRODUCTION_BASIS_NAME")
            @Expose
            public String productionBasisName;

            @SerializedName("BOOKING_NO")
            @Expose
            public String bookingNo;
            @SerializedName("KNITTING_COMPANY")
            @Expose
            private String knittingCompany;
            @SerializedName("BOOKING_WITHOUT_ORDER")
            @Expose
            private String bookingWithoutOrder;
            @SerializedName("BOOKING_ID")
            @Expose
            private String bookingId;
            @SerializedName("SAMP_BOOKING")
            @Expose
            private String sampBooking;
            @SerializedName("BODY_PART_ID")
            @Expose
            private String bodyPartId;
            @SerializedName("BODY_PART_NAME")
            @Expose
            private String bodyPartName;
            @SerializedName("YARN_LOT")
            @Expose
            private String yarnLot;
            @SerializedName("BRAND_ID")
            @Expose
            private String brandId;
            @SerializedName("SHIFT_NAME")
            @Expose
            private String shiftName;
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
            private String machineNoId;
            @SerializedName("YARN_COUNT")
            @Expose
            private String yarnCount;
            @SerializedName("COLOR_ID")
            @Expose
            private String colorId;
            @SerializedName("COLOR_NAME")
            @Expose
            private String colorName;
            @SerializedName("COLOR_RANGE_ID")
            @Expose
            private String colorRangeId;
            @SerializedName("COLOR_RANGE_NAME")
            @Expose
            private String colorRangeName;
            @SerializedName("ROLL_ID")
            @Expose
            private String rollId;
            @SerializedName("UOM")
            @Expose
            private String uom;
            @SerializedName("DTLS_ID")
            @Expose
            private String dtlsId;
            @SerializedName("PROD_ID")
            @Expose
            private String prodId;
            @SerializedName("DETER_ID")
            @Expose
            private String deterId;
            @SerializedName("CONSTRUCTION")
            @Expose
            private String construction;
            @SerializedName("COMPOSITION")
            @Expose
            private String composition;
            @SerializedName("GSM")
            @Expose
            private String gsm;
            @SerializedName("WIDTH")
            @Expose
            private String width;
            @SerializedName("STITCH_LENGTH")
            @Expose
            private String stitchLength;
            @SerializedName("ROLL_NO")
            @Expose
            private String rollNo;
            @SerializedName("PO_BREAKDOWN_ID")
            @Expose
            public String poBreakDownId;
            @SerializedName("PO_NUMBER")
            @Expose
            private String poNumber;
            @SerializedName("JOB_NO")
            @Expose
            private String jobNo;
            @SerializedName("QNTY")
            @Expose
            private String qnty;
            @SerializedName("REJECT_QNTY")
            @Expose
            private String rejectQnty;
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

            public String getProductionBasis() {
                return productionBasis;
            }

            public void setProductionBasis(String productionBasis) {
                this.productionBasis = productionBasis;
            }

            public String getProductionBasisName() {
                return productionBasisName;
            }

            public void setProductionBasisName(String productionBasisName) {
                this.productionBasisName = productionBasisName;
            }

            public String getBookingNo() {
                return bookingNo;
            }

            public void setBookingNo(String bookingNo) {
                this.bookingNo = bookingNo;
            }

            public String getKnittingCompany() {
                return knittingCompany;
            }

            public void setKnittingCompany(String knittingCompany) {
                this.knittingCompany = knittingCompany;
            }

            public String getBookingWithoutOrder() {
                return bookingWithoutOrder;
            }

            public void setBookingWithoutOrder(String bookingWithoutOrder) {
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

            public String getBodyPartId() {
                return bodyPartId;
            }

            public void setBodyPartId(String bodyPartId) {
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

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getShiftName() {
                return shiftName;
            }

            public void setShiftName(String shiftName) {
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

            public String getMachineNoId() {
                return machineNoId;
            }

            public void setMachineNoId(String machineNoId) {
                this.machineNoId = machineNoId;
            }

            public String getYarnCount() {
                return yarnCount;
            }

            public void setYarnCount(String yarnCount) {
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

            public String getColorRangeId() {
                return colorRangeId;
            }

            public void setColorRangeId(String colorRangeId) {
                this.colorRangeId = colorRangeId;
            }

            public String getColorRangeName() {
                return colorRangeName;
            }

            public void setColorRangeName(String colorRangeName) {
                this.colorRangeName = colorRangeName;
            }

            public String getRollId() {
                return rollId;
            }

            public void setRollId(String rollId) {
                this.rollId = rollId;
            }

            public String getUom() {
                return uom;
            }

            public void setUom(String uom) {
                this.uom = uom;
            }

            public String getDtlsId() {
                return dtlsId;
            }

            public void setDtlsId(String dtlsId) {
                this.dtlsId = dtlsId;
            }

            public String getProdId() {
                return prodId;
            }

            public void setProdId(String prodId) {
                this.prodId = prodId;
            }

            public String getDeterId() {
                return deterId;
            }

            public void setDeterId(String deterId) {
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

            public String getGsm() {
                return gsm;
            }

            public void setGsm(String gsm) {
                this.gsm = gsm;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getStitchLength() {
                return stitchLength;
            }

            public void setStitchLength(String stitchLength) {
                this.stitchLength = stitchLength;
            }

            public String getRollNo() {
                return rollNo;
            }

            public void setRollNo(String rollNo) {
                this.rollNo = rollNo;
            }

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

            public String getJobNo() {
                return jobNo;
            }

            public void setJobNo(String jobNo) {
                this.jobNo = jobNo;
            }

            public String getQnty() {
                return qnty;
            }

            public void setQnty(String qnty) {
                this.qnty = qnty;
            }

            public String getRejectQnty() {
                return rejectQnty;
            }

            public void setRejectQnty(String rejectQnty) {
                this.rejectQnty = rejectQnty;
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
