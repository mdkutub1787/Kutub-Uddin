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

public class RollReceiveRequest implements Serializable {


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
            public String barcodeNo;

            @SerializedName("BUYER_ID")
            @Expose
            public Integer buyerId;

            @SerializedName("BUYER_NAME")
            @Expose
            public String buyerName;

            @SerializedName("PRODUCTION_BASIS")
            @Expose
            public Integer productionBasis;

            @SerializedName("PRODUCTION_BASIS_NAME")
            @Expose
            public String productionBasisName;

            @SerializedName("BOOKING_NO")
            @Expose
            public Integer bookingNo;

            @SerializedName("KNITTING_COMPANY")
            @Expose
            public Object knittingCompany;

            @SerializedName("BOOKING_WITHOUT_ORDER")
            @Expose
            public Integer bookingWithoutOrder;

            @SerializedName("BOOKING_ID")
            @Expose
            public Integer bookingId;

            @SerializedName("BODY_PART_ID")
            @Expose
            public Integer bodyPartId;

            @SerializedName("BODY_PART_NAME")
            @Expose
            public String bodyPartName;

            @SerializedName("YARN_LOT")
            @Expose
            public String yarnLot;

            @SerializedName("BRAND_ID")
            @Expose
            public Integer brandId;

            @SerializedName("SHIFT_NAME")
            @Expose
            public Integer shiftName;

            @SerializedName("FLOOR_ID")
            @Expose
            public Integer floorId;

            @SerializedName("MACHINE_NO_ID")
            @Expose
            public Integer machineNoId;

            @SerializedName("YARN_COUNT")
            @Expose
            public Object yarnCount;

            @SerializedName("COLOR_ID")
            @Expose
            public String colorId;

            @SerializedName("COLOR_NAME")
            @Expose
            public String colorName;

            @SerializedName("COLOR_RANGE_ID")
            @Expose
            public Integer colorRangeId;

            @SerializedName("COLOR_RANGE_NAME")
            @Expose
            public String colorRangeName;

            @SerializedName("ROLL_ID")
            @Expose
            public Integer rollId;

            @SerializedName("UOM")
            @Expose
            public Integer uom;

            @SerializedName("DTLS_ID")
            @Expose
            public Integer dtlsId;

            @SerializedName("PROD_ID")
            @Expose
            public Integer prodId;

            @SerializedName("DETER_ID")
            @Expose
            public Integer deterId;

            @SerializedName("CONSTRUCTION")
            @Expose
            public String construction;

            @SerializedName("COMPOSITION")
            @Expose
            public String composition;

            @SerializedName("GSM")
            @Expose
            public Integer gsm;

            @SerializedName("WIDTH")
            @Expose
            public Integer width;

            @SerializedName("STITCH_LENGTH")
            @Expose
            public String stitchLength;

            @SerializedName("ROLL_NO")
            @Expose
            public Integer rollNo;

            @SerializedName("PO_BREAKDOWN_ID")
            @Expose
            public Integer poBreakDownId;

            @SerializedName("PO_NUMBER")
            @Expose
            public String poNumber;

            @SerializedName("QNTY")
            @Expose
            public Integer qnty;

            @SerializedName("REJECT_QNTY")
            @Expose
            public Integer rejectQnty;

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

            public Integer getBookingId() {
                return bookingId;
            }

            public void setBookingId(Integer bookingId) {
                this.bookingId = bookingId;
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

            public Integer getFloorId() {
                return floorId;
            }

            public void setFloorId(Integer floorId) {
                this.floorId = floorId;
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
        }

    }
}
