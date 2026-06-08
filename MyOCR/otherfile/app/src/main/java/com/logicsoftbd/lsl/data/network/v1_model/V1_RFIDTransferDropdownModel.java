package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_RFIDTransferDropdownModel {

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
    public class Datum {

        @SerializedName("STORE_ID")
        @Expose
        private String storeId;
        @SerializedName("STORE_NAME")
        @Expose
        private String storeName;
        @SerializedName("FLOOR_ID")
        @Expose
        private String floorId;
        @SerializedName("FLOOR_NAME")
        @Expose
        private String floorName;
        @SerializedName("ROOM_ID")
        @Expose
        private String roomId;
        @SerializedName("ROOM_NAME")
        @Expose
        private String roomName;
        @SerializedName("RACK_ID")
        @Expose
        private String rackId;
        @SerializedName("RACK_NAME")
        @Expose
        private String rackName;
        @SerializedName("SHELF_ID")
        @Expose
        private String shelfId;
        @SerializedName("SHELF_NAME")
        @Expose
        private String shelfName;
        @SerializedName("BIN_ID")
        @Expose
        private String binId;
        @SerializedName("BIN_NAME")
        @Expose
        private String binName;

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

        public String getFloorId() {
            return floorId;
        }

        public void setFloorId(String floorId) {
            this.floorId = floorId;
        }

        public String getFloorName() {
            return floorName;
        }

        public void setFloorName(String floorName) {
            this.floorName = floorName;
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

    }
}
