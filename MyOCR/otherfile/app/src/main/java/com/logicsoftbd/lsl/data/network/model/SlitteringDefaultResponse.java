package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SlitteringDefaultResponse {

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
    public class Source {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class ShiftName {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class ServiceCompany {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("company")
        @Expose
        private String company;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

    }
    public class Resultset {

        @SerializedName("entry_form_no")
        @Expose
        private List<String> entryFormNo = null;
        @SerializedName("production_type")
        @Expose
        private List<ProductionType> productionType = null;
        @SerializedName("company")
        @Expose
        private List<Company> company = null;
        @SerializedName("source")
        @Expose
        private List<Source> source = null;
        @SerializedName("service_company")
        @Expose
        private List<ServiceCompany> serviceCompany = null;
        @SerializedName("process_name")
        @Expose
        private List<ProcessName> processName = null;
        @SerializedName("next_process")
        @Expose
        private List<NextProcess> nextProcess = null;
        @SerializedName("result")
        @Expose
        private List<Result> result = null;
        @SerializedName("shift_name")
        @Expose
        private List<ShiftName> shiftName = null;

        public List<String> getEntryFormNo() {
            return entryFormNo;
        }

        public void setEntryFormNo(List<String> entryFormNo) {
            this.entryFormNo = entryFormNo;
        }

        public List<ProductionType> getProductionType() {
            return productionType;
        }

        public void setProductionType(List<ProductionType> productionType) {
            this.productionType = productionType;
        }

        public List<Company> getCompany() {
            return company;
        }

        public void setCompany(List<Company> company) {
            this.company = company;
        }

        public List<Source> getSource() {
            return source;
        }

        public void setSource(List<Source> source) {
            this.source = source;
        }

        public List<ServiceCompany> getServiceCompany() {
            return serviceCompany;
        }

        public void setServiceCompany(List<ServiceCompany> serviceCompany) {
            this.serviceCompany = serviceCompany;
        }

        public List<ProcessName> getProcessName() {
            return processName;
        }

        public void setProcessName(List<ProcessName> processName) {
            this.processName = processName;
        }

        public List<NextProcess> getNextProcess() {
            return nextProcess;
        }

        public void setNextProcess(List<NextProcess> nextProcess) {
            this.nextProcess = nextProcess;
        }

        public List<Result> getResult() {
            return result;
        }

        public void setResult(List<Result> result) {
            this.result = result;
        }

        public List<ShiftName> getShiftName() {
            return shiftName;
        }

        public void setShiftName(List<ShiftName> shiftName) {
            this.shiftName = shiftName;
        }

    }
    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class ProductionType {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class ProcessName {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class NextProcess {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class Company {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("company")
        @Expose
        private String company;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

    }
}