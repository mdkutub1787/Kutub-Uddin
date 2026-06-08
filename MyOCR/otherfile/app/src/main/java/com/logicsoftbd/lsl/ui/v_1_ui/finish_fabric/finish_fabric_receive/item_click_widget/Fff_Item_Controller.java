package com.logicsoftbd.lsl.ui.v_1_ui.finish_fabric.finish_fabric_receive.item_click_widget;

import com.logicsoftbd.lsl.data.network.v1_model.V1_finish_fabric_receive.FFRBarcode;

public class Fff_Item_Controller {
    public static Fff_Item_Controller instance;
    public static Fff_Item_Controller getInstance() {
        if (instance == null) {
            instance= new Fff_Item_Controller();
        }
        return instance;
    }

    Fff_click_interface fff_click_interface;
    FFRBarcode ffrBarcode;

    public Fff_click_interface getFff_click_interface() {
        return fff_click_interface;
    }

    public void setFff_click_interface(Fff_click_interface fff_click_interface) {
        this.fff_click_interface = fff_click_interface;
    }

    public FFRBarcode getFfrBarcode() {
        return ffrBarcode;
    }

    public void setFfrBarcode(FFRBarcode ffrBarcode) {
        this.ffrBarcode = ffrBarcode;
    }
}
