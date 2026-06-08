package com.logicsoftbd.lsl.data.network.v1_model;

import java.io.Serializable;

public class V1_IssueReturnRFIDModel implements Serializable {
    private String rfid;
    private String weight;

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
