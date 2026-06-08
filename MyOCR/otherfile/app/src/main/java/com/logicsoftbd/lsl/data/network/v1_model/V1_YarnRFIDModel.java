package com.logicsoftbd.lsl.data.network.v1_model;

import java.io.Serializable;

public class V1_YarnRFIDModel implements Serializable {
    private String rfidItem;
    private String dateTime;

    public String getRfidItem() {
        return rfidItem;
    }

    public void setRfidItem(String rfidItem) {
        this.rfidItem = rfidItem;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
