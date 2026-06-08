package com.logicsoftbd.lsl.ui.v_1_ui.with_observation;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BatchModel;

import java.util.ArrayList;

public class V1_BatchAdapter extends ArrayAdapter<V1_BatchModel> {
    ArrayList<V1_BatchModel> batchModels;
    LayoutInflater vi;
    int Resource;

    public V1_BatchAdapter(@NonNull Context context, int resource, ArrayList<V1_BatchModel> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        batchModels = objects;
        Resource = resource;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return batchModels.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        holder = new V1_BatchAdapter.ViewHolder();

        if(convertView == null){
            convertView = vi.inflate(R.layout.batch_details_layout, null);

            holder.receive_no = convertView.findViewById(R.id.receive_number);
            holder.description = convertView.findViewById(R.id.fabric_description);
            holder.recn_id = convertView.findViewById(R.id.recv_id);
            holder.batch_no = convertView.findViewById(R.id.batch_no);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.receive_no.setText(batchModels.get(position).getRecive_number());
        holder.description.setText(batchModels.get(position).getFabric_description());
        holder.recn_id.setText(batchModels.get(position).getRecive_id());
        holder.batch_no.setText(batchModels.get(position).getBatch_no());

        return convertView;
    }

    public class ViewHolder {
        //sewing Input Field
        private TextView receive_no;
        private TextView description;
        private TextView recn_id;
        private TextView batch_no;
    }
}
