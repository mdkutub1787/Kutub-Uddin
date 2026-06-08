package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_FabricBagColorModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private List<ResultSet> resultSet;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ResultSet> getResultSet() {
        return resultSet;
    }

    public void setResultSet(List<ResultSet> resultSet) {
        this.resultSet = resultSet;
    }

    public class ResultSet {

        @SerializedName("color_id")
        @Expose
        private String colorId;
        @SerializedName("color_name")
        @Expose
        private String colorName;

        public String getColorId() {
            return colorId;
        }

        public void setColorId(String colorId) {
            this.colorId = colorId;
        }

        public String getColorName() {
            return colorName;
        }

        public void setColorName(String colorName) {
            this.colorName = colorName;
        }

    }
}
