package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_LineWiseHourlyProductionResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("resultset")
    @Expose
    private Resultset resultset;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Resultset getResultset() {
        return resultset;
    }

    public void setResultset(Resultset resultset) {
        this.resultset = resultset;
    }
    public class Resultset {

        @SerializedName("home_data")
        @Expose
        private HomeData homeData;

        public HomeData getHomeData() {
            return homeData;
        }

        public void setHomeData(HomeData homeData) {
            this.homeData = homeData;
        }

    }
    public class HomeData {

        @SerializedName("hourly_target")
        @Expose
        private String hourlyTarget;
        @SerializedName("day_target")
        @Expose
        private String dayTarget;
        @SerializedName("msg")
        @Expose
        private String msg;
        @SerializedName("planned")
        @Expose
        private String planned;
        @SerializedName("day_total_qty")
        @Expose
        private String dayTotalQty;
        @SerializedName("today_input_qty")
        @Expose
        private String toDayInputQty;
        @SerializedName("cur_hour_qty")
        @Expose
        private String curHourQty;
        @SerializedName("check_qty")
        @Expose
        private String checkQty;
        @SerializedName("reject_qty")
        @Expose
        private String rejectQty;
        @SerializedName("alter_qty")
        @Expose
        private String alterQty;
        @SerializedName("spot_qty")
        @Expose
        private String spotQty;
        @SerializedName("varience")
        @Expose
        private String varience;
        @SerializedName("efficiency")
        @Expose
        private String efficiency;
        @SerializedName("efficiency_min")
        @Expose
        private String efficiency_min;
        @SerializedName("dhu")
        @Expose
        private String dhu;

        public String getHourlyTarget() {
            return hourlyTarget;
        }

        public void setHourlyTarget(String hourlyTarget) {
            this.hourlyTarget = hourlyTarget;
        }

        public String getDayTarget() {
            return dayTarget;
        }
        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getPlanned() {
            return planned;
        }

        public void setPlanned(String planned) {
            this.planned = planned;
        }
        public void setDayTarget(String dayTarget) {
            this.dayTarget = dayTarget;
        }

        public String getDayTotalQty() {
            return dayTotalQty;
        }

        public void setDayTotalQty(String dayTotalQty) {
            this.dayTotalQty = dayTotalQty;
        }

        public String getToDayInputQty() {
            return toDayInputQty;
        }

        public void setToDayInputQty(String toDayInputQty) {
            this.toDayInputQty = toDayInputQty;
        }

        public String getCurHourQty() {
            return curHourQty;
        }

        public String getCheckQty() {
            return checkQty;
        }

        public void setCheckQty(String checkQty) {
            this.checkQty = checkQty;
        }

        public void setCurHourQty(String curHourQty) {
            this.curHourQty = curHourQty;
        }

        public String getRejectQty() {
            return rejectQty;
        }

        public void setRejectQty(String rejectQty) {
            this.rejectQty = rejectQty;
        }

        public String getAlterQty() {
            return alterQty;
        }

        public void setAlterQty(String alterQty) {
            this.alterQty = alterQty;
        }

        public String getSpotQty() {
            return spotQty;
        }

        public void setSpotQty(String spotQty) {
            this.spotQty = spotQty;
        }

        public String getVarience() {
            return varience;
        }

        public void setVarience(String varience) {
            this.varience = varience;
        }

        public String getEfficiency() {
            return efficiency;
        }

        public void setEfficiency(String efficiency) {
            this.efficiency = efficiency;
        }

        public String getEfficiency_min() {
            return efficiency_min;
        }

        public void setEfficiency_min(String efficiency_min) {
            this.efficiency_min = efficiency_min;
        }

        public String getDhu() {
            return dhu;
        }

        public void setDhu(String dhu) {
            this.dhu = dhu;
        }

    }
}