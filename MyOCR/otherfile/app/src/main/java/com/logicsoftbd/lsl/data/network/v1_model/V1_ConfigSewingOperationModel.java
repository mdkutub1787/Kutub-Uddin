package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_ConfigSewingOperationModel implements Comparable<V1_ConfigSewingOperationModel> {
    @SerializedName("operationId")
    @Expose
    private String operationId;
    @SerializedName("operationName")
    @Expose
    private String operationName;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int compareTo(V1_ConfigSewingOperationModel o) {
        return (this.getOperationName().compareTo(o.operationName));
    }
}
