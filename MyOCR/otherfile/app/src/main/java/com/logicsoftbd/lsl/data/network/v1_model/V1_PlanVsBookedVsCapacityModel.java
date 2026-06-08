package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_PlanVsBookedVsCapacityModel {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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
    public class Datum {

        @SerializedName("company_id")
        @Expose
        private String companyId;
        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("location_id")
        @Expose
        private String locationId;
        @SerializedName("location_name")
        @Expose
        private String locationName;
        @SerializedName("month")
        @Expose
        private String month;
        @SerializedName("capacity_minute")
        @Expose
        private String capacityMinute;
        @SerializedName("plan_minute")
        @Expose
        private String planMinute;
        @SerializedName("booked_minute")
        @Expose
        private String bookedMinute;

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getLocationId() {
            return locationId;
        }

        public void setLocationId(String locationId) {
            this.locationId = locationId;
        }

        public String getLocationName() {
            return locationName;
        }

        public void setLocationName(String locationName) {
            this.locationName = locationName;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getCapacityMinute() {
            return capacityMinute;
        }

        public void setCapacityMinute(String capacityMinute) {
            this.capacityMinute = capacityMinute;
        }

        public String getPlanMinute() {
            return planMinute;
        }

        public void setPlanMinute(String planMinute) {
            this.planMinute = planMinute;
        }

        public String getBookedMinute() {
            return bookedMinute;
        }

        public void setBookedMinute(String bookedMinute) {
            this.bookedMinute = bookedMinute;
        }

    }
}
