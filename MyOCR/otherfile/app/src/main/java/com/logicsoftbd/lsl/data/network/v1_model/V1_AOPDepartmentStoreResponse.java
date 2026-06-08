package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_AOPDepartmentStoreResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class Datum {

        @SerializedName("STORE_ID")
        @Expose
        private String storeId;
        @SerializedName("STORE_NAME")
        @Expose
        private String storeName;
        @SerializedName("ITEM_CATEGORY_ID")
        @Expose
        private String itemCategoryId;
        @SerializedName("STORE_LOCATION")
        @Expose
        private String storeLocation;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getItemCategoryId() {
            return itemCategoryId;
        }

        public void setItemCategoryId(String itemCategoryId) {
            this.itemCategoryId = itemCategoryId;
        }

        public String getStoreLocation() {
            return storeLocation;
        }

        public void setStoreLocation(String storeLocation) {
            this.storeLocation = storeLocation;
        }

    }
}
