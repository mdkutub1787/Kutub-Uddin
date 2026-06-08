package com.logicsoftbd.lsl.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Process implements Serializable {
    private int drawableId;
    private String title;
    private String subTitle;
    private boolean isActivityResult;
    private DataParam dataParam;




    public Process(int drawableId, String title, DataParam dataParam) {
        this.drawableId = drawableId;
        this.title = title;
        this.dataParam = dataParam;
    }

    public Process(int drawableId, String title, String subTitle, DataParam dataParam) {
        this.drawableId = drawableId;
        this.title = title;
        this.subTitle = subTitle;
        this.dataParam = dataParam;
        this.isActivityResult = false;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public boolean isActivityResult() {
        return isActivityResult;
    }

    public void setActivityResult(boolean activityResult) {
        isActivityResult = activityResult;
    }

    public DataParam getDataParam() {
        return dataParam;
    }

    public void setDataParam(DataParam dataParam) {
        this.dataParam = dataParam;
    }

    public static class DataParam implements  Serializable{
        @Expose
        @SerializedName("barcode")
        private String barcode;

        @Expose
        @SerializedName("page_param")
        private String pageParam;

        @Expose
        @SerializedName("type_param")
        private String typeParam;

        private int type;
        private int productionProcess;

        public DataParam(String pageParam, String typeParam) {
            this.pageParam = pageParam;
            this.typeParam = typeParam;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getPageParam() {
            return pageParam;
        }

        public void setPageParam(String pageParam) {
            this.pageParam = pageParam;
        }

        public String getTypeParam() {
            return typeParam;
        }

        public void setTypeParam(String typeParam) {
            this.typeParam = typeParam;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getProductionProcess() {
            return productionProcess;
        }

        public void setProductionProcess(int productionProcess) {
            this.productionProcess = productionProcess;
        }
    }
}
