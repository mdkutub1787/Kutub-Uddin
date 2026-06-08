package com.logicsoftbd.lsl.ui.v_1_ui.transfer.transfer_out;

import com.logicsoftbd.lsl.data.network.v1_model.V1_transfer_out.barcode.DtlsPart;

public class DataControllerTransferOut {
    public static DataControllerTransferOut instance;

    public static DataControllerTransferOut getInstance() {
        if (instance == null) {
            instance = new DataControllerTransferOut();
        }
        return instance;
    }

    DeleteItemFromTransferOutInterface clickInterface;
    DtlsPart modelData;

    public DeleteItemFromTransferOutInterface getClickInterface() {
        return clickInterface;
    }

    public void setClickInterface(DeleteItemFromTransferOutInterface clickInterface) {
        this.clickInterface = clickInterface;
    }

    public DtlsPart getModelData() {
        return modelData;
    }

    public void setModelData(DtlsPart modelData) {
        this.modelData = modelData;
    }
}
