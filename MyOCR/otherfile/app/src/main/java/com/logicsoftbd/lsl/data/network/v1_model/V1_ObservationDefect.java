package com.logicsoftbd.lsl.data.network.v1_model;

public class V1_ObservationDefect {
    private String key;
    private String value;

    public V1_ObservationDefect() {
    }

    public V1_ObservationDefect(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
