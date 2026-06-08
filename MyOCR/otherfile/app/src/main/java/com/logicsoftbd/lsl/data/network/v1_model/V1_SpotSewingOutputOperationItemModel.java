package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_SpotSewingOutputOperationItemModel {
    private String operationId;
    private String operationName;
    private Integer status;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
