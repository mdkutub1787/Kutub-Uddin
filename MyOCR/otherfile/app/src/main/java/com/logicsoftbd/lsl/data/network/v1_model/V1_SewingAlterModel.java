package com.logicsoftbd.lsl.data.network.v1_model;

import java.io.Serializable;

public class V1_SewingAlterModel implements Serializable {
    private Integer id;
    private String defectName;
    private String defectCount;
    private Boolean defectSelect;
    private Integer defectType;

    private String defectSerialNo;

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

    public Boolean getDefectSelect() {
        return defectSelect;
    }

    public void setDefectSelect(Boolean defectSelect) {
        this.defectSelect = defectSelect;
    }

    public Integer getDefectType() {
        return defectType;
    }

    public void setDefectType(Integer defectType) {
        this.defectType = defectType;
    }

    public String getDefectSerialNo() {
        return defectSerialNo;
    }

    public void setDefectSerialNo(String defectSerialNo) {
        this.defectSerialNo = defectSerialNo;
    }

}
