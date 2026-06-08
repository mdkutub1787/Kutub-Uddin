package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ShiftResponse {
    @SerializedName("isFirst")
    @Expose
    private Boolean isFirst;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("resultset")
    @Expose
    private List<Resultset> resultset;

    public Boolean getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Boolean isFirst) {
        this.isFirst = isFirst;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Resultset> getResultset() {
        return resultset;
    }

    public void setResultset(List<Resultset> resultset) {
        this.resultset = resultset;
    }

    public class Resultset {

        @SerializedName("SHIFT_ID")
        @Expose
        private Integer shiftId;
        @SerializedName("SHIFT_NAME")
        @Expose
        private String shiftName;

        public Integer getShiftId() {
            return shiftId;
        }

        public void setShiftId(Integer shiftId) {
            this.shiftId = shiftId;
        }

        public String getShiftName() {
            return shiftName;
        }

        public void setShiftName(String shiftName) {
            this.shiftName = shiftName;
        }

    }
}
