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
import com.logicsoftbd.lsl.data.network.v1_model.V1_PlanVsBookedModelClass;

import java.util.ArrayList;

public class V1_PlanVsBookedAdapter extends ArrayAdapter<V1_PlanVsBookedModelClass> {
    Context context;
    ArrayList<V1_PlanVsBookedModelClass> planVsBookedModelClasses;
    LayoutInflater vi;
    private int Resource;

    public V1_PlanVsBookedAdapter(@NonNull Context context, int resource, ArrayList<V1_PlanVsBookedModelClass> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        planVsBookedModelClasses = objects;
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
        V1_PlanVsBookedAdapter.ViewHolder viewHolder;

        viewHolder = new V1_PlanVsBookedAdapter.ViewHolder();

        if(convertView == null){
            convertView = vi.inflate(R.layout.plan_vs_booked_report_layout, null);

            viewHolder.serialNumber = convertView.findViewById(R.id.slText);
            viewHolder.companyName = convertView.findViewById(R.id.companyText);
            viewHolder.locationName = convertView.findViewById(R.id.locationText);
            viewHolder.planFirst = convertView.findViewById(R.id.planfirstText);
            viewHolder.planTotal = convertView.findViewById(R.id.planTotalText);
            viewHolder.bookedFirst = convertView.findViewById(R.id.bookedfirstText);
            viewHolder.bookedTotal = convertView.findViewById(R.id.bookedTotalText);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (V1_PlanVsBookedAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.serialNumber.setText(String.valueOf(position+1));
        viewHolder.companyName.setText(planVsBookedModelClasses.get(position).getCompanyName());
        viewHolder.locationName.setText(planVsBookedModelClasses.get(position).getLocationName());
        viewHolder.planFirst.setText(planVsBookedModelClasses.get(position).getPlan());
        viewHolder.planTotal.setText(planVsBookedModelClasses.get(position).getPlan());
        viewHolder.bookedFirst.setText(planVsBookedModelClasses.get(position).getBooked());
        viewHolder.bookedTotal.setText(planVsBookedModelClasses.get(position).getBooked());

        return convertView;
    }

    public class ViewHolder {
        private TextView serialNumber, companyName, locationName, planFirst, bookedFirst, planTotal, bookedTotal;
    }
}
