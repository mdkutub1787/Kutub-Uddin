package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_AOPDeptBagReceiveResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingDataBySystemResponse;

import java.util.List;

public class V1_AOPDeptReceiveRecyclerViewAdapter extends RecyclerView.Adapter<V1_AOPDeptReceiveRecyclerViewAdapter.ViewHolder> {
    private List<V1_AOPDeptBagReceiveResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_AOPDeptReceiveRecyclerViewAdapter(List<V1_AOPDeptBagReceiveResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_AOPDeptReceiveRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aop_dept_rcv_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        V1_AOPDeptBagReceiveResponse.ResultSet currentItem = bagKeepingResponses.get(position);

        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        viewHolder.batchNoTV.setText(bagKeepingResponses.get(position).getBatchNo());
        viewHolder.weightTV.setText(bagKeepingResponses.get(position).getWeight());
        viewHolder.irbrTV.setText(bagKeepingResponses.get(position).getInternalRef());
        viewHolder.colorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.buyerNameTV.setText(bagKeepingResponses.get(position).getBuyerName());
        viewHolder.qrCodeNoTV.setText(bagKeepingResponses.get(position).getQrNo());
        viewHolder.rfidNoTV.setText(bagKeepingResponses.get(position).getRfidNo());
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
        private TextView bagNoTV, qrCodeNoTV, rfidNoTV, batchNoTV, weightTV, irbrTV, colorTV, buyerNameTV;
        private ImageButton removeBT;
        private LinearLayout ly;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            qrCodeNoTV = itemView.findViewById(R.id.qrCodeNoTV);
            rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
            batchNoTV = itemView.findViewById(R.id.batchNoTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            irbrTV = itemView.findViewById(R.id.irbrTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            colorTV = itemView.findViewById(R.id.colorTV);
            buyerNameTV = itemView.findViewById(R.id.buyerNameTV);
            ly = itemView.findViewById(R.id.ly);

            removeBT = itemView.findViewById(R.id.removeBT);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.removeBT:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }

    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
}
