package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_BatchModel {
    private String recive_number;
    private String fabric_description;
    private String recive_id;
    private String pro_dtls_id;
    private String batch_no;
    private String batch_id;
    private String prod_id;
    private String buyer_id;

    public V1_BatchModel() {
    }

    public V1_BatchModel(String recive_number, String fabric_description, String recive_id, String pro_dtls_id, String batch_no, String batch_id, String prod_id, String buyer_id) {
        this.recive_number = recive_number;
        this.fabric_description = fabric_description;
        this.recive_id = recive_id;
        this.pro_dtls_id = pro_dtls_id;
        this.batch_no = batch_no;
        this.batch_id = batch_id;
        this.prod_id = prod_id;
        this.buyer_id = buyer_id;
    }

    public String getRecive_number() {
        return recive_number;
    }

    public void setRecive_number(String recive_number) {
        this.recive_number = recive_number;
    }

    public String getFabric_description() {
        return fabric_description;
    }

    public void setFabric_description(String fabric_description) {
        this.fabric_description = fabric_description;
    }

    public String getRecive_id() {
        return recive_id;
    }

    public void setRecive_id(String recive_id) {
        this.recive_id = recive_id;
    }

    public String getPro_dtls_id() {
        return pro_dtls_id;
    }

    public void setPro_dtls_id(String pro_dtls_id) {
        this.pro_dtls_id = pro_dtls_id;
    }

    public String getBatch_no() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no = batch_no;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_id(String prod_id) {
        this.prod_id = prod_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }
}
