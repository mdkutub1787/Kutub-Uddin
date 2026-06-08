package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_MenuResponse {

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

        @SerializedName("Line Wise Sewing Input Challan Wise")
        @Expose
        private List<LineWiseSewingInputChallanWise> lineWiseSewingInputChallanWise = null;
        @SerializedName("Grey Fabric Production")
        @Expose
        private List<GreyFabricProduction> greyFabricProduction = null;
        @SerializedName("Finish Fabric Production")
        @Expose
        private List<FinishFabricProduction> finishFabricProduction = null;
        @SerializedName("Report")
        @Expose
        private List<Report> report = null;

        public List<LineWiseSewingInputChallanWise> getLineWiseSewingInputChallanWise() {
            return lineWiseSewingInputChallanWise;
        }

        public void setLineWiseSewingInputChallanWise(List<LineWiseSewingInputChallanWise> lineWiseSewingInputChallanWise) {
            this.lineWiseSewingInputChallanWise = lineWiseSewingInputChallanWise;
        }

        public List<GreyFabricProduction> getGreyFabricProduction() {
            return greyFabricProduction;
        }

        public void setGreyFabricProduction(List<GreyFabricProduction> greyFabricProduction) {
            this.greyFabricProduction = greyFabricProduction;
        }

        public List<FinishFabricProduction> getFinishFabricProduction() {
            return finishFabricProduction;
        }

        public void setFinishFabricProduction(List<FinishFabricProduction> finishFabricProduction) {
            this.finishFabricProduction = finishFabricProduction;
        }

        public List<Report> getReport() {
            return report;
        }

        public void setReport(List<Report> report) {
            this.report = report;
        }

    }
    public class Report {

        @SerializedName("MENU")
        @Expose
        private String menu;
        @SerializedName("MENU_LINK")
        @Expose
        private String menuLink;
        @SerializedName("MENU_ID")
        @Expose
        private Integer menuId;
        @SerializedName("FULL_NAME")
        @Expose
        private String fullName;
        @SerializedName("USER_LOGIN_ID")
        @Expose
        private Integer userLoginId;
        @SerializedName("SLNO")
        @Expose
        private Integer slno;

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

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(Integer userLoginId) {
            this.userLoginId = userLoginId;
        }

        public Integer getSlno() {
            return slno;
        }

        public void setSlno(Integer slno) {
            this.slno = slno;
        }

    }
    public class GreyFabricProduction {

        @SerializedName("MENU")
        @Expose
        private String menu;
        @SerializedName("MENU_LINK")
        @Expose
        private String menuLink;
        @SerializedName("MENU_ID")
        @Expose
        private Integer menuId;
        @SerializedName("FULL_NAME")
        @Expose
        private String fullName;
        @SerializedName("USER_LOGIN_ID")
        @Expose
        private Integer userLoginId;
        @SerializedName("SLNO")
        @Expose
        private Integer slno;

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

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(Integer userLoginId) {
            this.userLoginId = userLoginId;
        }

        public Integer getSlno() {
            return slno;
        }

        public void setSlno(Integer slno) {
            this.slno = slno;
        }

    }

    public class LineWiseSewingInputChallanWise {

        @SerializedName("MENU")
        @Expose
        private String menu;
        @SerializedName("MENU_LINK")
        @Expose
        private String menuLink;
        @SerializedName("MENU_ID")
        @Expose
        private Integer menuId;
        @SerializedName("FULL_NAME")
        @Expose
        private String fullName;
        @SerializedName("USER_LOGIN_ID")
        @Expose
        private Integer userLoginId;
        @SerializedName("SLNO")
        @Expose
        private Integer slno;

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

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(Integer userLoginId) {
            this.userLoginId = userLoginId;
        }

        public Integer getSlno() {
            return slno;
        }

        public void setSlno(Integer slno) {
            this.slno = slno;
        }

    }
    public class FinishFabricProduction {

        @SerializedName("MENU")
        @Expose
        private String menu;
        @SerializedName("MENU_LINK")
        @Expose
        private String menuLink;
        @SerializedName("MENU_ID")
        @Expose
        private Integer menuId;
        @SerializedName("FULL_NAME")
        @Expose
        private String fullName;
        @SerializedName("USER_LOGIN_ID")
        @Expose
        private Integer userLoginId;
        @SerializedName("SLNO")
        @Expose
        private Integer slno;

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

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Integer getUserLoginId() {
            return userLoginId;
        }

        public void setUserLoginId(Integer userLoginId) {
            this.userLoginId = userLoginId;
        }

        public Integer getSlno() {
            return slno;
        }

        public void setSlno(Integer slno) {
            this.slno = slno;
        }

    }

}