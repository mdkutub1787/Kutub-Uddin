package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_FinishLocationWiseFloorClass {
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

        @SerializedName("MasterPart")
        @Expose
        private List<MasterPart> masterPart;

        public List<MasterPart> getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(List<MasterPart> masterPart) {
            this.masterPart = masterPart;
        }

    }

    public class MasterPart {

        @SerializedName("ID")
        @Expose
        private Integer id;
        @SerializedName("FLOOR_NAME")
        @Expose
        private String floorName;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getFloorName() {
            return floorName;
        }

        public void setFloorName(String floorName) {
            this.floorName = floorName;
        }

    }
}
