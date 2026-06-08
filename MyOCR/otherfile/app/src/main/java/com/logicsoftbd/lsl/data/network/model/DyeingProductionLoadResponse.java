package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DyeingProductionLoadResponse {

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
    public class LoadingUnloading {

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
    public class LtbBtb {

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
    public class MultiBatch {

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
    public class Process {

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
    public class Responsibility {

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
    public class Resultset {

        @SerializedName("loading_unloading")
        @Expose
        private List<LoadingUnloading> loadingUnloading = null;
        @SerializedName("dyeing_type")
        @Expose
        private List<DyeingType> dyeingType = null;
        @SerializedName("company")
        @Expose
        private List<Company> company = null;
        @SerializedName("source")
        @Expose
        private List<Source> source = null;
        @SerializedName("service_company")
        @Expose
        private List<ServiceCompany> serviceCompany = null;
        @SerializedName("process")
        @Expose
        private List<Process> process = null;
        @SerializedName("ltb_btb")
        @Expose
        private List<LtbBtb> ltbBtb = null;
        @SerializedName("multi_batch")
        @Expose
        private List<MultiBatch> multiBatch = null;
        @SerializedName("result")
        @Expose
        private List<Result> result = null;
        @SerializedName("shift_name")
        @Expose
        private List<ShiftName> shiftName = null;
        @SerializedName("fabric_type")
        @Expose
        private List<FabricType> fabricType = null;
        @SerializedName("responsibility")
        @Expose
        private List<Responsibility> responsibility = null;

        public List<LoadingUnloading> getLoadingUnloading() {
            return loadingUnloading;
        }

        public void setLoadingUnloading(List<LoadingUnloading> loadingUnloading) {
            this.loadingUnloading = loadingUnloading;
        }

        public List<DyeingType> getDyeingType() {
            return dyeingType;
        }

        public void setDyeingType(List<DyeingType> dyeingType) {
            this.dyeingType = dyeingType;
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

        public List<Process> getProcess() {
            return process;
        }

        public void setProcess(List<Process> process) {
            this.process = process;
        }

        public List<LtbBtb> getLtbBtb() {
            return ltbBtb;
        }

        public void setLtbBtb(List<LtbBtb> ltbBtb) {
            this.ltbBtb = ltbBtb;
        }

        public List<MultiBatch> getMultiBatch() {
            return multiBatch;
        }

        public void setMultiBatch(List<MultiBatch> multiBatch) {
            this.multiBatch = multiBatch;
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


        public List<FabricType> getFabricType() {
            return fabricType;
        }

        public void setFabricType(List<FabricType> fabricType) {
            this.fabricType = fabricType;
        }

        public List<Responsibility> getResponsibility() {
            return responsibility;
        }

        public void setResponsibility(List<Responsibility> responsibility) {
            this.responsibility = responsibility;
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
    public class DyeingType {

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
    public class FabricType {

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
}
