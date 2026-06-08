package com.logicsoftbd.lsl.ui.v_1_ui.bunlde_wise_sewing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.logicsoftbd.lsl.R;


public class V1_CustomSewingInputAdapter extends BaseAdapter {
    private Context context;

    public V1_CustomSewingInputAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return V1_SewingInputActivity.modelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return V1_SewingInputActivity.modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.sewing_input_object_layout, null, true);

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
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

       // holder.slNumber.setText(SewingInputActivity.modelArrayList.get(position).getId());
        holder.bundleNotv.setText(V1_SewingInputActivity.modelArrayList.get(position).getBundle_no());
        holder.yeartv.setText(V1_SewingInputActivity.modelArrayList.get(position).getYearNo());
        holder.jobNotv.setText(V1_SewingInputActivity.modelArrayList.get(position).getJobNo());
        holder.buyertv.setText(V1_SewingInputActivity.modelArrayList.get(position).getBuyer());
        holder.orderNotv.setText(V1_SewingInputActivity.modelArrayList.get(position).getOrderNo());
        holder.gmtsItemtv.setText(V1_SewingInputActivity.modelArrayList.get(position).getItemNo());
        holder.countrytv.setText(V1_SewingInputActivity.modelArrayList.get(position).getCountry());
        holder.colortv.setText(V1_SewingInputActivity.modelArrayList.get(position).getColorNo());
        holder.sizetv.setText(V1_SewingInputActivity.modelArrayList.get(position).getSizeNo());
        //holder.qtytv.setText(SewingInputActivity.modelArrayList.get(position).getQuantity());

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
