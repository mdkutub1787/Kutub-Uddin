package com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DtlsPart implements Serializable {
    @SerializedName("QNTY")
    private String qNTY;
    @SerializedName("BARCODE_NO")
    private String bARCODENO;
    @SerializedName("PO_BREAKDOWN_ID")
    private String pOBREAKDOWNID;
    @SerializedName("ITEM_CATEGORY_ID")
    private String itemCategoryId;
    @SerializedName("ROLL_ID")
    private String rOLL_ID;
    @SerializedName("FLOOR_ID")
    private String fLOOR_ID;
    @SerializedName("ROOM_ID")
    private String rOOM_ID;
    @SerializedName("RACK_ID")
    private String rACK_ID;
    @SerializedName("SHELF_ID")
    private String sHELF_ID;
    @SerializedName("BIN_ID")
    private String bIN_ID;


    public String getQNTY() {
        return qNTY;
    }

    public String getBARCODENO() {
        return bARCODENO;
    }

    public String getPOBREAKDOWNID() {
        return pOBREAKDOWNID;
    }

    public String getItemCategoryId() {
        return itemCategoryId;
    }

    public void setItemCategoryId(String itemCategoryId) {
        this.itemCategoryId = itemCategoryId;
    }

    public String getROLLID() {
        return rOLL_ID;
    }

    public String getfLOOR_ID() {
        return fLOOR_ID;
    }

    public String getrOOM_ID() {
        return rOOM_ID;
    }

    public String getrACK_ID() {
        return rACK_ID;
    }

    public String getsHELF_ID() {
        return sHELF_ID;
    }

    public String getbIN_ID() {
        return bIN_ID;
    }
}