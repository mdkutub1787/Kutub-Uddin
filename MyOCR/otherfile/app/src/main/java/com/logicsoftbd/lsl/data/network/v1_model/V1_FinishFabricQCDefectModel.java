package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_FinishFabricQCDefectModel {
    private String defectId;
    private String defectName;
    private Integer defectCount;
    private Integer defectFound;
    private Integer defectPenalty;
    private String defectTypeName;


    public V1_FinishFabricQCDefectModel(String defectId, String defectName, Integer defectCount, Integer defectFound, Integer defectPenalty, String defectTypeName) {
        this.defectId = defectId;
        this.defectName = defectName;
        this.defectCount = defectCount;
        this.defectFound = defectFound;
        this.defectPenalty = defectPenalty;
        this.defectTypeName = defectTypeName;
    }

//    public V1_FinishFabricQCDefectModel(String defectId, String defectName, Integer defectCount, Integer defectFound, Integer defectPenalty) {
//        this.defectId = defectId;
//        this.defectName = defectName;
//        this.defectCount = defectCount;
//        this.defectFound = defectFound;
//        this.defectPenalty = defectPenalty;
//    }

    public String getDefectId() {
        return defectId;
    }

    public void setDefectId(String defectId) {
        this.defectId = defectId;
    }

    public String getDefectName() {
        return defectName;
    }

    public void setDefectName(String defectName) {
        this.defectName = defectName;
    }

    public Integer getDefectCount() {
        return defectCount;
    }

    public void setDefectCount(Integer defectCount) {
        this.defectCount = defectCount;
    }

    public Integer getDefectFound() {
        return defectFound;
    }

    public void setDefectFound(Integer defectFound) {
        this.defectFound = defectFound;
    }

    public Integer getDefectPenalty() {
        return defectPenalty;
    }

    public void setDefectPenalty(Integer defectPenalty) {
        this.defectPenalty = defectPenalty;
    }

    public String getDefectTypeName() {
        return defectTypeName;
    }

    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }
}
