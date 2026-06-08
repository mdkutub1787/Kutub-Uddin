package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_BundleWiseFloorWiseLineClass {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private List<Resultset> resultset = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Resultset> getResultset() {
        return resultset;
    }

    public void setResultset(List<Resultset> resultset) {
        this.resultset = resultset;
    }
    public class Resultset {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}
