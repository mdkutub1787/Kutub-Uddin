package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_SewingInputPCSSaveResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private Resultset resultset;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Resultset getResultset() {
        return resultset;
    }

    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }

    public class Resultset {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("save_update_status")
        @Expose
        private Integer saveUpdateStatus;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getSaveUpdateStatus() {
            return saveUpdateStatus;
        }

        public void setSaveUpdateStatus(Integer saveUpdateStatus) {
            this.saveUpdateStatus = saveUpdateStatus;
        }

    }
}
