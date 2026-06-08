package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class EmbSpRequest {
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
        private List<EmbSpBarcodeResponse.BodyPart.DetailsPart> detailsPart;

        public MasterPart getMasterPart() {
            return masterPart;
        }

        public void setMasterPart(MasterPart masterPart) {
            this.masterPart = masterPart;
        }

        public List<EmbSpBarcodeResponse.BodyPart.DetailsPart> getDetailsPart() {
            return detailsPart;
        }

        public void setDetailsPart(List<EmbSpBarcodeResponse.BodyPart.DetailsPart> detailsPart) {
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

        @SerializedName("PRODUCTION_TYPE")
        @Expose
        public Integer productionType;

        @SerializedName("EMBEL_ID")
        @Expose
        public Integer embelId;

        @SerializedName("EMBEL_NAME")
        @Expose
        public Integer embNameId;

        @SerializedName("EMBEL_TYPE")
        @Expose
        public Integer embTypeId;

        @SerializedName("BODY_PART")
        @Expose
        public Integer bodyPartId;

        @SerializedName("DELIVERY_DATE")
        @Expose
        public String deliveryDate;


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

        public Integer getProductionType() {
            return productionType;
        }

        public void setProductionType(Integer productionType) {
            this.productionType = productionType;
        }

        public Integer getEmbelId() {
            return embelId;
        }

        public void setEmbelId(Integer embelId) {
            this.embelId = embelId;
        }

        public Integer getEmbNameId() {
            return embNameId;
        }

        public void setEmbNameId(Integer embNameId) {
            this.embNameId = embNameId;
        }

        public Integer getEmbTypeId() {
            return embTypeId;
        }

        public void setEmbTypeId(Integer embTypeId) {
            this.embTypeId = embTypeId;
        }

        public Integer getBodyPartId() {
            return bodyPartId;
        }

        public void setBodyPartId(Integer bodyPartId) {
            this.bodyPartId = bodyPartId;
        }

        public String getDeliveryDate() {
            return deliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            this.deliveryDate = deliveryDate;
        }
    }
}
