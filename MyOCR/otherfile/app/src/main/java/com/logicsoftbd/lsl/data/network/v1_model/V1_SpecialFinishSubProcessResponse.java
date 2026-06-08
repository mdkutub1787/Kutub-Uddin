package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_SpecialFinishSubProcessResponse {
    @SerializedName("status")
    private Boolean status;

    @SerializedName("result_set")
    private List<SubProcess> resultSet;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<SubProcess> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<SubProcess> resultSet) {
        this.resultSet = resultSet;
    }

    public static class SubProcess {
        @SerializedName("ID")
        private Integer id;

        @SerializedName("PROCESS_NAME")
        private String processName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getProcessName() {
            return processName;
        }
        public void setProcessName(String processName) {
            this.processName = processName;
        }
    }
}