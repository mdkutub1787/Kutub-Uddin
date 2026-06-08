package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DefectInchModel {
    private boolean isFirst = false;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private ArrayList<Result> data;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Result> getData() {
        return data;
    }

    public void setData(ArrayList<Result> data) {
        this.data = data;
    }

    public static class Result implements Serializable {

        @Expose
        @SerializedName("DEFECT_INCH_ID")
        private String DEFECT_INCH_ID;

        @Expose
        @SerializedName("DEFECT_INCH_NAME")
        private String DEFECT_INCH_NAME;

        public String getDEFECT_INCH_ID() {
            return DEFECT_INCH_ID;
        }

        public void setDEFECT_INCH_ID(String DEFECT_INCH_ID) {
            this.DEFECT_INCH_ID = DEFECT_INCH_ID;
        }

        public String getDEFECT_INCH_NAME() {
            return DEFECT_INCH_NAME;
        }

        public void setDEFECT_INCH_NAME(String DEFECT_INCH_NAME) {
            this.DEFECT_INCH_NAME = DEFECT_INCH_NAME;
        }
    }
}