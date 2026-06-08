package com.logicsoftbd.lsl.ui.v_1_ui.report;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_CapacityVsAllocatedOrderModelClass;

import java.util.ArrayList;

public class V1_CapacityVsAllocatedAdapter extends ArrayAdapter<V1_CapacityVsAllocatedOrderModelClass> {
    Context context;
    ArrayList<V1_CapacityVsAllocatedOrderModelClass> capacityVsAllocatedModelClasses;
    LayoutInflater vi;
    private int Resource;

    public V1_CapacityVsAllocatedAdapter(@NonNull Context context, int resource, ArrayList<V1_CapacityVsAllocatedOrderModelClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        capacityVsAllocatedModelClasses = objects;
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
        V1_CapacityVsAllocatedAdapter.ViewHolder viewHolder;

        viewHolder = new V1_CapacityVsAllocatedAdapter.ViewHolder();

        if(convertView == null){
            convertView = vi.inflate(R.layout.plan_vs_booked_report_layout, null);

            viewHolder.serialNumber = convertView.findViewById(R.id.slText);
            viewHolder.companyName = convertView.findViewById(R.id.companyText);
            viewHolder.locationName = convertView.findViewById(R.id.locationText);
            viewHolder.bookedText = convertView.findViewById(R.id.bookedText);
            viewHolder.allocatedText = convertView.findViewById(R.id.planText);
            viewHolder.allocatedFirst = convertView.findViewById(R.id.planfirstText);
            viewHolder.allocatedTotal = convertView.findViewById(R.id.planTotalText);
            viewHolder.capacityFirst = convertView.findViewById(R.id.bookedfirstText);
            viewHolder.capacityTotal = convertView.findViewById(R.id.bookedTotalText);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (V1_CapacityVsAllocatedAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.serialNumber.setText(String.valueOf(position+1));
        viewHolder.companyName.setText(capacityVsAllocatedModelClasses.get(position).getCompanyName());
        viewHolder.locationName.setText(capacityVsAllocatedModelClasses.get(position).getLocationName());
        viewHolder.bookedText.setText("Capacity");
        viewHolder.allocatedText.setText("Allocated");
        viewHolder.allocatedFirst.setText(capacityVsAllocatedModelClasses.get(position).getAllocated());
        viewHolder.allocatedTotal.setText(capacityVsAllocatedModelClasses.get(position).getAllocated());
        viewHolder.capacityFirst.setText(capacityVsAllocatedModelClasses.get(position).getCapacity());
        viewHolder.capacityTotal.setText(capacityVsAllocatedModelClasses.get(position).getCapacity());

        return convertView;
    }

    public class ViewHolder {
        private TextView serialNumber, companyName, locationName, bookedText, allocatedText, allocatedFirst, capacityFirst, allocatedTotal, capacityTotal;
    }
}
