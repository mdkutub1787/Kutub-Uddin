package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_DyeingProductionPDAResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result_set")
    @Expose
    private ResultSet resultSet;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ResultSet {
        @SerializedName("mode")
        @Expose
        private String mode;
        @SerializedName("batch_id")
        @Expose
        private Integer batchId;
        @SerializedName("batch_no")
        @Expose
        private String batchNo;
        @SerializedName("extension_no")
        @Expose
        private Integer extensionNo;
        @SerializedName("total_roll")
        @Expose
        private Integer totalRoll;
        @SerializedName("batch_weight")
        @Expose
        private Integer batchWeight;
        @SerializedName("start_date")
        @Expose
        private String startDate;
        @SerializedName("start_hour")
        @Expose
        private Integer startHour;
        @SerializedName("start_minute")
        @Expose
        private Integer startMinute;
        @SerializedName("machine_no")
        @Expose
        private Integer machineNo;
        @SerializedName("multi_batch")
        @Expose
        private List<MultiBatch> multiBatch;
        @SerializedName("dyeing_type")
        @Expose
        private List<DyeingType> dyeingType;
        @SerializedName("btb_ltb")
        @Expose
        private List<BtbLtb> btbLtb;
        @SerializedName("process")
        @Expose
        private List<Process> process;
        @SerializedName("result_arr")
        @Expose
        private List<ResultArr> resultArr;

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public Integer getBatchId() {
            return batchId;
        }

        public void setBatchId(Integer batchId) {
            this.batchId = batchId;
        }

        public String getBatchNo() {
            return batchNo;
        }

        public void setBatchNo(String batchNo) {
            this.batchNo = batchNo;
        }

        public Integer getExtensionNo() {
            return extensionNo;
        }

        public void setExtensionNo(Integer extensionNo) {
            this.extensionNo = extensionNo;
        }

        public Integer getTotalRoll() {
            return totalRoll;
        }

        public void setTotalRoll(Integer totalRoll) {
            this.totalRoll = totalRoll;
        }

        public Integer getBatchWeight() {
            return batchWeight;
        }

        public void setBatchWeight(Integer batchWeight) {
            this.batchWeight = batchWeight;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public Integer getStartHour() {
            return startHour;
        }

        public void setStartHour(Integer startHour) {
            this.startHour = startHour;
        }

        public Integer getStartMinute() {
            return startMinute;
        }

        public void setStartMinute(Integer startMinute) {
            this.startMinute = startMinute;
        }

        public Integer getMachineNo() {
            return machineNo;
        }

        public void setMachineNo(Integer machineNo) {
            this.machineNo = machineNo;
        }

        public List<MultiBatch> getMultiBatch() {
            return multiBatch;
        }

        public void setMultiBatch(List<MultiBatch> multiBatch) {
            this.multiBatch = multiBatch;
        }

        public List<DyeingType> getDyeingType() {
            return dyeingType;
        }

        public void setDyeingType(List<DyeingType> dyeingType) {
            this.dyeingType = dyeingType;
        }

        public List<BtbLtb> getBtbLtb() {
            return btbLtb;
        }

        public void setBtbLtb(List<BtbLtb> btbLtb) {
            this.btbLtb = btbLtb;
        }

        public List<Process> getProcess() {
            return process;
        }

        public void setProcess(List<Process> process) {
            this.process = process;
        }

        public List<ResultArr> getResultArr() {
            return resultArr;
        }

        public void setResultArr(List<ResultArr> resultArr) {
            this.resultArr = resultArr;
        }
    }

    public static class DyeingType {
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

    public static class BtbLtb {
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

    public static class Process {
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

    public static class MultiBatch {
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

    public static class ResultArr {
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