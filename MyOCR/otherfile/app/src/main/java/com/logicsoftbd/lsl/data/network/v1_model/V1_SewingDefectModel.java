package com.logicsoftbd.lsl.data.network.v1_model;

import java.io.Serializable;

public class V1_SewingDefectModel implements Serializable {
    private Integer id;
    private String defectName;
    private String defectCount;
    private Boolean defectSelect;
    private Integer defectType;

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
}
