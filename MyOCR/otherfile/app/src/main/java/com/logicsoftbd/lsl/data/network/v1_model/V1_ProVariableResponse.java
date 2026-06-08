package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class V1_ProVariableResponse {
    @SerializedName("alter")
    @Expose
    private Alter alter;
    @SerializedName("companyId")
    @Expose
    private Integer companyId;
    @SerializedName("reject")
    @Expose
    private Reject reject;
    @SerializedName("spot")
    @Expose
    private Spot spot;

    public Alter getAlter() {
        return alter;
    }

    public void setAlter(Alter alter) {
        this.alter = alter;
    }

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

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }
    public class Reject {

        @SerializedName("defect")
        @Expose
        private Integer defect;
        @SerializedName("operation")
        @Expose
        private Integer operation;

        public Integer getDefect() {
            return defect;
        }

        public void setDefect(Integer defect) {
            this.defect = defect;
        }

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

    }
    public class Spot {

        @SerializedName("defect")
        @Expose
        private Integer defect;
        @SerializedName("operation")
        @Expose
        private Integer operation;

        public Integer getDefect() {
            return defect;
        }

        public void setDefect(Integer defect) {
            this.defect = defect;
        }

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

    }
    public class Alter {

        @SerializedName("defect")
        @Expose
        private Integer defect;
        @SerializedName("operation")
        @Expose
        private Integer operation;

        public Integer getDefect() {
            return defect;
        }

        public void setDefect(Integer defect) {
            this.defect = defect;
        }

        public Integer getOperation() {
            return operation;
        }

        public void setOperation(Integer operation) {
            this.operation = operation;
        }

    }
}
