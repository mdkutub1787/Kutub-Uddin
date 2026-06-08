package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DyeingProFunctionalBatchResponse {

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
    public class FunctionalBatchIndex {

        @SerializedName("BATCH_ID")
        @Expose
        private String batchId;
        @SerializedName("SYSTEM_NO")
        @Expose
        private String systemNo;
        @SerializedName("COMPANY_ID")
        @Expose
        private String companyId;
        @SerializedName("PROCESS_START_DATE")
        @Expose
        private String processStartDate;
        @SerializedName("BATCH_NO")
        @Expose
        private String batchNo;
        @SerializedName("PRODUCTION_QTY")
        @Expose
        private String productionQty;
        @SerializedName("BATCH_QTY")
        @Expose
        private String batchQty;

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getSystemNo() {
            return systemNo;
        }

        public void setSystemNo(String systemNo) {
            this.systemNo = systemNo;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getProcessStartDate() {
            return processStartDate;
        }

        public void setProcessStartDate(String processStartDate) {
            this.processStartDate = processStartDate;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public String getProductionQty() {
            return productionQty;
        }

        public void setProductionQty(String productionQty) {
            this.productionQty = productionQty;
        }

        public String getBatchQty() {
            return batchQty;
        }

        public void setBatchQty(String batchQty) {
            this.batchQty = batchQty;
        }

    }
    public class Resultset {

        @SerializedName("functional_batch_index")
        @Expose
        private List<FunctionalBatchIndex> functionalBatchIndex = null;

        public List<FunctionalBatchIndex> getFunctionalBatchIndex() {
            return functionalBatchIndex;
        }

        public void setFunctionalBatchIndex(List<FunctionalBatchIndex> functionalBatchIndex) {
            this.functionalBatchIndex = functionalBatchIndex;
        }

    }
}