package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_RFIDTransferModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public class Rfid {

        @SerializedName("rfid_id")
        @Expose
        private String rfidId;
        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("buyer_id")
        @Expose
        private String buyerId;
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
        @SerializedName("txt_yarn_brand")
        @Expose
        private String txtYarnBrand;
        @SerializedName("txt_rate")
        @Expose
        private String txtRate;
        @SerializedName("cbo_uom")
        @Expose
        private String cboUom;
        @SerializedName("rfid_no")
        @Expose
        private String rfidNo;
        @SerializedName("bag_weight")
        @Expose
        private String bagWeight;

        private boolean selected;

        public String getRfidId() {
            return rfidId;
        }

        public void setRfidId(String rfidId) {
            this.rfidId = rfidId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getBuyerId() {
            return buyerId;
        }

        public void setBuyerId(String buyerId) {
            this.buyerId = buyerId;
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

        public String getTxtYarnBrand() {
            return txtYarnBrand;
        }

        public void setTxtYarnBrand(String txtYarnBrand) {
            this.txtYarnBrand = txtYarnBrand;
        }

        public String getTxtRate() {
            return txtRate;
        }

        public void setTxtRate(String txtRate) {
            this.txtRate = txtRate;
        }

        public String getCboUom() {
            return cboUom;
        }

        public void setCboUom(String cboUom) {
            this.cboUom = cboUom;
        }

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

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
    public class Datum {

        @SerializedName("prod_id")
        @Expose
        private String prodId;
        @SerializedName("txt_item_desc")
        @Expose
        private String txtItemDesc;
        @SerializedName("txt_yarn_lot")
        @Expose
        private String txtYarnLot;
        @SerializedName("rfids")
        @Expose
        private List<Rfid> rfids;

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getTxtItemDesc() {
            return txtItemDesc;
        }

        public void setTxtItemDesc(String txtItemDesc) {
            this.txtItemDesc = txtItemDesc;
        }

        public String getTxtYarnLot() {
            return txtYarnLot;
        }

        public void setTxtYarnLot(String txtYarnLot) {
            this.txtYarnLot = txtYarnLot;
        }

        public List<Rfid> getRfids() {
            return rfids;
        }

        public void setRfids(List<Rfid> rfids) {
            this.rfids = rfids;
        }

    }
}
