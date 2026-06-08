package com.logicsoftbd.lsl.ui.v_1_ui.linking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_LinkingInputModel;

import java.util.ArrayList;


public class V1_LinkingInputAdapter extends ArrayAdapter<V1_LinkingInputModel>{
     ArrayList<V1_LinkingInputModel> sInputModelClasses;
     LayoutInflater vi;
    int Resource;

    public V1_LinkingInputAdapter(@NonNull Context context, int resource, ArrayList<V1_LinkingInputModel> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sInputModelClasses = objects;
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
        ViewHolder holder;

            holder = new V1_LinkingInputAdapter.ViewHolder();

        if(convertView == null)
        {
            convertView = vi.inflate(R.layout.linking_input_object_layout, null);

            holder.slNumber = convertView.findViewById(R.id.idTV);
            holder.bundleNotv = convertView.findViewById(R.id.bundleNoTV);
            holder.yeartv = convertView.findViewById(R.id.yearTV);
            holder.jobNotv = convertView.findViewById(R.id.jobNoTV);
            holder.buyertv = convertView.findViewById(R.id.buyerTV);
            holder.orderNotv = convertView.findViewById(R.id.orderNoTV);
            holder.gmtsItemtv = convertView.findViewById(R.id.gmtsTV);
            holder.countrytv = convertView.findViewById(R.id.countryTV);
            holder.colortv = convertView.findViewById(R.id.colorTV);
            holder.sizetv = convertView.findViewById(R.id.sizeTV);
            holder.qtytv = convertView.findViewById(R.id.qtyTV);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        int i = position + 1;

        holder.slNumber.setText(String.valueOf(i));
        holder.bundleNotv.setText(sInputModelClasses.get(position).getBundle_no());
        holder.yeartv.setText(sInputModelClasses.get(position).getYearNo());
        holder.jobNotv.setText(sInputModelClasses.get(position).getJobNo());
        holder.buyertv.setText(sInputModelClasses.get(position).getBuyer());
        holder.orderNotv.setText(sInputModelClasses.get(position).getOrderNo());
        holder.gmtsItemtv.setText(sInputModelClasses.get(position).getItemNo());
        holder.countrytv.setText(sInputModelClasses.get(position).getCountry());
        holder.colortv.setText(sInputModelClasses.get(position).getColorNo());
        holder.sizetv.setText(sInputModelClasses.get(position).getSizeNo());
        holder.qtytv.setText(String.valueOf(sInputModelClasses.get(position).getQuantity()));

        return convertView;
    }

    public class ViewHolder {
        //sewing Input Field
        private TextView slNumber;
        private TextView bundleNotv;
        private TextView yeartv;
        private TextView jobNotv;
        private TextView buyertv;
        private TextView orderNotv;
        private TextView gmtsItemtv;
        private TextView countrytv;
        private TextView colortv;
        private TextView sizetv;
        private TextView qtytv;
    }
}
