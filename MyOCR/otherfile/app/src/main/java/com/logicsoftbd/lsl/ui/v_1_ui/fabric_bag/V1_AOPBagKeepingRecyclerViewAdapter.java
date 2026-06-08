package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;



import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPBagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;

import java.util.ArrayList;
import java.util.List;

public class V1_AOPBagKeepingRecyclerViewAdapter extends RecyclerView.Adapter<V1_AOPBagKeepingRecyclerViewAdapter.ViewHolder> {
    private List<V1_AOPBagKeepingResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;
    private OnPrintHeadListener mOnPrintHeadListener;
    private List<String> _fabricTypeName = new ArrayList<>();
    private List<String> _fabricTypeId = new ArrayList<>();

    public V1_AOPBagKeepingRecyclerViewAdapter(List<V1_AOPBagKeepingResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, OnPrintHeadListener mOnPrintHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.mOnPrintHeadListener = mOnPrintHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_AOPBagKeepingRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aop_bag_keeping_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener, mOnPrintHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        V1_AOPBagKeepingResponse.ResultSet currentItem = bagKeepingResponses.get(position);

        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        viewHolder.batchNoTV.setText(bagKeepingResponses.get(position).getBatchNo());
        viewHolder.irbrTV.setText(bagKeepingResponses.get(position).getInternalRef());
        viewHolder.colorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.buyerNameTV.setText(bagKeepingResponses.get(position).getBuyerName());
        viewHolder.aopWeightTV.setText(bagKeepingResponses.get(position).getAopWeight());
        viewHolder.solidWeightTV.setText(bagKeepingResponses.get(position).getSolidWeight());
        viewHolder.rfidNoTV.setText(bagKeepingResponses.get(position).getRfidNo());
        viewHolder.qrNoTV.setText(bagKeepingResponses.get(position).getQrNo());
        viewHolder.bagColorTV.setText(bagKeepingResponses.get(position).getBagColorName());

        if(bagKeepingResponses.get(position).getPrintingStatus()){
            viewHolder.ly.setBackgroundColor(Color.parseColor("#c9ebee"));
        }else{
            viewHolder.ly.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        if(bagKeepingResponses.get(position).getSaveStatus()){
            viewHolder.printBT.setVisibility(View.VISIBLE);
        }else{
            viewHolder.printBT.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return bagKeepingResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bagNoTV, qrNoTV, rfidNoTV, batchNoTV, irbrTV, colorTV, buyerNameTV, aopWeightTV, solidWeightTV, bagColorTV;
        private ImageButton removeBT, printBT;
        private LinearLayout ly;
        private OnRemoveHeadListener onRemoveHeadListener;
        private OnPrintHeadListener onPrintHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener, OnPrintHeadListener mOnPrintHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            batchNoTV = itemView.findViewById(R.id.batchNoTV);
            qrNoTV = itemView.findViewById(R.id.qrNoTV);
            irbrTV = itemView.findViewById(R.id.irbrTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            buyerNameTV = itemView.findViewById(R.id.buyerNameTV);
            rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
            aopWeightTV = itemView.findViewById(R.id.aopWeightTV);
            solidWeightTV = itemView.findViewById(R.id.solidWeightTV);
            bagColorTV = itemView.findViewById(R.id.bagColorTV);
            ly = itemView.findViewById(R.id.ly);

            removeBT = itemView.findViewById(R.id.removeBT);
            printBT = itemView.findViewById(R.id.printBT);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);

            this.onPrintHeadListener = mOnPrintHeadListener;
            printBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
                case R.id.printBT:
                    onPrintHeadListener.onPrintHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
    public interface OnPrintHeadListener {
        void onPrintHeadClick(int position, View v);
    }
}
