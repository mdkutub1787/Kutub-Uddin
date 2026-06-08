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
import com.logicsoftbd.lsl.data.network.v1_model.V1_BagReturnResponse;

import java.util.List;

public class V1_BagReturnRecyclerViewAdapter extends RecyclerView.Adapter<V1_BagReturnRecyclerViewAdapter.ViewHolder> {
    private List<V1_BagReturnResponse.ResultSet> bagReturnResponses;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_BagReturnRecyclerViewAdapter(List<V1_BagReturnResponse.ResultSet> bagReturnResponses, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.bagReturnResponses = bagReturnResponses;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_BagReturnRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_return_item_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bagNoTV.setText(bagReturnResponses.get(position).getBagNo());
        viewHolder.qrCodeNoTV.setText(bagReturnResponses.get(position).getQrNo());
        viewHolder.companyNameTV.setText(bagReturnResponses.get(position).getCompanyName());
        viewHolder.storeNameTV.setText(bagReturnResponses.get(position).getStoreId());
        viewHolder.weightTV.setText(bagReturnResponses.get(position).getWeight());
        viewHolder.uomTV.setText(bagReturnResponses.get(position).getUom());
        viewHolder.noOfRollTV.setText(bagReturnResponses.get(position).getRollQnty());
        viewHolder.roomNameTV.setText(bagReturnResponses.get(position).getRoomName());
        viewHolder.rackNameTV.setText(bagReturnResponses.get(position).getRackName());
        viewHolder.shelfNameTV.setText(bagReturnResponses.get(position).getShelfName());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return bagReturnResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView companyNameTV, bagNoTV, storeNameTV, qrCodeNoTV, noOfRollTV, weightTV, uomTV, roomNameTV, rackNameTV, shelfNameTV;
        private ImageButton removeBT, moreBT;
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
