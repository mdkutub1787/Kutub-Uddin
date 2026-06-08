package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.SerializedName;

public class SpinnerModel {

    @SerializedName("Name")
    private String Name;
    @SerializedName("Id")
    private int Id;

    public SpinnerModel(String name, int id) {
        Name = name;
        Id = id;
    }

    @Override
    public String toString() {
        return Name;
    }
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
