package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class V1_BagChallanReceiveResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private List<V1_BagReceiveResponse.ResultSet> resultSet;
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

    public List<V1_BagReceiveResponse.ResultSet> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<V1_BagReceiveResponse.ResultSet> resultSet) {
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
}
