package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class V1_BagReceiveResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private List<ResultSet> resultSet;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ResultSet> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<ResultSet> resultSet) {
        this.resultSet = resultSet;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public class ResultSet implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("item_category")
        @Expose
        private String itemCategory;
        @SerializedName("system_no")
        @Expose
        private String systemNo;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("store_id")
        @Expose
        private String storeId;
        @SerializedName("store_name")
        @Expose
        private String storeName;
        @SerializedName("bag_no")
        @Expose
        private String bagNo;
        @SerializedName("bag_keeping_id")
        @Expose
        private String bagKeepingId;
        @SerializedName("rfid_no")
        @Expose
        private String rfidNo;
        @SerializedName("qr_no")
        @Expose
        private String qrNo;
        @SerializedName("roll_qnty")
        @Expose
        private String rollQnty;
        @SerializedName("weight")
        @Expose
        private String weight;
        @SerializedName("uom")
        @Expose
        private String uom;
        @SerializedName("internal_ref")
        @Expose
        private String internalRef;
        @SerializedName("fab_color_id")
        @Expose
        private String fabColorId;
        @SerializedName("fab_color_name")
        @Expose
        private String fabColorName;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
        @SerializedName("buyer_name")
        @Expose
        private String buyerName;
        @SerializedName("room_id")
        @Expose
        private String roomId;
        @SerializedName("room_name")
        @Expose
        private String roomName;
        @SerializedName("rack_id")
        @Expose
        private String rackId;
        @SerializedName("rack_name")
        @Expose
        private String rackName;
        @SerializedName("shelf_id")
        @Expose
        private String shelfId;
        @SerializedName("shelf_name")
        @Expose
        private String shelfName;
        @SerializedName("bin_id")
        @Expose
        private String binId;
        @SerializedName("bin_name")
        @Expose
        private String binName;
        private String bagColorId;
        private String categoryId;
        private Boolean isChecked;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItemCategory() {
            return itemCategory;
        }

        public void setItemCategory(String itemCategory) {
            this.itemCategory = itemCategory;
        }

        public String getSystemNo() {
            return systemNo;
        }

        public void setSystemNo(String systemNo) {
            this.systemNo = systemNo;
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

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getBagNo() {
            return bagNo;
        }

        public void setBagNo(String bagNo) {
            this.bagNo = bagNo;
        }

        public String getBagKeepingId() {
            return bagKeepingId;
        }

        public void setBagKeepingId(String bagKeepingId) {
            this.bagKeepingId = bagKeepingId;
        }

        public String getRfidNo() {
            return rfidNo;
        }

        public void setRfidNo(String rfidNo) {
            this.rfidNo = rfidNo;
        }

        public String getQrNo() {
            return qrNo;
        }

        public void setQrNo(String qrNo) {
            this.qrNo = qrNo;
        }

        public String getRollQnty() {
            return rollQnty;
        }

        public void setRollQnty(String rollQnty) {
            this.rollQnty = rollQnty;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getUom() {
            return uom;
        }

        public void setUom(String uom) {
            this.uom = uom;
        }

        public String getInternalRef() {
            return internalRef;
        }

        public void setInternalRef(String internalRef) {
            this.internalRef = internalRef;
        }

        public String getFabColorId() {
            return fabColorId;
        }

        public void setFabColorId(String fabColorId) {
            this.fabColorId = fabColorId;
        }

        public String getFabColorName() {
            return fabColorName;
        }

        public void setFabColorName(String fabColorName) {
            this.fabColorName = fabColorName;
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

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getRoomName() {
            return roomName;
        }

        public void setRoomName(String roomName) {
            this.roomName = roomName;
        }

        public String getRackId() {
            return rackId;
        }

        public void setRackId(String rackId) {
            this.rackId = rackId;
        }

        public String getRackName() {
            return rackName;
        }

        public void setRackName(String rackName) {
            this.rackName = rackName;
        }

        public String getShelfId() {
            return shelfId;
        }

        public void setShelfId(String shelfId) {
            this.shelfId = shelfId;
        }

        public String getShelfName() {
            return shelfName;
        }

        public void setShelfName(String shelfName) {
            this.shelfName = shelfName;
        }

        public String getBinId() {
            return binId;
        }

        public void setBinId(String binId) {
            this.binId = binId;
        }

        public String getBinName() {
            return binName;
        }

        public void setBinName(String binName) {
            this.binName = binName;
        }

        public String getBagColorId() {
            return bagColorId;
        }

        public void setBagColorId(String bagColorId) {
            this.bagColorId = bagColorId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public Boolean getChecked() {
            return isChecked;
        }

        public void setChecked(Boolean checked) {
            isChecked = checked;
        }
    }
}
