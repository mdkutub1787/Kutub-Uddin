package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_MenuModelClass {
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

        @SerializedName("menu_name")
        @Expose
        private String menuName;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("save")
        @Expose
        private Integer save;
        @SerializedName("update")
        @Expose
        private Integer update;
        @SerializedName("delete")
        @Expose
        private Integer delete;
        @SerializedName("show")
        @Expose
        private Integer show;
        @SerializedName("approve")
        @Expose
        private Integer approve;

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Integer getSave() {
            return save;
        }

        public void setSave(Integer save) {
            this.save = save;
        }

        public Integer getUpdate() {
            return update;
        }

        public void setUpdate(Integer update) {
            this.update = update;
        }

        public Integer getDelete() {
            return delete;
        }

        public void setDelete(Integer delete) {
            this.delete = delete;
        }

        public Integer getShow() {
            return show;
        }

        public void setShow(Integer show) {
            this.show = show;
        }

        public Integer getApprove() {
            return approve;
        }

        public void setApprove(Integer approve) {
            this.approve = approve;
        }

    }

}
