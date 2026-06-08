package com.logicsoftbd.lsl.ui.v_1_ui.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CapacityVsPlanModelClass;

import java.util.ArrayList;

public class V1_CapacityVsBookedAdapter extends ArrayAdapter<V1_CapacityVsPlanModelClass> {
    Context context;
    ArrayList<V1_CapacityVsPlanModelClass> capacityVsBookedModelClasses;
    LayoutInflater vi;
    private int Resource;

    public V1_CapacityVsBookedAdapter(@NonNull Context context, int resource, ArrayList<V1_CapacityVsPlanModelClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        capacityVsBookedModelClasses = objects;
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
        V1_CapacityVsBookedAdapter.ViewHolder viewHolder;

        viewHolder = new V1_CapacityVsBookedAdapter.ViewHolder();

        if(convertView == null){
            convertView = vi.inflate(R.layout.plan_vs_booked_report_layout, null);

            viewHolder.serialNumber = convertView.findViewById(R.id.slText);
            viewHolder.companyName = convertView.findViewById(R.id.companyText);
            viewHolder.locationName = convertView.findViewById(R.id.locationText);
            viewHolder.bookedText = convertView.findViewById(R.id.bookedText);
            viewHolder.planText = convertView.findViewById(R.id.planText);
            viewHolder.planFirst = convertView.findViewById(R.id.planfirstText);
            viewHolder.planTotal = convertView.findViewById(R.id.planTotalText);
            viewHolder.capacityFirst = convertView.findViewById(R.id.bookedfirstText);
            viewHolder.capacityTotal = convertView.findViewById(R.id.bookedTotalText);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (V1_CapacityVsBookedAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.serialNumber.setText(String.valueOf(position+1));
        viewHolder.companyName.setText(capacityVsBookedModelClasses.get(position).getCompanyName());
        viewHolder.locationName.setText(capacityVsBookedModelClasses.get(position).getLocationName());
        viewHolder.bookedText.setText("Capacity");
        viewHolder.planText.setText("Plan");
        viewHolder.planFirst.setText(capacityVsBookedModelClasses.get(position).getPlan());
        viewHolder.planTotal.setText(capacityVsBookedModelClasses.get(position).getPlan());
        viewHolder.capacityFirst.setText(capacityVsBookedModelClasses.get(position).getCapacity());
        viewHolder.capacityTotal.setText(capacityVsBookedModelClasses.get(position).getCapacity());

        return convertView;
    }

    public class ViewHolder {
        private TextView serialNumber, companyName, locationName, bookedText, planText, planFirst, capacityFirst, planTotal, capacityTotal;
    }
}
