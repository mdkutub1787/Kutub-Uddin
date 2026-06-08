package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class V1_YarnIssueReturnResponse implements Serializable {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Rfid implements Serializable{

        @SerializedName("RFID_NO")
        @Expose
        private String rfidNo;
        @SerializedName("BAG_WEIGHT")
        @Expose
        private String bagWeight;

        public String getRfidNo() {
            return rfidNo;
        }

        public void setRfidNo(String rfidNo) {
            this.rfidNo = rfidNo;
        }

        public String getBagWeight() {
            return bagWeight;
        }

        public void setBagWeight(String bagWeight) {
            this.bagWeight = bagWeight;
        }

    }

    public class DtlsIssueDetail implements Serializable{

        @SerializedName("prod_id")
        @Expose
        private String prodId;
        @SerializedName("unit_of_measure")
        @Expose
        private String unitOfMeasure;
        @SerializedName("avg_rate_per_unit")
        @Expose
        private String avgRatePerUnit;
        @SerializedName("issue_number")
        @Expose
        private String issueNumber;
        @SerializedName("year")
        @Expose
        private String year;
        @SerializedName("issue_date")
        @Expose
        private String issueDate;
        @SerializedName("issue_purpose_id")
        @Expose
        private String issuePurposeId;
        @SerializedName("issue_purpose")
        @Expose
        private String issuePurpose;
        @SerializedName("item_name_details")
        @Expose
        private String itemNameDetails;
        @SerializedName("lot_no")
        @Expose
        private String lotNo;
        @SerializedName("brand")
        @Expose
        private String brand;
        @SerializedName("dyeing_color")
        @Expose
        private String dyeingColor;
        @SerializedName("booking_req_dem_no")
        @Expose
        private String bookingReqDemNo;
        @SerializedName("store_id")
        @Expose
        private String storeId;
        @SerializedName("store")
        @Expose
        private String store;
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
        @SerializedName("challan_no")
        @Expose
        private String challanNo;
        @SerializedName("supplier_id")
        @Expose
        private String supplierId;
        @SerializedName("supplier")
        @Expose
        private String supplier;
        @SerializedName("issue_quantity")
        @Expose
        private String issueQuantity;
        @SerializedName("returnable_quantity")
        @Expose
        private String returnableQuantity;
        @SerializedName("NO_CONE")
        @Expose
        private String noCone;
        private Boolean selectedStatus;

        public Boolean getSelectedStatus() {
            return selectedStatus;
        }

        public void setSelectedStatus(Boolean selectedStatus) {
            this.selectedStatus = selectedStatus;
        }

        @SerializedName("RFID")
        @Expose
        private List<Rfid> rfid;

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getUnitOfMeasure() {
            return unitOfMeasure;
        }

        public void setUnitOfMeasure(String unitOfMeasure) {
            this.unitOfMeasure = unitOfMeasure;
        }

        public String getAvgRatePerUnit() {
            return avgRatePerUnit;
        }

        public void setAvgRatePerUnit(String avgRatePerUnit) {
            this.avgRatePerUnit = avgRatePerUnit;
        }

        public String getIssueNumber() {
            return issueNumber;
        }

        public void setIssueNumber(String issueNumber) {
            this.issueNumber = issueNumber;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getIssuePurposeId() {
            return issuePurposeId;
        }

        public void setIssuePurposeId(String issuePurposeId) {
            this.issuePurposeId = issuePurposeId;
        }

        public String getIssuePurpose() {
            return issuePurpose;
        }

        public void setIssuePurpose(String issuePurpose) {
            this.issuePurpose = issuePurpose;
        }

        public String getItemNameDetails() {
            return itemNameDetails;
        }

        public void setItemNameDetails(String itemNameDetails) {
            this.itemNameDetails = itemNameDetails;
        }

        public String getLotNo() {
            return lotNo;
        }

        public void setLotNo(String lotNo) {
            this.lotNo = lotNo;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getDyeingColor() {
            return dyeingColor;
        }

        public void setDyeingColor(String dyeingColor) {
            this.dyeingColor = dyeingColor;
        }

        public String getBookingReqDemNo() {
            return bookingReqDemNo;
        }

        public void setBookingReqDemNo(String bookingReqDemNo) {
            this.bookingReqDemNo = bookingReqDemNo;
        }

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
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

        public String getChallanNo() {
            return challanNo;
        }

        public void setChallanNo(String challanNo) {
            this.challanNo = challanNo;
        }

        public String getSupplierId() {
            return supplierId;
        }

        public void setSupplierId(String supplierId) {
            this.supplierId = supplierId;
        }

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public String getIssueQuantity() {
            return issueQuantity;
        }

        public void setIssueQuantity(String issueQuantity) {
            this.issueQuantity = issueQuantity;
        }

        public String getReturnableQuantity() {
            return returnableQuantity;
        }

        public void setReturnableQuantity(String returnableQuantity) {
            this.returnableQuantity = returnableQuantity;
        }

        public String getNoCone() {
            return noCone;
        }

        public void setNoCone(String noCone) {
            this.noCone = noCone;
        }

        public List<Rfid> getRfid() {
            return rfid;
        }

        public void setRfid(List<Rfid> rfid) {
            this.rfid = rfid;
        }

    }

    public class Data implements Serializable {

        @SerializedName("issue_id")
        @Expose
        private String issueId;
        @SerializedName("unit_of_measure")
        @Expose
        private String unitOfMeasure;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("basis_id")
        @Expose
        private String basisId;
        @SerializedName("basis")
        @Expose
        private String basis;
        @SerializedName("booking_req_dem_no")
        @Expose
        private String bookingReqDemNo;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("return_source_id")
        @Expose
        private String returnSourceId;
        @SerializedName("return_source")
        @Expose
        private String returnSource;
        @SerializedName("working_company_id")
        @Expose
        private String workingCompanyId;
        @SerializedName("working_company")
        @Expose
        private String workingCompany;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("dtls_issue_details")
        @Expose
        private List<DtlsIssueDetail> dtlsIssueDetails;

        public String getIssueId() {
            return issueId;
        }

        public void setIssueId(String issueId) {
            this.issueId = issueId;
        }

        public String getUnitOfMeasure() {
            return unitOfMeasure;
        }

        public void setUnitOfMeasure(String unitOfMeasure) {
            this.unitOfMeasure = unitOfMeasure;
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

        public String getBasisId() {
            return basisId;
        }

        public void setBasisId(String basisId) {
            this.basisId = basisId;
        }

        public String getBasis() {
            return basis;
        }

        public void setBasis(String basis) {
            this.basis = basis;
        }

        public String getBookingReqDemNo() {
            return bookingReqDemNo;
        }

        public void setBookingReqDemNo(String bookingReqDemNo) {
            this.bookingReqDemNo = bookingReqDemNo;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getReturnSourceId() {
            return returnSourceId;
        }

        public void setReturnSourceId(String returnSourceId) {
            this.returnSourceId = returnSourceId;
        }

        public String getReturnSource() {
            return returnSource;
        }

        public void setReturnSource(String returnSource) {
            this.returnSource = returnSource;
        }

        public String getWorkingCompanyId() {
            return workingCompanyId;
        }

        public void setWorkingCompanyId(String workingCompanyId) {
            this.workingCompanyId = workingCompanyId;
        }

        public String getWorkingCompany() {
            return workingCompany;
        }

        public void setWorkingCompany(String workingCompany) {
            this.workingCompany = workingCompany;
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

        public List<DtlsIssueDetail> getDtlsIssueDetails() {
            return dtlsIssueDetails;
        }

        public void setDtlsIssueDetails(List<DtlsIssueDetail> dtlsIssueDetails) {
            this.dtlsIssueDetails = dtlsIssueDetails;
        }

    }
}
