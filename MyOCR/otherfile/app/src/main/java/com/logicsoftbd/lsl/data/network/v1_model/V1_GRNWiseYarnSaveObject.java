package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_GRNWiseYarnSaveObject {
    @SerializedName("MRR_ID")
    @Expose
    private String mrrId;
    @SerializedName("FLOOR_ROOM_RACK_DTLS_ID")
    @Expose
    private String floorRoomRackDtlsId;
    @SerializedName("USER_ID")
    @Expose
    private String userId;
    @SerializedName("RFIDS")
    @Expose
    private List<Rfid> rfids;

    public String getMrrId() {
        return mrrId;
    }

    public void setMrrId(String mrrId) {
        this.mrrId = mrrId;
    }

    public String getFloorRoomRackDtlsId() {
        return floorRoomRackDtlsId;
    }

    public void setFloorRoomRackDtlsId(String floorRoomRackDtlsId) {
        this.floorRoomRackDtlsId = floorRoomRackDtlsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Rfid> getRfids() {
        return rfids;
    }

    public void setRfids(List<Rfid> rfids) {
        this.rfids = rfids;
    }
}
