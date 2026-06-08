package com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FFRResponse implements Serializable{
    @SerializedName("data")
    @Expose
    private List<FFRData> barcodeList;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg() {
        return msg;
    }

    public List<FFRData> getBarcodeList() {
        return barcodeList;
    }
}
