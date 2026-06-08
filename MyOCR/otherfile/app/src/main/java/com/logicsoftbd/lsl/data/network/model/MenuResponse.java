/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by janisharali on 28/01/17.
 */

public class MenuResponse {

    @Expose
    @SerializedName("status")
    private boolean status;

    @Expose
    @SerializedName("resultset")
    public List<Menu> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Menu> getData() {
        return data;
    }

    public void setData(List<Menu> data) {
        this.data = data;
    }

    public static class Menu {

        @Expose
        @SerializedName("USER_ID")
        private int userId;

        @Expose
        @SerializedName("MENU")
        private String menu;

        @Expose
        @SerializedName("MENU_ID")
        private int menuId;

        @Expose
        @SerializedName("FULL_NAME")
        private String fullName;

        @Expose
        @SerializedName("USER_LOGIN_ID")
        private String userLoginId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getMenu() {
            return menu;
        }

        public void setMenu(String menu) {
            this.menu = menu;
        }

        public int getMenuId() {
            return menuId;
        }

        public void setMenuId(int menuId) {
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
    }
}
