package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_ObservationModelClass {
    private int Id;
    private int DefectId;
    private String observationDefectName;
    private int observationInchSpinner;
    private int observationDepartmentSpinner;

    public V1_ObservationModelClass() {
    }

    public V1_ObservationModelClass(int defectId, String observationDefectName, int observationInchSpinner, int observationDepartmentSpinner) {
        DefectId = defectId;
        this.observationDefectName = observationDefectName;
        this.observationInchSpinner = observationInchSpinner;
        this.observationDepartmentSpinner = observationDepartmentSpinner;
    }

    public V1_ObservationModelClass(int id, int defectId, String observationDefectName, int observationInchSpinner, int observationDepartmentSpinner) {
        Id = id;
        DefectId = defectId;
        this.observationDefectName = observationDefectName;
        this.observationInchSpinner = observationInchSpinner;
        this.observationDepartmentSpinner = observationDepartmentSpinner;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getDefectId() {
        return DefectId;
    }

    public void setDefectId(int defectId) {
        DefectId = defectId;
    }

    public String getObservationDefectName() {
        return observationDefectName;
    }

    public void setObservationDefectName(String observationDefectName) {
        this.observationDefectName = observationDefectName;
    }

    public int getObservationInchSpinner() {
        return observationInchSpinner;
    }

    public void setObservationInchSpinner(int observationInchSpinner) {
        this.observationInchSpinner = observationInchSpinner;
    }

    public int getObservationDepartmentSpinner() {
        return observationDepartmentSpinner;
    }

    public void setObservationDepartmentSpinner(int observationDepartmentSpinner) {
        this.observationDepartmentSpinner = observationDepartmentSpinner;
    }
}
