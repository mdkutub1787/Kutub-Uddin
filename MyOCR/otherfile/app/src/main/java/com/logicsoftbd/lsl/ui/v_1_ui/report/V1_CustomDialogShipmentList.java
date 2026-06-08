package com.logicsoftbd.lsl.ui.v_1_ui.report;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_Shipment_Schedule_Model;

public class V1_CustomDialogShipmentList extends Dialog implements View.OnClickListener {

    V1_Shipment_Schedule_Adapter adapter;
    Context context;


    public V1_CustomDialogShipmentList(@NonNull Context context, V1_Shipment_Schedule_Adapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;

    }


    public Activity activity;
    public Button yes, no;
    TextView title;
    GridView gridView;
TextView totalQ, totalQ_val,fullshipeedTotal,partialShippedTotal,totalRunnig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shipment_dialog_layout);
      getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        yes =  findViewById(R.id.yes);
        title = findViewById(R.id.title);
        gridView = findViewById(R.id.shipmentGrid);

        totalQ = (TextView) findViewById(R.id.totalQunt);
        totalQ_val = (TextView) findViewById(R.id.quantyVal);
        fullshipeedTotal = (TextView) findViewById(R.id.fullShipped);
        partialShippedTotal = (TextView) findViewById(R.id.partialShipped);
        totalRunnig = (TextView) findViewById(R.id.runningTotal);

        totalQ.setText(V1_Shipment_Schedule_Model.getTotalQuantity());
        totalQ_val.setText(V1_Shipment_Schedule_Model.getTotalQuantityValue());
        fullshipeedTotal.setText(V1_Shipment_Schedule_Model.getTotalFullshipped());
        partialShippedTotal.setText(V1_Shipment_Schedule_Model.getTotalPartialShipped());
        totalRunnig.setText(V1_Shipment_Schedule_Model.getTotalRunning());





        gridView.setAdapter(adapter);
        yes.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
