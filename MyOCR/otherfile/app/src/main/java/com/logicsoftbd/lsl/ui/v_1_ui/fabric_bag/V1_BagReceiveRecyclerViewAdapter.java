package com.logicsoftbd.lsl.ui.v_1_ui.fabric_bag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagKeepingResponse;
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReceiveResponse;

import java.util.List;

public class V1_BagReceiveRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagReceiveRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagReceiveResponse.ResultSet> bagKeepingResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_BagReceiveRecyclerViewAdapter(List<V1_BagReceiveResponse.ResultSet> bagKeepingResponses, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.bagKeepingResponses = bagKeepingResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagReceiveRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_receive_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bagNoTV.setText(bagKeepingResponses.get(position).getBagNo());
        viewHolder.qrCodeNoTV.setText(bagKeepingResponses.get(position).getQrNo());
        viewHolder.companyNameTV.setText(bagKeepingResponses.get(position).getCompanyName());
        viewHolder.storeNameTV.setText(bagKeepingResponses.get(position).getStoreName());
        viewHolder.weightTV.setText(bagKeepingResponses.get(position).getWeight());
        viewHolder.uomTV.setText(bagKeepingResponses.get(position).getUom());
        viewHolder.noOfRollTV.setText(bagKeepingResponses.get(position).getRollQnty());
        viewHolder.roomNameTV.setText(bagKeepingResponses.get(position).getRoomName());
        viewHolder.rackNameTV.setText(bagKeepingResponses.get(position).getRackName());
        viewHolder.shelfNameTV.setText(bagKeepingResponses.get(position).getShelfName());
        viewHolder.checkBox.setChecked(bagKeepingResponses.get(position).getChecked());

        viewHolder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            bagKeepingResponses.get(position).setChecked(isChecked);
        });

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
        private TextView companyNameTV, bagNoTV, storeNameTV, qrCodeNoTV, noOfRollTV, weightTV, uomTV, roomNameTV, rackNameTV, shelfNameTV;
        private ImageButton removeBT, moreBT;
        private CheckBox checkBox;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            bagNoTV = itemView.findViewById(R.id.bagNoTV);
            qrCodeNoTV = itemView.findViewById(R.id.qrCodeNoTV);
            noOfRollTV = itemView.findViewById(R.id.noOfRollTV);
            companyNameTV = itemView.findViewById(R.id.companyNameTV);
            storeNameTV = itemView.findViewById(R.id.storeNameTV);
            weightTV = itemView.findViewById(R.id.weightTV);
            uomTV = itemView.findViewById(R.id.uomTV);
            roomNameTV = itemView.findViewById(R.id.roomNameTV);
            rackNameTV = itemView.findViewById(R.id.rackNameTV);
            shelfNameTV = itemView.findViewById(R.id.shelfNameTV);
            checkBox = itemView.findViewById(R.id.checkBox);

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
