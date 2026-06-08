package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SlitteringSequzSaveResponse {

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

        @SerializedName("batch_id")
        @Expose
        private String batchId;
        @SerializedName("entry_form_no")
        @Expose
        private String entryFormNo;
        @SerializedName("mst_id")
        @Expose
        private String mstId;
        @SerializedName("save_msg")
        @Expose
        private String saveMsg;

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getEntryFormNo() {
            return entryFormNo;
        }

        public void setEntryFormNo(String entryFormNo) {
            this.entryFormNo = entryFormNo;
        }

        public String getMstId() {
            return mstId;
        }

        public void setMstId(String mstId) {
            this.mstId = mstId;
        }

        public String getSaveMsg() {
            return saveMsg;
        }

        public void setSaveMsg(String saveMsg) {
            this.saveMsg = saveMsg;
        }

    }
}