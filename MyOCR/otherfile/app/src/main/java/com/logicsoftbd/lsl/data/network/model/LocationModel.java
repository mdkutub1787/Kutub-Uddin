package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationModel {
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
        @SerializedName("ID")
        private String ID;

        @Expose
        @SerializedName("LOCATION_NAME")
        private String LOCATION_NAME;

        @Override
        public String toString() {
            return LOCATION_NAME;
        }

        public String getID() {
            return ID;
        }

        public void setID(String DEFECT_INCH_ID) {
            this.ID = DEFECT_INCH_ID;
        }

        public String getLOCATION_NAME() {
            return LOCATION_NAME;
        }

        public void setLOCATION_NAME(String STORE_NAME) {
            this.LOCATION_NAME = STORE_NAME;
        }
    }
}
