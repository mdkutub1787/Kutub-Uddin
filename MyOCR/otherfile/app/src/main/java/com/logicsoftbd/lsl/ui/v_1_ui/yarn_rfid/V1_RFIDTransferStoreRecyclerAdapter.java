package com.logicsoftbd.lsl.ui.v_1_ui.yarn_rfid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.logicsoftbd.lsl.R;
import com.logicsoftbd.lsl.data.network.v1_model.V1_RFIDTransferModel;

import java.util.List;

public class V1_RFIDTransferStoreRecyclerAdapter extends RecyclerView.Adapter<V1_RFIDTransferStoreRecyclerAdapter.ViewHolder> {
        List<V1_RFIDTransferModel.Datum> rfidTransferModels;
        Context context;
        OnSelectSelectListener mOnSelectListener;
        Integer selectedItemPosition = -1;

        public V1_RFIDTransferStoreRecyclerAdapter(List<V1_RFIDTransferModel.Datum> rfidTransferModels, Context context, OnSelectSelectListener mOnSelectListener) {
                this.rfidTransferModels = rfidTransferModels;
                this.context = context;
                this.mOnSelectListener = mOnSelectListener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rfid_transfer_item_layout, parent, false);
                return new ViewHolder(view, mOnSelectListener);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                int count = position + 1;
                viewHolder.serialTV.setText(String.valueOf(count));
//                viewHolder.rfidNoTV.setText(rfidTransferModels.get(position).getRfidNo());
//                viewHolder.weightTV.setText(rfidTransferModels.get(position).getBagWeight());
//
//                if(rfidTransferModels.get(position).isSelected()) {
//                        viewHolder.unSelectCheckBox.setChecked(true);
//                }else{
//                        viewHolder.unSelectCheckBox.setChecked(false);
//                }
        }

        @Override
        public int getItemViewType(int position) {
                return position;
        }

        @Override
        public int getItemCount() {
                return rfidTransferModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                TextView rfidNoTV, weightTV, serialTV;
                ImageView deleteBtn, qrCodeClick;
                CheckBox unSelectCheckBox;
                OnSelectSelectListener onSelectSelectListener;

                public ViewHolder(@NonNull View itemView, OnSelectSelectListener onSelectSelectListener) {
                        super(itemView);
                        rfidNoTV = itemView.findViewById(R.id.rfidNoTV);
                        weightTV = itemView.findViewById(R.id.weightTV);
                        serialTV = itemView.findViewById(R.id.serialTV);
                        unSelectCheckBox = itemView.findViewById(R.id.unSelectCheckBox);

                        this.onSelectSelectListener = onSelectSelectListener;
                        unSelectCheckBox.setOnClickListener(this);
                }

                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onClick(View v) {
                        if (v.getId() == R.id.unSelectCheckBox) {
                                selectedItemPosition = getAdapterPosition();
                                notifyDataSetChanged();
                                if(unSelectCheckBox.isChecked()) {
                                        unSelectCheckBox.setChecked(false);
                                }
                                onSelectSelectListener.onSelectClick(selectedItemPosition, v);
                        }
                }
        }

        public interface OnSelectSelectListener {
                void onSelectClick(int position, View v);
        }
}

