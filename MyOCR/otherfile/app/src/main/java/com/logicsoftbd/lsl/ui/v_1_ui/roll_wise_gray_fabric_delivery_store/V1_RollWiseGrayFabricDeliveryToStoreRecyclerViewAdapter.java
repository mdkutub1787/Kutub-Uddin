package com.logicsoftbd.lsl.ui.v_1_ui.roll_wise_gray_fabric_delivery_store;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_GreyRollDeliveryItemModel;

import java.util.ArrayList;

public class V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter extends RecyclerView.Adapter<V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter.ViewHolder> {
    private ArrayList<V1_GreyRollDeliveryItemModel> greyRollDeliveryItemModels;
    private Context context;
    private OnRemoveHeadListener mOnRemoveHeadListener;

    public V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter(ArrayList<V1_GreyRollDeliveryItemModel> greyRollDeliveryItemModels, OnRemoveHeadListener mOnRemoveHeadListener, Context context) {
        this.greyRollDeliveryItemModels = greyRollDeliveryItemModels;
        this.mOnRemoveHeadListener = mOnRemoveHeadListener;
        this.context = context;
    }

    @NonNull
    @Override
    public V1_RollWiseGrayFabricDeliveryToStoreRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gray_fabric_delivery_store_layout, parent, false);
        return new ViewHolder(view, mOnRemoveHeadListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.slTV.setText(String.valueOf(position+1));
        viewHolder.barcodeTV.setText(greyRollDeliveryItemModels.get(position).getBarcodeNo());
        viewHolder.rollWgtTV.setText(greyRollDeliveryItemModels.get(position).getWeight());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return greyRollDeliveryItemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView slTV, barcodeTV, rollWgtTV;
        private ImageButton removeBT;
        private OnRemoveHeadListener onRemoveHeadListener;

        public ViewHolder(@NonNull View itemView, OnRemoveHeadListener mOnRemoveHeadListener) {
            super(itemView);
            slTV = itemView.findViewById(R.id.serialTV);
            barcodeTV = itemView.findViewById(R.id.barcodeNoTV);
            rollWgtTV = itemView.findViewById(R.id.weightTV);

            removeBT = itemView.findViewById(R.id.deleteBtn);

            this.onRemoveHeadListener = mOnRemoveHeadListener;
            removeBT.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.deleteBtn:
                    onRemoveHeadListener.onRemoveHeadClick(getAdapterPosition(), v);
                    break;
            }
        }
    }
    public interface OnRemoveHeadListener {
        void onRemoveHeadClick(int position, View v);
    }
}
