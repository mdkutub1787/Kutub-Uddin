package com.logicsoftbd.lsl.data.network.v1_model;

import java.util.ArrayList;
import java.util.Collections;

public class V1_OperationSorterListModel {
    ArrayList<V1_ConfigSewingOperationModel> configSewingOperationModels = new ArrayList<>();

    public V1_OperationSorterListModel(ArrayList<V1_ConfigSewingOperationModel> configSewingOperationModels) {
        this.configSewingOperationModels = configSewingOperationModels;
    }

    public ArrayList<V1_ConfigSewingOperationModel> getConfigSewingOperationModels() {
        Collections.sort(configSewingOperationModels);
        return configSewingOperationModels;
    }
}
