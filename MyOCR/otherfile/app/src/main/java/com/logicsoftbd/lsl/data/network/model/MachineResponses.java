package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MachineResponses implements Serializable {

    private boolean isFirst = true;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private List<Challan> data;

    public List<Challan> getData() {
        return data;
    }

    public void setData(List<Challan> data) {
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
        @SerializedName("ID")
        private int id;

        @Expose
        @SerializedName("MACHINE_NAME")
        private String machine;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMachine() {
            return machine;
        }

        public void setMachine(String machine) {
            this.machine = machine;
        }
    }
}
