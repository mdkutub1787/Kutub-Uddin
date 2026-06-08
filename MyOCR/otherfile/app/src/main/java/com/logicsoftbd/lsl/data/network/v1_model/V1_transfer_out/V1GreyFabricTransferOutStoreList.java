package com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out;

import com.google.gson.annotations.SerializedName;

public class V1GreyFabricTransferOutStoreList {
    @SerializedName("ITEM_CATEGORY_ID")
    private String iTEMCATEGORYID;

    @SerializedName("STORE_NAME")
    private String sTORENAME;

    @SerializedName("STORE_ID")
    private String sTOREID;

    public void setITEMCATEGORYID(String iTEMCATEGORYID){
        this.iTEMCATEGORYID = iTEMCATEGORYID;
    }

    public String getITEMCATEGORYID(){
        return iTEMCATEGORYID;
    }

    public void setSTORENAME(String sTORENAME){
        this.sTORENAME = sTORENAME;
    }

    public String getSTORENAME(){
        return sTORENAME;
    }

    public void setSTOREID(String sTOREID){
        this.sTOREID = sTOREID;
    }

    public String getSTOREID(){
        return sTOREID;
    }
}