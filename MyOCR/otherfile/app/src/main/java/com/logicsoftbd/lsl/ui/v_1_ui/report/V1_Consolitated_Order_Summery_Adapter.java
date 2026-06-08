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
import com.logicsoftbd.lsl.data.network.v1_model.V1_Consolitated_Order_Summery_Model;

import java.util.ArrayList;

public class V1_Consolitated_Order_Summery_Adapter extends ArrayAdapter<V1_Consolitated_Order_Summery_Model> {

    ArrayList<V1_Consolitated_Order_Summery_Model> consolitatedOrderModels;
    LayoutInflater vi;
    int Resource;

    public V1_Consolitated_Order_Summery_Adapter(@NonNull Context context, int resource, ArrayList<V1_Consolitated_Order_Summery_Model> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        consolitatedOrderModels = objects;
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
        ConsolitadedViewHolder holder;

        holder = new V1_Consolitated_Order_Summery_Adapter.ConsolitadedViewHolder();

        if(convertView == null)
        {
            convertView = vi.inflate(R.layout.consolitated_report_items, null);

            holder.seral = convertView.findViewById(R.id.serial_no);
            holder.month = convertView.findViewById(R.id.com_name);
            holder.com_name = convertView.findViewById(R.id.buyer_name);
            holder.confirm = convertView.findViewById(R.id.quantity);
            holder.projection = convertView.findViewById(R.id.quantity_value);
            holder.confirm_quantity = convertView.findViewById(R.id.quantity_value_pre);
            holder.confirm_ammount = convertView.findViewById(R.id.full_shpped);
            holder.avg = convertView.findViewById(R.id.partial_shipped);



            convertView.setTag(holder);
        }else {
            holder = (ConsolitadedViewHolder) convertView.getTag();
        }

        int pos = position + 1;

        holder.seral.setText(String.valueOf(pos));
        holder.month.setText(consolitatedOrderModels.get(position).getMONTH());
        holder.com_name.setText(consolitatedOrderModels.get(position).getCOMPANY());
        holder.confirm.setText(consolitatedOrderModels.get(position).getCONFIRM());
        holder.projection.setText(consolitatedOrderModels.get(position).getPROJECTION());
        holder.confirm_quantity.setText(consolitatedOrderModels.get(position).getCONFIRM_QTY());
        holder.confirm_ammount.setText(consolitatedOrderModels.get(position).getCONFIRM_AMOUNT());
        holder.avg.setText(consolitatedOrderModels.get(position).getAVG());



        return convertView;
    }


    public class ConsolitadedViewHolder {
        public TextView seral, month, com_name,confirm, projection, confirm_quantity, confirm_ammount, avg;
    }
}
