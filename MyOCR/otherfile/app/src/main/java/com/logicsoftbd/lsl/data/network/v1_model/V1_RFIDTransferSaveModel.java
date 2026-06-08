package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_RFIDTransferSaveModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("cbo_company_id")
    @Expose
    private String cboCompanyId;
    @SerializedName("cbo_store_name")
    @Expose
    private String cboStoreName;
    @SerializedName("cbo_floor")
    @Expose
    private String cboFloor;
    @SerializedName("cbo_room")
    @Expose
    private String cboRoom;
    @SerializedName("txt_rack")
    @Expose
    private String txtRack;
    @SerializedName("txt_shelf")
    @Expose
    private String txtShelf;
    @SerializedName("cbo_bin")
    @Expose
    private String cboBin;
    @SerializedName("cbo_store_name_to")
    @Expose
    private String cboStoreNameTo;
    @SerializedName("cbo_floor_to")
    @Expose
    private String cboFloorTo;
    @SerializedName("cbo_room_to")
    @Expose
    private String cboRoomTo;
    @SerializedName("txt_rack_to")
    @Expose
    private String txtRackTo;
    @SerializedName("txt_shelf_to")
    @Expose
    private String txtShelfTo;
    @SerializedName("cbo_bin_to")
    @Expose
    private String cboBinTo;
    @SerializedName("rfid")
    @Expose
    private List<Rfid> rfid;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCboCompanyId() {
        return cboCompanyId;
    }

    public void setCboCompanyId(String cboCompanyId) {
        this.cboCompanyId = cboCompanyId;
    }

    public String getCboStoreName() {
        return cboStoreName;
    }

    public void setCboStoreName(String cboStoreName) {
        this.cboStoreName = cboStoreName;
    }

    public String getCboFloor() {
        return cboFloor;
    }

    public void setCboFloor(String cboFloor) {
        this.cboFloor = cboFloor;
    }

    public String getCboRoom() {
        return cboRoom;
    }

    public void setCboRoom(String cboRoom) {
        this.cboRoom = cboRoom;
    }

    public String getTxtRack() {
        return txtRack;
    }

    public void setTxtRack(String txtRack) {
        this.txtRack = txtRack;
    }

    public String getTxtShelf() {
        return txtShelf;
    }

    public void setTxtShelf(String txtShelf) {
        this.txtShelf = txtShelf;
    }

    public String getCboBin() {
        return cboBin;
    }

    public void setCboBin(String cboBin) {
        this.cboBin = cboBin;
    }

    public String getCboStoreNameTo() {
        return cboStoreNameTo;
    }

    public void setCboStoreNameTo(String cboStoreNameTo) {
        this.cboStoreNameTo = cboStoreNameTo;
    }

    public String getCboFloorTo() {
        return cboFloorTo;
    }

    public void setCboFloorTo(String cboFloorTo) {
        this.cboFloorTo = cboFloorTo;
    }

    public String getCboRoomTo() {
        return cboRoomTo;
    }

    public void setCboRoomTo(String cboRoomTo) {
        this.cboRoomTo = cboRoomTo;
    }

    public String getTxtRackTo() {
        return txtRackTo;
    }

    public void setTxtRackTo(String txtRackTo) {
        this.txtRackTo = txtRackTo;
    }

    public String getTxtShelfTo() {
        return txtShelfTo;
    }

    public void setTxtShelfTo(String txtShelfTo) {
        this.txtShelfTo = txtShelfTo;
    }

    public String getCboBinTo() {
        return cboBinTo;
    }

    public void setCboBinTo(String cboBinTo) {
        this.cboBinTo = cboBinTo;
    }

    public List<Rfid> getRfid() {
        return rfid;
    }

    public void setRfid(List<Rfid> rfid) {
        this.rfid = rfid;
    }
    public static class Rfid {

        @SerializedName("epcid")
        @Expose
        private String epcid;
        @SerializedName("bag_weight")
        @Expose
        private String bagWeight;
        @SerializedName("cbo_uom")
        @Expose
        private String cboUom;
        @SerializedName("txt_rate")
        @Expose
        private String txtRate;
        @SerializedName("txt_yarn_brand")
        @Expose
        private String txtYarnBrand;
        @SerializedName("txt_item_desc")
        @Expose
        private String txtItemDesc;
        @SerializedName("txt_challan_no")
        @Expose
        private String txtChallanNo;
        @SerializedName("cbo_purpose")
        @Expose
        private String cboPurpose;
        @SerializedName("txt_yarn_lot")
        @Expose
        private String txtYarnLot;
        @SerializedName("prod_id")
        @Expose
        private String prodId;
        @SerializedName("rfid_id")
        @Expose
        private String rfidId;

        public String getEpcid() {
            return epcid;
        }

        public void setEpcid(String epcid) {
            this.epcid = epcid;
        }

        public String getBagWeight() {
            return bagWeight;
        }

        public void setBagWeight(String bagWeight) {
            this.bagWeight = bagWeight;
        }

        public String getCboUom() {
            return cboUom;
        }

        public void setCboUom(String cboUom) {
            this.cboUom = cboUom;
        }

        public String getTxtRate() {
            return txtRate;
        }

        public void setTxtRate(String txtRate) {
            this.txtRate = txtRate;
        }

        public String getTxtYarnBrand() {
            return txtYarnBrand;
        }

        public void setTxtYarnBrand(String txtYarnBrand) {
            this.txtYarnBrand = txtYarnBrand;
        }

        public String getTxtItemDesc() {
            return txtItemDesc;
        }

        public void setTxtItemDesc(String txtItemDesc) {
            this.txtItemDesc = txtItemDesc;
        }

        public String getTxtChallanNo() {
            return txtChallanNo;
        }

        public void setTxtChallanNo(String txtChallanNo) {
            this.txtChallanNo = txtChallanNo;
        }

        public String getCboPurpose() {
            return cboPurpose;
        }

        public void setCboPurpose(String cboPurpose) {
            this.cboPurpose = cboPurpose;
        }

        public String getTxtYarnLot() {
            return txtYarnLot;
        }

        public void setTxtYarnLot(String txtYarnLot) {
            this.txtYarnLot = txtYarnLot;
        }

        public String getProdId() {
            return prodId;
        }

        public void setProdId(String prodId) {
            this.prodId = prodId;
        }

        public String getRfidId() {
            return rfidId;
        }

        public void setRfidId(String rfidId) {
            this.rfidId = rfidId;
        }
    }
}
