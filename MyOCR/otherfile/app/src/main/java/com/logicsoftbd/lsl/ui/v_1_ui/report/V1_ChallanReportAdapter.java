package com.logicsoftbd.lsl.ui.v_1_ui.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ChallanModelClass;

import java.util.ArrayList;

public class V1_ChallanReportAdapter extends ArrayAdapter<V1_ChallanModelClass> {
    Context context;
    ArrayList<V1_ChallanModelClass> challanModelClasses;
    LayoutInflater vi;
    private int Resource;

    public V1_ChallanReportAdapter(@NonNull Context context, int resource, ArrayList<V1_ChallanModelClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        challanModelClasses = objects;
        Resource = resource;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 500;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        viewHolder = new V1_ChallanReportAdapter.ViewHolder();

        if(convertView == null){
            convertView = vi.inflate(R.layout.challan_report_layout, null);

            viewHolder.bundleNo = convertView.findViewById(R.id.challanBunldeNo);
            viewHolder.buyer = convertView.findViewById(R.id.challanBuyer);
            viewHolder.barcode = convertView.findViewById(R.id.challanbarcode);
            viewHolder.country = convertView.findViewById(R.id.challanCountry);
            viewHolder.deletecheck = convertView.findViewById(R.id.deletecheck);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.buyer.setText("Buyer: "+challanModelClasses.get(position).getBuyer());
        viewHolder.bundleNo.setText("BUndle NO: "+challanModelClasses.get(position).getBundleNo());
        viewHolder.barcode.setText("Barcode: "+challanModelClasses.get(position).getBarcodeNo());
        viewHolder.country.setText("Country: "+challanModelClasses.get(position).getCountry());

        viewHolder.deletecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    V1_ChallanReportActivity.modelArrayList.get(position).setDeleteStatus(1);
                }
            }
        });


        return convertView;
    }

    public class ViewHolder {
        private TextView bundleNo, buyer, country, barcode;
        private CheckBox deletecheck;

    }
}
