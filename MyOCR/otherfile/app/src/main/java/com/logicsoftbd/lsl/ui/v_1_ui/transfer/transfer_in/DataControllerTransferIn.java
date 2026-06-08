package com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_in;

import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.DtlsPart;

public class DataControllerTransferIn {

    public static DataControllerTransferIn instance;
    public static DataControllerTransferIn getInstance() {
        if (instance == null) {
            instance = new DataControllerTransferIn();
        }
        return instance;
    }

    DeleteItemFromTransferInInterface clickInterface;
    DtlsPart modelData;

    public DeleteItemFromTransferInInterface getClickInterface() {
        return clickInterface;
    }

    public void setClickInterface(DeleteItemFromTransferInInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public DtlsPart getModelData() {
        return modelData;
    }

    public void setModelData(DtlsPart modelData) {
        this.modelData = modelData;
    }
}
