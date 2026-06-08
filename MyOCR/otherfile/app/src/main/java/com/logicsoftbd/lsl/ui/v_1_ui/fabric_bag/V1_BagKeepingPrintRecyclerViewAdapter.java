package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;



import android.annotation.SuppressLint;
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
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagPrintResponse;

import java.util.ArrayList;
import java.util.List;

public class V1_BagKeepingPrintRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagKeepingPrintRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagPrintResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;
    private OnPrintHeadListener mOnPrintHeadListener;

    public V1_BagKeepingPrintRecyclerViewAdapter(List<V1_BagPrintResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, OnPrintHeadListener mOnPrintHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.mOnPrintHeadListener = mOnPrintHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagKeepingPrintRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_keeping_item_print_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener, mOnPrintHeadListener);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        viewHolder.batchNoTV.setText(bagKeepingResponses.get(position).getBatchNo());
//        viewHolder.weightTV.setText(bagKeepingResponses.get(position).getFinishWeight());
        double finishWeight = 0.0;
        try {
            finishWeight = Double.parseDouble(bagKeepingResponses.get(position).getFinishWeight());
        } catch (NumberFormatException e) {
            finishWeight = 0.0;
        }
        viewHolder.weightTV.setText(String.format("%.2f", finishWeight));

//        viewHolder.greyWeightTV.setText(bagKeepingResponses.get(position).getGreyWeight());
        double greyWeight = 0.0;
        try {
            if(bagKeepingResponses.get(position).getGreyWeight() != null)
            greyWeight = Double.parseDouble(bagKeepingResponses.get(position).getGreyWeight());
        } catch (NumberFormatException e) {
            greyWeight = 0.0;
        }

        viewHolder.greyWeightTV.setText(String.format("%.2f", greyWeight));

        viewHolder.gsmTV.setText(bagKeepingResponses.get(position).getGsm());
        viewHolder.diaTV.setText(bagKeepingResponses.get(position).getDia());
        viewHolder.irbrTV.setText(bagKeepingResponses.get(position).getIrIb());
        viewHolder.colorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.buyerNameTV.setText(bagKeepingResponses.get(position).getBuyerName());
        viewHolder.bagColorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.rfidNoTV.setText(bagKeepingResponses.get(position).getRfidNo());
        viewHolder.aopTV.setText(bagKeepingResponses.get(position).getAop().equals("0") ? "NO" : "YES");
        viewHolder.fabricTypeTV.setText(bagKeepingResponses.get(position).getFabricTypeName());


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
        private TextView bagNoTV, batchNoTV, irbrTV, colorTV, buyerNameTV, rfidNoTV, bagColorTV, gsmTV, diaTV, weightTV, greyWeightTV, fabricTypeTV, aopTV;

        private ImageButton removeBT, printBT;
        private LinearLayout ly;
        private OnRemoveHeadListener onRemoveHeadListener;
        private OnPrintHeadListener onPrintHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener, OnPrintHeadListener mOnPrintHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            gsmTV = itemView.findViewById(R.id.gsmTV);
            diaTV = itemView.findViewById(R.id.diaTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            greyWeightTV = itemView.findViewById(R.id.greyWeightTV);
            batchNoTV = itemView.findViewById(R.id.batchNoTV);
            irbrTV = itemView.findViewById(R.id.irbrTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            buyerNameTV = itemView.findViewById(R.id.buyerNameTV);
            fabricTypeTV = itemView.findViewById(R.id.fabricTypeTV);
            aopTV = itemView.findViewById(R.id.aopTV);
            rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
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
