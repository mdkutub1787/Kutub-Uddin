package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SewingRequest {
    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private Result data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Result getData() {
        return data;
    }

    public void setData(Result data) {
        this.data = data;
    }

    public static class Result implements Serializable {

        @Expose
        @SerializedName("MasterPart")
        private MasterPart masterPart;

        @Expose
        @SerializedName("DtlsPart")
        private List<SewingResponse.Result.MasterPart> detailsPart;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public List<SewingResponse.Result.MasterPart> getDetailsPart() {
            return detailsPart;
        }

        public void setDetailsPart(List<SewingResponse.Result.MasterPart> detailsPart) {
            this.detailsPart = detailsPart;
        }
    }

    public  static class MasterPart implements Serializable {

        @SerializedName("LOCATION_ID")
        @Expose
        public Integer locationId;

        @SerializedName("FLOOR_ID")
        @Expose
        public Integer floorId;

        @Expose
        @SerializedName("COMPANY_ID")
        private int companyId;

        @Expose
        @SerializedName("SERVING_COMPANY")
        private int servingCompanyId;

        @SerializedName("PRODUCTION_TYPE")
        @Expose
        public Integer productionType;

        @SerializedName("CUT_NO")
        @Expose
        public String cutNo;

        @SerializedName("SEWING_LINE")
        @Expose
        public Integer sewingLine;

        @SerializedName("INPUT_DATE")
        @Expose
        public String inputDate;

        @SerializedName("ENTRY_DATE")
        @Expose
        public String entryDate;

        @SerializedName("HOUR")
        @Expose
        public String hour;

        @SerializedName("user_id")
        @Expose
        public String userId;

        public Integer getLocationId() {
            return locationId;
        }

        public void setLocationId(Integer locationId) {
            this.locationId = locationId;
        }

        public Integer getFloorId() {
            return floorId;
        }

        public void setFloorId(Integer floorId) {
            this.floorId = floorId;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public int getServingCompanyId() {
            return servingCompanyId;
        }

        public void setServingCompanyId(int servingCompanyId) {
            this.servingCompanyId = servingCompanyId;
        }

        public Integer getProductionType() {
            return productionType;
        }

        public void setProductionType(Integer productionType) {
            this.productionType = productionType;
        }

        public String getCutNo() {
            return cutNo;
        }

        public void setCutNo(String cutNo) {
            this.cutNo = cutNo;
        }

        public Integer getSewingLine() {
            return sewingLine;
        }

        public void setSewingLine(Integer sewingLine) {
            this.sewingLine = sewingLine;
        }

        public String getInputDate() {
            return inputDate;
        }

        public void setInputDate(String inputDate) {
            this.inputDate = inputDate;
        }

        public String getEntryDate() {
            return entryDate;
        }

        public void setEntryDate(String entryDate) {
            this.entryDate = entryDate;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
