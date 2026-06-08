package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_CuttingRollReceivePostResponse {
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

        @SerializedName("DUPLICATE_DATA")
        @Expose
        private String duplicateData;
        @SerializedName("SAVED_DATA")
        @Expose
        private String savedData;

        public String getDuplicateData() {
            return duplicateData;
        }

        public void setDuplicateData(String duplicateData) {
            this.duplicateData = duplicateData;
        }

        public String getSavedData() {
            return savedData;
        }

        public void setSavedData(String savedData) {
            this.savedData = savedData;
        }

    }

}
