package com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fff_save_response {
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
