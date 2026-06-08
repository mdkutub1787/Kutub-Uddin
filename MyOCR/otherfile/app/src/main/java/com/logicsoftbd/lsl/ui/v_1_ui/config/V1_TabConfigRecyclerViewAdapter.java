package com.logicsoftbd.lsl.ui.v_1_ui.config;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigStyleWiseDataItemModel;

import java.util.ArrayList;
import java.util.List;

public class V1_TabConfigRecyclerViewAdapter extends RecyclerView.Adapter<V1_TabConfigRecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<V1_ConfigStyleWiseDataItemModel> dtlsIndexArrayList = new ArrayList<>();
    private OnHeadListener mOnHeadListener;
    private Integer selectedItemPosition = -1;

    public V1_TabConfigRecyclerViewAdapter(Context context, List<V1_ConfigStyleWiseDataItemModel> dtlsIndexArrayList, OnHeadListener mOnHeadListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_wise_tab_config_item, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._jobNoTV.setText(dtlsIndexArrayList.get(position).getJobNo());
        holder._styleTV.setText(dtlsIndexArrayList.get(position).getStyleRefNo());
        holder._poNumberTV.setText(dtlsIndexArrayList.get(position).getPoNumber());
        holder._itemNoTV.setText(dtlsIndexArrayList.get(position).getItemName());
        holder._irNumberTV.setText(dtlsIndexArrayList.get(position).getIrNumber());
        holder._buyerNameTV.setText(dtlsIndexArrayList.get(position).getBuyerName());

        if(selectedItemPosition == position || dtlsIndexArrayList.get(position).getStatus()) {
            holder._jobNoTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._jobNoTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._styleTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._styleTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._poNumberTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._poNumberTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._itemNoTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._itemNoTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._irNumberTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._irNumberTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._buyerNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._buyerNameTV.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            holder._jobNoTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._jobNoTV.setTextColor(Color.parseColor("#000000"));

            holder._styleTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._styleTV.setTextColor(Color.parseColor("#000000"));

            holder._poNumberTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._poNumberTV.setTextColor(Color.parseColor("#000000"));

            holder._itemNoTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._itemNoTV.setTextColor(Color.parseColor("#000000"));

            holder._irNumberTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._irNumberTV.setTextColor(Color.parseColor("#000000"));

            holder._buyerNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._buyerNameTV.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView _jobNoTV, _styleTV, _poNumberTV, _itemNoTV, _irNumberTV, _buyerNameTV;
        LinearLayout _layout;
        OnHeadListener onHeadListener;

        public ViewHolder(@NonNull View itemView, OnHeadListener mOnHeadListener) {
            super(itemView);
            _layout = itemView.findViewById(R.id.ly);
            _jobNoTV = itemView.findViewById(R.id.jobNoTV);
            _styleTV = itemView.findViewById(R.id.styleTV);
            _poNumberTV = itemView.findViewById(R.id.poNumberTV);
            _itemNoTV = itemView.findViewById(R.id.itemNoTV);
            _irNumberTV = itemView.findViewById(R.id.irNumberTV);
            _buyerNameTV = itemView.findViewById(R.id.buyerNameTV);

            this.onHeadListener = mOnHeadListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selectedItemPosition = getAdapterPosition();
            onHeadListener.onHeadClick(getAdapterPosition(), v);
        }
    }

    public interface OnHeadListener {
        void onHeadClick(int position, View v);
    }
}
