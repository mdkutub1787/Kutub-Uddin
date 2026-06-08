package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.logicsoftbd.lsl.R;

public class V1_CustomDialogPendingList extends Dialog implements View.OnClickListener {

    V1_SewingInputPendingAdapter adapter;
    Context context;
    public V1_CustomDialogPendingList(@NonNull Context context, V1_SewingInputPendingAdapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
    }

    public Activity activity;
    public Button yes, no;
    TextView title;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        yes =  findViewById(R.id.yes);
        title = findViewById(R.id.title);
        gridView = findViewById(R.id.pendingGrid);

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
