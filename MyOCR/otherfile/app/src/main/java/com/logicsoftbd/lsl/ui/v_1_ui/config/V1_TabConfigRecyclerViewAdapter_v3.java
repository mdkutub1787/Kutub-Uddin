package com.logicsoftbd.lsl.ui.v_1_ui.config;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_ConfigStyleWiseDataItemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class V1_TabConfigRecyclerViewAdapter_v3 extends RecyclerView.Adapter<V1_TabConfigRecyclerViewAdapter_v3.ViewHolder> implements Filterable {
    private static final String TAG = "V1_TabConfigRecyclerVie";
    private Context context;
    private List<V1_ConfigStyleWiseDataItemModel> dtlsIndexArrayList = new ArrayList<>();
    private List<V1_ConfigStyleWiseDataItemModel> searchedDtlsIndexArrayList = new ArrayList<>();
    private OnHeadListener mOnHeadListener;
    private Integer selectedItemPosition = -1;

    public V1_TabConfigRecyclerViewAdapter_v3(Context context, List<V1_ConfigStyleWiseDataItemModel> dtlsIndexArrayList, OnHeadListener mOnHeadListener) {
        this.context = context;
        this.dtlsIndexArrayList = dtlsIndexArrayList;
        this.mOnHeadListener = mOnHeadListener;
        this.searchedDtlsIndexArrayList = new ArrayList<>(dtlsIndexArrayList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_wise_tab_config_item_v3, parent, false);
        return new ViewHolder(view, mOnHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder._jobNoTV.setText(dtlsIndexArrayList.get(position).getJobNo());
        holder._styleTV.setText(dtlsIndexArrayList.get(position).getStyleRefNo());
        holder._itemNoTV.setText(dtlsIndexArrayList.get(position).getItemName());
        holder._irNumberTV.setText(dtlsIndexArrayList.get(position).getIrNumber());
        holder._buyerNameTV.setText(dtlsIndexArrayList.get(position).getBuyerName());
        holder._poNoTV.setText(dtlsIndexArrayList.get(position).getPoNumber());

        if(dtlsIndexArrayList.get(position).getVariableStatus().equals("1")){
            holder._poNoLayout.setVisibility(View.GONE);
        }else if(dtlsIndexArrayList.get(position).getVariableStatus().equals("2")){
            holder._poNoLayout.setVisibility(View.VISIBLE);
        }

        if(dtlsIndexArrayList.get(position).getStatus()) {
            holder._jobNoTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._jobNoTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._styleTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._styleTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._itemNoTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._itemNoTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._irNumberTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._irNumberTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._buyerNameTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._buyerNameTV.setTextColor(Color.parseColor("#FFFFFF"));

            holder._poNoTV.setBackgroundColor(Color.parseColor("#FF861F"));
            holder._poNoTV.setTextColor(Color.parseColor("#FFFFFF"));
        }
        else{
            holder._jobNoTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._jobNoTV.setTextColor(Color.parseColor("#000000"));

            holder._styleTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._styleTV.setTextColor(Color.parseColor("#000000"));

            holder._itemNoTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._itemNoTV.setTextColor(Color.parseColor("#000000"));

            holder._irNumberTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._irNumberTV.setTextColor(Color.parseColor("#000000"));

            holder._buyerNameTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._buyerNameTV.setTextColor(Color.parseColor("#000000"));

            holder._poNoTV.setBackgroundColor(Color.parseColor("#E5F0FF"));
            holder._poNoTV.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return dtlsIndexArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<V1_ConfigStyleWiseDataItemModel> getCaseModelResponses = new ArrayList<>();

            if(constraint.toString().isEmpty()){
                getCaseModelResponses.addAll(searchedDtlsIndexArrayList);
            }else {
                for(V1_ConfigStyleWiseDataItemModel itemList: searchedDtlsIndexArrayList){
                    if(itemList.getJobNo().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                       itemList.getStyleRefNo().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                       itemList.getPoNumber().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                       itemList.getItemName().toLowerCase().contains(constraint.toString().toLowerCase())){
                        getCaseModelResponses.add(itemList);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = getCaseModelResponses;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            try{
                dtlsIndexArrayList.clear();
                dtlsIndexArrayList.addAll((Collection<? extends V1_ConfigStyleWiseDataItemModel>) results.values);
                notifyDataSetChanged();
            }catch (Exception e){
                Log.d(TAG, "publishResults: "+e.getMessage());
            }
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        TextView _jobNoTV, _poNoTV, _styleTV, _itemNoTV, _irNumberTV, _buyerNameTV;
        LinearLayout _layout, _poNoLayout;
        OnHeadListener onHeadListener;

        public ViewHolder(@NonNull View itemView, OnHeadListener mOnHeadListener) {
            super(itemView);
            _layout = itemView.findViewById(R.id.ly);
            _poNoTV = itemView.findViewById(R.id.poNoTV);
            _jobNoTV = itemView.findViewById(R.id.jobNoTV);
            _styleTV = itemView.findViewById(R.id.styleTV);
            _itemNoTV = itemView.findViewById(R.id.itemNoTV);
            _irNumberTV = itemView.findViewById(R.id.irNumberTV);
            _buyerNameTV = itemView.findViewById(R.id.buyerNameTV);
            _poNoLayout = itemView.findViewById(R.id.poNoLayout);

            this.onHeadListener = mOnHeadListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            selectedItemPosition = getAdapterPosition();
            onHeadListener.onHeadClick(getAdapterPosition(), v);
        }
    }

    public interface OnHeadListener {
        void onHeadClick(int position, View v);
    }
}
