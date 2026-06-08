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
import com.logicsoftbd.lsl.data.network.v1_model.V1_Shipment_Schedule_Model;

import java.util.ArrayList;

public class V1_Shipment_Schedule_Adapter extends ArrayAdapter<V1_Shipment_Schedule_Model> {

    ArrayList<V1_Shipment_Schedule_Model> shipmentModels;
    LayoutInflater vi;
    int Resource;

    public V1_Shipment_Schedule_Adapter(@NonNull Context context, int resource, ArrayList<V1_Shipment_Schedule_Model> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        shipmentModels = objects;
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ShipmentViewHolder holder;

        holder = new V1_Shipment_Schedule_Adapter.ShipmentViewHolder();

        if(convertView == null)
        {
            convertView = vi.inflate(R.layout.shipment_report_items, null);

            holder.seral = convertView.findViewById(R.id.serial_no);
            holder.com_name = convertView.findViewById(R.id.com_name);
            holder.buyer_name = convertView.findViewById(R.id.buyer_name);
            holder.quantity = convertView.findViewById(R.id.quantity);
            holder.quantity_value = convertView.findViewById(R.id.quantity_value);
            holder.quantity_value_pre = convertView.findViewById(R.id.quantity_value_pre);
            holder.full_shpped = convertView.findViewById(R.id.full_shpped);
            holder.partial_shipped = convertView.findViewById(R.id.partial_shipped);
            holder.running = convertView.findViewById(R.id.running);
            holder.ex_fact_per = convertView.findViewById(R.id.ex_fact_per);


            convertView.setTag(holder);
        }else {
            holder = (ShipmentViewHolder) convertView.getTag();
        }

        int pos = position + 1;

        holder.seral.setText(String.valueOf(pos));
        holder.com_name.setText(shipmentModels.get(position).getCOMPANY_NAME());
        holder.buyer_name.setText(shipmentModels.get(position).getBUYER_NAME());
        holder.quantity.setText(shipmentModels.get(position).getQUANTITY());
        holder.quantity_value.setText(shipmentModels.get(position).getQUANTITY_VALUE());
        holder.quantity_value_pre.setText(shipmentModels.get(position).getQUANTITY_VALUE_PERCENTAGE());
        holder.full_shpped.setText(shipmentModels.get(position).getFULL_SHIPPED());
        holder.partial_shipped.setText(shipmentModels.get(position).getPARTIAL_SHIPPED());
        holder.running.setText(shipmentModels.get(position).getRUNNING());
        holder.ex_fact_per.setText(shipmentModels.get(position).getEX_FACTORY_PERCENTAGE());


        return convertView;
    }


    public class ShipmentViewHolder {
        public TextView seral, com_name, buyer_name, quantity, quantity_value, quantity_value_pre, full_shpped,partial_shipped,running,ex_fact_per,tquantity;
    }
}
