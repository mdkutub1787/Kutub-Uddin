package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class IssueStoreModel {
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
        @SerializedName("STORE_NAME")
        private String STORE_NAME;

        @Override
        public String toString() {
            return STORE_NAME;
        }

        public String getID() {
            return ID;
        }

        public void setID(String DEFECT_INCH_ID) {
            this.ID = DEFECT_INCH_ID;
        }

        public String getSTORE_NAME() {
            return STORE_NAME;
        }

        public void setSTORE_NAME(String STORE_NAME) {
            this.STORE_NAME = STORE_NAME;
        }
    }
}