package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FabricShade {
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
        @SerializedName("SHADE_ID")
        private Integer SHADE_ID;

        @Expose
        @SerializedName("FABRIC_SHADE")
        private String FABRIC_SHADE;

        @Override
        public String toString() {
            return FABRIC_SHADE;
        }

        public Integer getID() {
            return SHADE_ID;
        }

        public void setID(Integer SHADE_ID) {
            this.SHADE_ID = SHADE_ID;
        }

        public String getSTORE_NAME() {
            return FABRIC_SHADE;
        }

        public void setSTORE_NAME(String STORE_NAME) {
            this.FABRIC_SHADE = STORE_NAME;
        }
    }
}
