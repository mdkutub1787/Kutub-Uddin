package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_ApprovalNotificationsModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

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

        @SerializedName("MENU")
        @Expose
        private String menu;
        @SerializedName("MENU_LINK")
        @Expose
        private String menuLink;
        @SerializedName("MENU_ID")
        @Expose
        private String menuId;
        @SerializedName("FULL_NAME")
        @Expose
        private String fullName;
        @SerializedName("USER_LOGIN_ID")
        @Expose
        private String userLoginId;
        @SerializedName("SLNO")
        @Expose
        private String slno;
        @SerializedName("NOTIFICATIONS")
        @Expose
        private String notifications;

        public String getMenu() {
            return menu;
        }

        public void setMenu(String menu) {
            this.menu = menu;
        }

        public String getMenuLink() {
            return menuLink;
        }

        public void setMenuLink(String menuLink) {
            this.menuLink = menuLink;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(String userLoginId) {
            this.userLoginId = userLoginId;
        }

        public String getSlno() {
            return slno;
        }

        public void setSlno(String slno) {
            this.slno = slno;
        }

        public String getNotifications() {
            return notifications;
        }

        public void setNotifications(String notifications) {
            this.notifications = notifications;
        }

    }
}