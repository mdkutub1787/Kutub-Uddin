package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DyeingProdSaveResponse {

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

        @SerializedName("functional_batch")
        @Expose
        private String functionalBatch;
        @SerializedName("save_msg")
        @Expose
        private String saveMsg;

        public String getFunctionalBatch() {
            return functionalBatch;
        }

        public void setFunctionalBatch(String functionalBatch) {
            this.functionalBatch = functionalBatch;
        }

        public String getSaveMsg() {
            return saveMsg;
        }

        public void setSaveMsg(String saveMsg) {
            this.saveMsg = saveMsg;
        }

    }
}