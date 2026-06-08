package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DefectListModel {
    private boolean isFirst = false;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private  List<Result> resultset;

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

    public  List<Result> getData() {
        return resultset;
    }



    public static class Result implements Serializable {
        private boolean isFirst = false;
        @Expose
        @SerializedName("DEFECT_ID")
        private String DEFECT_ID;

        public boolean isFirst() {
            return isFirst;
        }

        public void setFirst(boolean first) {
            isFirst = first;
        }

        public String getDEFECT_ID() {
            return DEFECT_ID;
        }

        public void setDEFECT_ID(String DEFECT_ID) {
            this.DEFECT_ID = DEFECT_ID;
        }

        public String getDEFECT_NAME() {
            return DEFECT_NAME;
        }

        public void setDEFECT_NAME(String DEFECT_NAME) {
            this.DEFECT_NAME = DEFECT_NAME;
        }

        @Expose
        @SerializedName("DEFECT_NAME")
        private String DEFECT_NAME;

    }
}