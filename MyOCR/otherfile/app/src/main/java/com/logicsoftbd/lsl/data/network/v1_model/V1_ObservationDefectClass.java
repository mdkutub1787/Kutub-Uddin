package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ObservationDefectClass {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class DataArr {

        @SerializedName("key")
        @Expose
        private String key;
        @SerializedName("value")
        @Expose
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public class Data {

        @SerializedName("data_arr")
        @Expose
        private List<DataArr> dataArr = null;

        public List<DataArr> getDataArr() {
            return dataArr;
        }

        public void setDataArr(List<DataArr> dataArr) {
            this.dataArr = dataArr;
        }

    }
}
