package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_BundleWiseSewingOperationResponse {
    @SerializedName("result")
    @Expose
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("OPERATION_ID")
        @Expose
        private Integer operationId;
        @SerializedName("OPERATION_NAME")
        @Expose
        private String operationName;

        public Integer getOperationId() {
            return operationId;
        }

        public void setOperationId(Integer operationId) {
            this.operationId = operationId;
        }

        public String getOperationName() {
            return operationName;
        }

        public void setOperationName(String operationName) {
            this.operationName = operationName;
        }

    }
}
