package com.logicsoftbd.lsl.data.network.model;

import java.io.Serializable;
import java.util.List;

public class BarcodeListResponse implements Serializable {
    private boolean isFirst = true;

    private List<BarcodeResponse> responseList;

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public List<BarcodeResponse> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<BarcodeResponse> responseList) {
        this.responseList = responseList;
    }
}
