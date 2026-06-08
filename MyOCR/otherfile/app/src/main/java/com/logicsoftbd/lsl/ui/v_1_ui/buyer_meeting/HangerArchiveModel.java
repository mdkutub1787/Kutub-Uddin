package com.logicsoftbd.lsl.ui.v_1_ui.buyer_meeting;

import java.io.Serializable;

public class HangerArchiveModel implements Serializable {
    private String garmentItem;
    private String garmentItemBarcode;
    private String dateTime;
    private String archiveId;

    public String getGarmentItem() {
        return garmentItem;
    }

    public void setGarmentItem(String garmentItem) {
        this.garmentItem = garmentItem;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(String archiveId) {
        this.archiveId = archiveId;
    }

    public String getGarmentItemBarcode() {
        return garmentItemBarcode;
    }

    public void setGarmentItemBarcode(String garmentItemBarcode) {
        this.garmentItemBarcode = garmentItemBarcode;
    }
}
