package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class IssuePurposeModel {
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
        @SerializedName("PURPOSE_ID")
        private String PURPOSE_ID;

        @Expose
        @SerializedName("PURPOSE_NAME")
        private String PURPOSE_NAME;
        @Override
        public String toString() {
            return PURPOSE_NAME;
        }

        public String getPURPOSE_ID() {
            return PURPOSE_ID;
        }

        public void setPURPOSE_ID(String PURPOSE_ID) {
            this.PURPOSE_ID = PURPOSE_ID;
        }

        public String getPURPOSE_NAME() {
            return PURPOSE_NAME;
        }

        public void setPURPOSE_NAME(String PURPOSE_NAME) {
            this.PURPOSE_NAME = PURPOSE_NAME;
        }
    }
}
