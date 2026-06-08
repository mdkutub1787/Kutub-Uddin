package com.logicsoftbd.lsl.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FabricGradeModel {
    private boolean isFirst = false;

    @Expose
    @SerializedName("status")
    private String status;


    @Expose
    @SerializedName("resultset")
    private One data;

    public  class One implements Serializable {

        @Expose
        @SerializedName("1")
        private ArrayList<Result> one;


    }
    public  class Result implements Serializable {

        @Expose
        @SerializedName("limit_from")
        private String limit_from;

        @Expose
        @SerializedName("limit_to")
        private String limit_to;

        @Expose
        @SerializedName("grade")
        private String grade;

    }
}
