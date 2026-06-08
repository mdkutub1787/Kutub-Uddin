package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagDeliveryResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;

import java.util.List;

public class V1_BagDeliveryRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagDeliveryRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagDeliveryResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_BagDeliveryRecyclerViewAdapter(List<V1_BagDeliveryResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagDeliveryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_delivery_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        if(bagKeepingResponses.get(position).getQrNo() != null){
            String qrCode =  bagKeepingResponses.get(position).getQrNo().length() > 6
                    ? bagKeepingResponses.get(position).getQrNo().substring(0, 3) + "..." + bagKeepingResponses.get(position).getQrNo().substring(bagKeepingResponses.get(position).getQrNo().length() - 3)
                    : bagKeepingResponses.get(position).getQrNo() != null ? bagKeepingResponses.get(position).getQrNo() : "";
            viewHolder.qrCodeNoTV.setText(qrCode);
        }
        if(bagKeepingResponses.get(position).getRfidNo() != null){
            String rfId =  bagKeepingResponses.get(position).getRfidNo().length() > 6
                    ? bagKeepingResponses.get(position).getRfidNo().substring(0, 3) + "..." + bagKeepingResponses.get(position).getRfidNo().substring(bagKeepingResponses.get(position).getRfidNo().length() - 3)
                    : bagKeepingResponses.get(position).getRfidNo();
            viewHolder.rfidNoTV.setText(rfId);
        }
        viewHolder.batchNoTV.setText(bagKeepingResponses.get(position).getBatchNo());
        viewHolder.weightTV.setText(bagKeepingResponses.get(position).getWeight());
        viewHolder.irbrTV.setText(bagKeepingResponses.get(position).getIrIb());
        viewHolder.colorTV.setText(bagKeepingResponses.get(position).getFabColorName());
        viewHolder.buyerNameTV.setText(bagKeepingResponses.get(position).getBuyerName());
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
        private ImageButton removeBT, moreBT;
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
            buyerNameTV = itemView.findViewById(R.id.buyerNameTV);

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
