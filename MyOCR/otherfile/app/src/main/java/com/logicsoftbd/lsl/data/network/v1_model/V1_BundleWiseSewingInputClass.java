package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_BundleWiseSewingInputClass {
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
    public class Company {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("company")
        @Expose
        private String company;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

    }
    public class Machine {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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

        @SerializedName("company")
        @Expose
        private List<Company> company = null;
        @SerializedName("supplier")
        @Expose
        private List<Supplier> supplier = null;
        @SerializedName("source")
        @Expose
        private List<Source> source = null;
        @SerializedName("shift")
        @Expose
        private List<Shift> shift = null;
        @SerializedName("machine")
        @Expose
        private List<Machine> machine = null;

        public List<Company> getCompany() {
            return company;
        }

        public void setCompany(List<Company> company) {
            this.company = company;
        }

        public List<Supplier> getSupplier() {
            return supplier;
        }

        public void setSupplier(List<Supplier> supplier) {
            this.supplier = supplier;
        }

        public List<Source> getSource() {
            return source;
        }

        public void setSource(List<Source> source) {
            this.source = source;
        }

        public List<Shift> getShift() {
            return shift;
        }

        public void setShift(List<Shift> shift) {
            this.shift = shift;
        }

        public List<Machine> getMachine() {
            return machine;
        }

        public void setMachine(List<Machine> machine) {
            this.machine = machine;
        }

    }
    public class Shift {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class Source {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class Supplier {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
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


