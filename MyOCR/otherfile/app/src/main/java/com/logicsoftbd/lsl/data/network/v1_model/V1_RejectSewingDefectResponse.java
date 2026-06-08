package com.logicsoftbd.lsl.data.network.v1_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V1_RejectSewingDefectResponse {

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

        @SerializedName("defect_type")
        @Expose
        private List<DefectType> defectType = null;

        public List<DefectType> getDefectType() {
            return defectType;
        }

        public void setDefectType(List<DefectType> defectType) {
            this.defectType = defectType;
        }

    }
    public class DefectType {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("defect_name")
        @Expose
        private String defectName;
        @SerializedName("defectCount")
        @Expose
        private String defectCount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getDefectName() {
            return defectName;
        }

        public void setDefectName(String defectName) {
            this.defectName = defectName;
        }

        public String getDefectCount(){return  defectCount;}

        public void setDefectCount(String defectCount){this.defectCount = defectCount;}

    }
}