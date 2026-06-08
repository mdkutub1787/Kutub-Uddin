package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class V1_LoginResponse {
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

        @SerializedName("module")
        @Expose
        private List<Module> module = null;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("user_id")
        @Expose
        private String user_id;
        @SerializedName("company_id")
        @Expose
        private String company_id;

        @SerializedName("pro_variable")
        @Expose
        private List<ProVariable> proVariable;


        @SerializedName("msg")
        @Expose
        private String msg;

        @SerializedName("reference_data")
        @Expose
        private String reference_data;

        public List<Module> getModule() {
            return module;
        }

        public void setModule(List<Module> module) {
            this.module = module;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getReference_data() {
            return reference_data;
        }

        public void setReference_data(String reference_data) {
            this.reference_data = reference_data;
        }
        public List<ProVariable> getProVariable() {
            return proVariable;
        }

        public void setProVariable(List<ProVariable> proVariable) {
            this.proVariable = proVariable;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }
    }
    public class ModuleWiseMenu {

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
    public class Module {

        @SerializedName("module_name")
        @Expose
        private String moduleName;
        @SerializedName("module_wise_menu")
        @Expose
        private List<ModuleWiseMenu> moduleWiseMenu = null;

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public List<ModuleWiseMenu> getModuleWiseMenu() {
            return moduleWiseMenu;
        }

        public void setModuleWiseMenu(List<ModuleWiseMenu> moduleWiseMenu) {
            this.moduleWiseMenu = moduleWiseMenu;
        }

    }

    public class ProVariable implements Serializable {

        @SerializedName("COMPANY_ID")
        @Expose
        private Integer companyId;
        @SerializedName("REJECT")
        @Expose
        private Reject reject;
        @SerializedName("ALTER")
        @Expose
        private Alter alter;
        @SerializedName("SPOT")
        @Expose
        private Spot spot;

        public Integer getCompanyId() {
            return companyId;
        }

        public void setCompanyId(Integer companyId) {
            this.companyId = companyId;
        }

        public Reject getReject() {
            return reject;
        }

        public void setReject(Reject reject) {
            this.reject = reject;
        }

        public Alter getAlter() {
            return alter;
        }

        public void setAlter(Alter alter) {
            this.alter = alter;
        }

        public Spot getSpot() {
            return spot;
        }

        public void setSpot(Spot spot) {
            this.spot = spot;
        }

    }

    public class Reject {

        @SerializedName("OPERATION")
        @Expose
        private Integer operation;
        @SerializedName("DEFECT")
        @Expose
        private Integer defect;

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

        public Integer getDefect() {
            return defect;
        }

        public void setDefect(Integer defect) {
            this.defect = defect;
        }

    }
    public class Spot {

        @SerializedName("OPERATION")
        @Expose
        private Integer operation;
        @SerializedName("DEFECT")
        @Expose
        private Integer defect;

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

        public Integer getDefect() {
            return defect;
        }

        public void setDefect(Integer defect) {
            this.defect = defect;
        }

    }

    public class Alter {

        @SerializedName("OPERATION")
        @Expose
        private Integer operation;
        @SerializedName("DEFECT")
        @Expose
        private Integer defect;

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

        public Integer getDefect() {
            return defect;
        }

        public void setDefect(Integer defect) {
            this.defect = defect;
        }

    }
}