package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShiftResponses implements Serializable {

    private boolean isFirst = true;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private List<ShiftResponses.Challan> data;

    public List<ShiftResponses.Challan> getData() {
        return data;
    }

    public void setData(List<ShiftResponses.Challan> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }



    public static class Challan implements Serializable{
        @Expose
        @SerializedName("SHIFT_ID")
        private int id;

        @Expose
        @SerializedName("SHIFT_NAME")
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShift() {
            return name;
        }

        public void setShift(String Shift) {
            this.name = Shift;
        }
    }
}

