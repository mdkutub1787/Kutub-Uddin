package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_ApkVersionResponse {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("app_name")
        @Expose
        private String appName;
        @SerializedName("version")
        @Expose
        private String version;
        @SerializedName("company_image")
        @Expose
        private String company_image;
        @SerializedName("app_url")
        @Expose
        private String appUrl;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }

        public String getCompany_image() {
            return company_image;
        }

        public void setCompany_image(String company_image) {
            this.company_image = company_image;
        }
    }
}
